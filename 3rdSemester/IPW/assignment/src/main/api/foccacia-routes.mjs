import services from "../Services/foccacia-logic.mjs";
import utils from "../utils/foccacia-utils.mjs";

/**
 * Retrieves a list of competitions.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function getCompetitions (req, res)
{
    const data = await services.getCompetitions()
    return res.status(200).json(data)
}

/**
 * Retrieves a list of teams for a specific competition and season.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function getTeams (req, res)
{
    const competitionCode = req.params.competitionCode
    const year = parseInt(req.query.season)
    const data = await services.getTeams(competitionCode, year)
    return res.status(200).json(data)
}

/**
 * Creates a new user with a randomly generated token.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function createUser (req, res)
{   
    const username = req.body.username;
    const token = crypto.randomUUID();
    await services.createUser(username, token);
    return res.status(201).json({ "username": username, "token": token });
}

/**
 * Deletes a user based on their authentication token.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function deleteUser (req, res)
{
    const token = utils.parseAuthToken(req);
    await services.deleteUser(token);
    return res.status(204).end();
}

/**
 * Retrieves all users.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function getAllUsers (req, res)
{
    const data = await services.getAllUsers();
    return res.status(200).json(data);
}

/**
 * Creates a new group for a user.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function createGroup (req, res)
{
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const groupName = req.body.name;
    const description = req.body.description;
    const competition = req.body.competition;
    const year = req.body.year;

    if (!utils.isValidString(groupName)) {
        return res.status(400).json({ error: "Invalid group name" });
    }
    if (!utils.isValidString(description)) {
        return res.status(400).json({ error: "Invalid description" });
    }
    if (!utils.isValidString(competition)) {
        return res.status(400).json({ error: "Invalid competition" });
    }
    if (!utils.isValidNumber(year) || year <= 0) {
        return res.status(400).json({ error: "Invalid year" });
    }

    await services.createGroup(token, groupName, description, competition, year);
    return res.status(201).end();
}

/**
 * Edits the details of an existing group.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function editGroup (req, res)
{
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const groupId = parseInt(req.params.groupId);
    if (!utils.isValidNumber(groupId) || groupId <= 0) {
        return res.status(400).json({ error: "Invalid group ID" });
    }

    const groupName = req.body.name;
    const description = req.body.description;

    if (!utils.isValidString(groupName)) {
        return res.status(400).json({ error: "Invalid group name" });
    }
    if (!utils.isValidString(description)) {
        return res.status(400).json({ error: "Invalid description" });
    }

    await services.editGroup(token, groupId, groupName, description);
    return res.status(200).end();
}

/**
 * Lists all groups associated with a user.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function listAllGroups (req, res)
{
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const data = await services.listAllGroups(token);
    return res.status(200).json(data);
}

/**
 * Deletes a group based on its ID.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function deleteGroup (req, res)
{
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const groupId = parseInt(req.params.groupId);
    if (!utils.isValidNumber(groupId) || groupId <= 0) {
        return res.status(400).json({ error: "Invalid group ID" });
    }

    await services.deleteGroup(token, groupId);
    return res.status(204).end();
}

/**
 * Retrieves the details of a specific group.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function getGroupDetails (req, res)
{
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const groupId = parseInt(req.params.groupId);
    if (!utils.isValidNumber(groupId) || groupId <= 0) {
        return res.status(400).json({ error: "Invalid group ID" });
    }

    const data = await services.getGroupDetails(token, groupId);
    return res.status(200).json(data);
}

/**
 * Adds a player to a group.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function addPlayer (req, res)
{
    // Header authorization
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const groupId = parseInt(req.params.groupId);
    if (!utils.isValidNumber(groupId) || groupId <= 0) {
        return res.status(400).json({ error: "Invalid group ID" });
    }

    const playerId = parseInt(req.params.playerId);
    if (!utils.isValidNumber(playerId) || playerId <= 0) {
        return res.status(400).json({ error: "Invalid player ID" });
    }

    await services.addPlayer(token, groupId, playerId);
    return res.status(200).end();
}

/**
 * Removes a player from a group.
 *
 * @param {Object} req - The HTTP request object.
 * @param {Object} res - The HTTP response object.
 * @returns {Promise<void>} - A promise that resolves when the response is sent.
 */
async function removePlayer (req, res)
{
    const token = utils.parseAuthToken(req);
    if (!utils.isValidString(token)) {
        return res.status(400).json({ error: "Invalid token" });
    }

    const groupId = parseInt(req.params.groupId);
    if (!utils.isValidNumber(groupId) || groupId <= 0) {
        return res.status(400).json({ error: "Invalid group ID" });
    }

    const playerId = parseInt(req.params.playerId);
    if (!utils.isValidNumber(playerId) || playerId <= 0) {
        return res.status(400).json({ error: "Invalid player ID" });
    }

    await services.removePlayer(token, groupId, playerId);
    return res.status(200).end();
}

/**
 * Authenticates a user based on their token.
 *
 * @param {string} token - The user's authentication token.
 * @returns {Promise<boolean>} - A promise that resolves to true if the user is authenticated, false otherwise.
 */
async function authUser (token)
{
    return services.authUser(token);
}

export const webApi = {
    getCompetitions,
    getTeams,
    createUser,
    deleteUser,
    getAllUsers,
    createGroup,
    editGroup,
    listAllGroups,
    deleteGroup,
    getGroupDetails,
    addPlayer,
    removePlayer,
    authUser,
}

export default webApi