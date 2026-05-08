import fs from "fs";
import yaml from "js-yaml";
import express from "express";
import swaggerUi from "swagger-ui-express";
import dotenv from "dotenv";

// Project Imports
import webApi from "./api/foccacia-routes.mjs";
import error from "./error/foccacia-error.mjs";
import utils from "./utils/foccacia-utils.mjs";

dotenv.config({ quiet: true });

const ADMIN_TOKEN = process.env.ADMIN_TOKEN || "";

function activateLoggingToFile(logFilePath) {
    // Create file and dirs if it doesn't exist
    fs.mkdirSync(logFilePath.replace(/\/[^\/]*$/, ""), { recursive: true });
    if (!fs.existsSync(logFilePath)) {
        fs.writeFileSync(logFilePath, "");
    }
    const logStream = fs.createWriteStream(logFilePath, { flags: "a" });

    const originalLogInfo = utils.logInfo;
    const originalLogWarning = utils.logWarning;
    const originalLogError = utils.logError;
    const originalLogCritical = utils.logCritical;

    utils.logInfo = (message, context = "") => {
        const logMessage = `[INFO] [${new Date().toISOString()}] [${context}] ${message}\n`;
        logStream.write(logMessage);
        originalLogInfo(message, context);
    };

    utils.logWarning = (message, context = "") => {
        const logMessage = `[WARNING] [${new Date().toISOString()}] [${context}] ${message}\n`;
        logStream.write(logMessage);
        originalLogWarning(message, context);
    };

    utils.logError = (message, context = "") => {
        const logMessage = `[ERROR] [${new Date().toISOString()}] [${context}] ${message}\n`;
        logStream.write(logMessage);
        originalLogError(message, context);
    };

    utils.logCritical = (message, context = "") => {
        const logMessage = `[CRITICAL] [${new Date().toISOString()}] [${context}] ${message}\n`;
        logStream.write(logMessage);
        originalLogCritical(message, context);
    };
}

function initConfigureApp(app) {
    app.use((err, req, res, next) => {
        if (err instanceof SyntaxError && err.status === 400 && "body" in err) {
            return res.status(400).json({
                error: "Invalid JSON format",
                cause: err.message,
            });
        }

        // other errors
        return res.status(500).json({
            error: "Internal error",
        });
    });

    try {
        const swaggerDocument = yaml.load(
            fs.readFileSync("./docs/foccacia-api-spec.yaml", "utf8")
        );
        const options = {
            explorer: true,
        };

        app.use(
            "/api-docs",
            swaggerUi.serve,
            swaggerUi.setup(swaggerDocument, options)
        );
    } catch (e) {
        utils.logWarning(
            "Could not load API documentation: " + e,
            "foccacia-server"
        );
    }
}

function adminAuthMiddleware(req, res, next) {
    try {
        const token = utils.parseAuthToken(req);
        if (ADMIN_TOKEN.length == 0 || token !== ADMIN_TOKEN) {
            res.status(401).send("Unauthorized");
        } else {
            next();
        }
    } catch (err) {
        utils.logError(
            `Error in adminAuthMiddleware: ${err.message}`,
            "adminAuthMiddleware"
        );
        error.ErrorMap(
            err,
            res,
            400,
            "Bad Request",
            "Invalid or missing authentication parameters"
        );
    }
}

async function userAuthMiddleware(req, res, next) {
    try {
        const token = utils.parseAuthToken(req);
        const authenticated = await webApi.authUser(token);
        if (authenticated) {
            next();
        } else if (ADMIN_TOKEN.length > 0 && token === ADMIN_TOKEN) {
            next();
        } else {
            res.status(401).send("Unauthorized");
        }
    } catch (err) {
        utils.logError(
            `Error in userAuthMiddleware: ${err.message}`,
            "userAuthMiddleware"
        );
        error.ErrorMap(
            err,
            res,
            400,
            "Bad Request",
            "Invalid or missing authentication parameters"
        );
    }
}

const serverUtils = {
    initConfigureApp,
    activateLoggingToFile,
    adminAuthMiddleware,
    userAuthMiddleware,
};

export default serverUtils;
