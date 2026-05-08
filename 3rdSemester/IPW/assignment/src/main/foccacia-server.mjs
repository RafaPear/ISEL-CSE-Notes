/**
 * Initializes and configures the Express application..
 *
 * @module foccacia-server
 */

/**
 * Imports required modules and initializes the Express application.
 * Configures middleware, routes, and logging.
 */
import express from "express";
import dotenv from "dotenv";

// Project Imports
import webApi from "./api/foccacia-routes.mjs";
import error from "./error/foccacia-error.mjs";
import utils from "./utils/foccacia-utils.mjs";
import serverUtils from "./foccacia-server-utils.mjs";

const {
    initConfigureApp,
    adminAuthMiddleware,
    userAuthMiddleware,
    activateLoggingToFile,
} = serverUtils;

dotenv.config({ quiet: true });

const app = express();
app.use(express.json());

//Configures the application with initial settings and logging.
initConfigureApp(app);
// Only put year, month, day, hour and minute
const date = new Date()
    .toISOString()
    .slice(0, 16)
    .replace("T", "_")
    .replace(/:/g, "-");
activateLoggingToFile(`./logs/foccacia-${date}.log`);

// -------------------------- Information Routes --------------------------

//Retrieves a list of competitions.
app.get("/competitions", error.wrap(webApi.getCompetitions));

//Retrieves teams for a specific competition.
app.get("/competitions/:competitionCode/teams", error.wrap(webApi.getTeams));

// ----------------------------- User Routes -----------------------------

//Creates a new user.
app.post("/users", error.wrap(webApi.createUser));

//Deletes a user based on their authentication token.
app.delete("/users", userAuthMiddleware, error.wrap(webApi.deleteUser));

//Retrieves all users (admin only).
app.get("/users", adminAuthMiddleware, error.wrap(webApi.getAllUsers));

// --------------------------- Group Routes ---------------------------

//Creates a new group for a user.
app.post("/groups", userAuthMiddleware, error.wrap(webApi.createGroup));

//Edits the details of an existing group.
app.put("/groups/:groupId", userAuthMiddleware, error.wrap(webApi.editGroup));

//Retrieves the details of a specific group.
app.get(
    "/groups/:groupId",
    userAuthMiddleware,
    error.wrap(webApi.getGroupDetails)
);

//Lists all groups associated with a user.
app.get("/groups", userAuthMiddleware, error.wrap(webApi.listAllGroups));

//Deletes a group based on its ID.
app.delete(
    "/groups/:groupId",
    userAuthMiddleware,
    error.wrap(webApi.deleteGroup)
);

// --------------------------- Player Routes ---------------------------

//Adds a player to a group.
app.post(
    "/groups/:groupId/:playerId",
    userAuthMiddleware,
    error.wrap(webApi.addPlayer)
);

//Removes a player from a group.
app.delete(
    "/groups/:groupId/:playerId",
    userAuthMiddleware,
    error.wrap(webApi.removePlayer)
);

// Server Start
const PORT = process.env.PORT;

//Starts the server and listens on the specified port.
app.listen(PORT, () => {
    utils.logInfo(`::RUNNING:: on port ${PORT}`, "foccacia-server");
});
