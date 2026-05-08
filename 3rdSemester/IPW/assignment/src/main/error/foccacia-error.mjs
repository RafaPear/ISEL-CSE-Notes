import utils from "../utils/foccacia-utils.mjs";
/**
 * File that contains error functions, with each throwing an error
 * with an internal message and code, and an optional cause.
 */

/**
 * Throws a 400 Bad Request error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function BadRequest(cause, method) {
    throwError(400, "Bad Request", cause, method);
}

/**
 * Throws a 401 Unauthorized error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function Unauthorized(cause, method) {
    throwError(401, "Unauthorized", cause, method);
}

/**
 * Throws a 403 Forbidden error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function Forbidden(cause, method) {
    throwError(403, "Forbidden", cause, method);
}

/**
 * Throws a 404 Not Found error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function NotFound(cause, method) {
    throwError(404, "Not Found", cause, method);
}

/**
 * Throws a 409 Conflict error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function Conflict(cause, method) {
    throwError(409, "Conflict", cause, method);
}

/**
 * Throws a 429 Too Many Requests error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function TooManyRequests(cause, method) {
    throwError(429, "Too Many Requests", cause, method);
}

/**
 * Throws a 500 Internal Server Error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function InternalServerError(cause, method) {
    throwError(500, "Internal Server Error", cause, method);
}

/**
 * Throws a 501 Not Implemented error.
 *
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function NotImplemented(cause, method) {
    throwError(501, "Not Implemented", cause, method);
}

/**
 * Constructs and throws an error object.
 *
 * @param {number} code - The HTTP status code.
 * @param {string} message - The error message.
 * @param {string} cause - The cause of the error.
 * @param {string} method - The method where the error occurred.
 * @throws {Object} - The constructed error object.
 */
function throwError(code, message, cause, method) {
    throw buildError(code, message, cause, method);
}

/**
 * Constructs an error object.
 *
 * @param {number} code - The HTTP status code.
 * @param {string} message - The error message.
 * @param {string} cause - The cause of the error.
 * @param {string} [method] - The method where the error occurred.
 * @returns {Object} - The constructed error object.
 */
function buildError(code, message, cause, method) {
    return {
        code: code,
        message: message,
        cause: cause,
        method: method || null,
    };
}

/**
 * Maps an application error to an HTTP JSON response with the appropriate status code.
 *
 * @param {Object} error - The error object, expected to have at least a 'code' (HTTP status code) and `message` property.
 * @param {Object} res - The HTTP response object (Express.js 'res').
 * @param {Object} [fallbackError] - The fallback error object to use if the provided error is invalid.
 * @returns {Object} - The HTTP response with the correct status and JSON error message.
 */
function ErrorMap(
    error,
    res,
    fallbackError = buildError(500, "Internal Server Error", ""),
) {
    if (!isValidError(error)) {
        return res.status(fallbackError.code)
        .json({ error: fallbackError.message, cause: fallbackError.cause });
    }
    return res.status(error.code)
    .json({ error: error.message, cause: error.cause });
}

/**
 * Validates if the provided error object is valid.
 *
 * @param {Object} error - The error object to validate.
 * @returns {boolean} - True if the error object is valid, false otherwise.
 */
function isValidError(error) {
    return (
        error &&
        typeof error.code === "number" &&
        typeof error.message === "string"
    );
}

/**
 * Extracts the origin of an error from its stack trace.
 *
 * @param {Object} error - The error object.
 * @returns {string|null} - The origin of the error, or null if not available.
 */
function getErrorOrigin(error) {
    if (!error || !error.stack) return null;

    const lines = error.stack.split("\n").map((l) => l.trim());
    const originLine = lines[1];
    if (!originLine) return null;

    const funcMatch = originLine.match(/at\s+(.*?)\s+\(/);
    const fileMatch = originLine.match(/\((.*)\)/);

    const functionName = funcMatch ? funcMatch[1] : "anonymous";

    if (!fileMatch) return functionName;

    // Extract the content inside (...)
    const fullPath = fileMatch[1];

    // Extract only the file name + line + column
    const fileName = fullPath.split("/").pop().split("\\").pop();

    return fileName;
}

/**
 * Logs a critical error that occurred in a Node.js request.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} err - The error object.
 */
function logNodeError(req, err) {
    utils.logCritical(
        `Error in ${req.method} ${req.url}: ${err.message}: ${err.cause}`,
        getErrorOrigin(err)
    );
}

/**
 * Logs an application-specific error that occurred in a request.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} err - The error object.
 */
function logFapiError(req, err) {
    utils.logError(
        `Error in ${req.method} ${req.url}: ${err.message}: ${err.cause}`,
        err.method
    );
}

/**
 * Wraps an async route handler to handle errors automatically.
 *
 * @param {function} handler - The async route handler function.
 * @returns {function} - The wrapped handler function that handles errors.
 */
function wrap(handler) {
    return async function (req, res, next) {
        utils.logInfo(`Handling ${req.method} ${req.url}`, handler.name);
        try {
            await handler(req, res, next);
        } catch (err) {
            if (!isValidError(err)) {
                logNodeError(req, err);
            } else {
                logFapiError(req, err);
            }
            ErrorMap(err, res);
        }
    };
}

export const errorSheet = {
    NotFound,
    BadRequest,
    Unauthorized,
    Forbidden,
    InternalServerError,
    Conflict,
    TooManyRequests,
    throwError,
    NotImplemented,
    ErrorMap,
    wrap,
};

export default errorSheet;

export const __test__ = {
    isValidError,
};