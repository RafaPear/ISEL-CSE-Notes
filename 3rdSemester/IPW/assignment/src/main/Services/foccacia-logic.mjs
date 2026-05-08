import fapi from "../data/foccacia-fapi-data.mjs";
import memData from "../data/foccacia-mem-data.mjs";
import { errorSheet } from "../error/foccacia-error.mjs";
import utils from "../utils/foccacia-utils.mjs";

/**
 * Retrieves a list of competitions.
 *
 * @returns {Promise<Array>} - A promise that resolves to a list of competitions.
 */
async function getCompetitions() {
    return await fapi.getCompetitions();
}

/**
 * Retrieves a list of teams for a specific competition and year.
 *
 * @param {string} competitionCode - The code of the competition.
 * @param {number} year - The year of the competition.
 * @returns {Promise<Array>} - A promise that resolves to a list of teams.
 * @throws Will throw an error if the competition code or year is invalid.
 */
async function getTeams(competitionCode, year) {
    if (!utils.isValidString(competitionCode)) {
        errorSheet.BadRequest(`Invalid competitionCode ${competitionCode}`, "services.getTeams");
    }
    if (!utils.isValidNumber(year) || year <= 0) {
        errorSheet.BadRequest(`Invalid year ${year}`, "services.getTeams");
    }
    return await fapi.getTeams(competitionCode, year);
}

/**
 * Creates a new user with the specified username and token.
 *
 * @param {string} user - The username of the new user.
 * @param {string} token - The authentication token for the user.
 * @throws Will throw an error if the username or token is invalid.
 */
async function createUser(user, token) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.createUser");
    }
    if (!utils.isValidString(user)) {
        errorSheet.BadRequest(`Invalid user ${user}`, "services.createUser");
    }
    memData.addUser(user, token);
}

/**
 * Deletes a user based on their authentication token.
 *
 * @param {string} token - The authentication token of the user to delete.
 * @throws Will throw an error if the token is invalid.
 */
async function deleteUser(token) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.deleteUser");
    }
    memData.deleteUser(token);
}

/**
 * Retrieves all users.
 *
 * @returns {Promise<Array>} - A promise that resolves to a list of all users.
 */
async function getAllUsers() {
    return memData.getAllUsers();
}

/**
 * Authenticates a user based on their token.
 *
 * @param {string} token - The authentication token of the user.
 * @returns {Promise<boolean>} - A promise that resolves to true if the user is authenticated, false otherwise.
 * @throws Will throw an error if the token is invalid.
 */
async function authUser(token) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.authUser");
    }
    return memData.checkUserToken(token);
}

/**
 * Creates a new group for a user.
 *
 * @param {string} token - The authentication token of the user.
 * @param {string} groupName - The name of the group.
 * @param {string} description - The description of the group.
 * @param {string} competition - The competition associated with the group.
 * @param {number} year - The year of the competition.
 * @throws Will throw an error if any of the parameters are invalid.
 */
async function createGroup(token, groupName, description, competition, year) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.createGroup");
    }
    if (!utils.isValidString(groupName)) {
        errorSheet.BadRequest(`Invalid groupName ${groupName}`, "services.createGroup");
    }
    if (!utils.isValidString(description)) {
        errorSheet.BadRequest(`Invalid description ${description}`, "services.createGroup");
    }
    if (!utils.isValidString(competition)) {
        errorSheet.BadRequest(`Invalid competition ${competition}`, "services.createGroup");
    }
    if (!utils.isValidNumber(year) || year <= 0) {
        errorSheet.BadRequest(`Invalid year ${year}`, "services.createGroup");
    }
    memData.createGroup(token, groupName, description, competition, year);
}

/**
 * Edits the details of an existing group.
 *
 * @param {string} token - The authentication token of the user.
 * @param {number} groupId - The ID of the group to edit.
 * @param {string} [newName] - The new name for the group.
 * @param {string} [newDescription] - The new description for the group.
 * @throws Will throw an error if any of the parameters are invalid.
 */
async function editGroup(token, groupId, newName, newDescription) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.editGroup");
    }
    if (!utils.isValidNumber(groupId)) {
        errorSheet.BadRequest(`Invalid groupId ${groupId}`, "services.editGroup");
    }
    if (!newName && !utils.isValidString(newName)) {
        errorSheet.BadRequest(`Invalid newName ${newName}`, "services.editGroup");
    }
    if (!newDescription && !utils.isValidString(newDescription)) {
        errorSheet.BadRequest(`Invalid newDescription ${newDescription}`, "services.editGroup");
    }
    memData.editGroup(token, groupId, newName, newDescription);
}

/**
 * Lists all groups associated with a user.
 *
 * @param {string} token - The authentication token of the user.
 * @returns {Promise<Array>} - A promise that resolves to a list of groups.
 * @throws Will throw an error if the token is invalid.
 */
async function listAllGroups(token) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.listAllGroups");
    }
    return memData.listAllGroups(token);
}

/**
 * Deletes a group based on its ID.
 *
 * @param {string} token - The authentication token of the user.
 * @param {number} groupId - The ID of the group to delete.
 * @throws Will throw an error if the token or group ID is invalid.
 */
async function deleteGroup(token, groupId) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.deleteGroup");
    }
    if (!utils.isValidNumber(groupId)) {
        errorSheet.BadRequest(`Invalid groupId ${groupId}`, "services.deleteGroup");
    }
    memData.deleteGroup(token, groupId);
}

/**
 * Retrieves the details of a specific group.
 *
 * @param {string} token - The authentication token of the user.
 * @param {number} groupId - The ID of the group.
 * @returns {Promise<Object>} - A promise that resolves to the details of the group.
 * @throws Will throw an error if the token or group ID is invalid.
 */
async function getGroupDetails(token, groupId) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.getGroupDetails");
    }
    if (!utils.isValidNumber(groupId)) {
        errorSheet.BadRequest(`Invalid groupId ${groupId}`, "services.getGroupDetails");
    }
    return memData.getGroupDetails(token, groupId);
}

/**
 * Adds a player to a group.
 *
 * @param {string} token - The authentication token of the user.
 * @param {number} groupId - The ID of the group.
 * @param {number} playerId - The ID of the player to add.
 * @throws Will throw an error if any of the parameters are invalid or the player cannot be added.
 */
async function addPlayer(token, groupId, playerId) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.addPlayer");
    }
    if (!utils.isValidNumber(groupId)) {
        errorSheet.BadRequest(`Invalid groupId ${groupId}`, "services.addPlayer");
    }
    if (!utils.isValidNumber(playerId)) {
        errorSheet.BadRequest(`Invalid playerId ${playerId}`, "services.addPlayer");
    }

    const groupDetails = memData.getGroupDetails(token, groupId);
    if (!groupDetails) {
        errorSheet.NotFound(`Group with ID ${groupId} not found`, "services.addPlayer");
    }

    const response = await fapi.getTeams(groupDetails.competition, groupDetails.year);
    if (!response) {
        errorSheet.InternalServerError(`Failed to fetch teams for competition ${groupDetails.competition}`, "services.addPlayer");
    }

    // Player must have the group year and competition
    const playerDetails = response
    .flatMap(team => team.players)
    .find(p => p.id === playerId);

    if (!playerDetails) {
        errorSheet.BadRequest(`Player with id ${playerId} not found in competition ${groupDetails.competition} for year ${groupDetails.year}`, "services.addPlayer");
    }

    const age = playerDetails.dateOfBirth
    ? new Date(Date.now() - new Date(playerDetails.dateOfBirth)).getUTCFullYear() - 1970
    : null;

    if (age === null)
        errorSheet.InternalServerError(`Invalid player data`, "services.addPlayer");

    const parsedPlayer = memData.playerObjectBuilder(
        playerDetails.id,
        playerDetails.name,
        playerDetails.currentTeam.id,
        playerDetails.currentTeam.name,
        playerDetails.position,
        playerDetails.nationality,
        age,
    );
    
    memData.addPlayerToGroup(token, groupId, parsedPlayer);
}

/**
 * Removes a player from a group.
 *
 * @param {string} token - The authentication token of the user.
 * @param {number} groupId - The ID of the group.
 * @param {number} playerId - The ID of the player to remove.
 * @throws Will throw an error if any of the parameters are invalid.
 */
async function removePlayer(token, groupId, playerId) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "services.removePlayer");
    }
    if (!utils.isValidNumber(groupId)) {
        errorSheet.BadRequest(`Invalid groupId ${groupId}`, "services.removePlayer");
    }
    if (!utils.isValidNumber(playerId)) {
        errorSheet.BadRequest(`Invalid playerId ${playerId}`, "services.removePlayer");
    }
    memData.removePlayerFromGroup(token, groupId, playerId);
}

export const services = {
    getCompetitions,
    getTeams,
    createUser,
    deleteUser,
    getAllUsers,
    authUser,
    createGroup,
    editGroup,
    listAllGroups,
    deleteGroup,
    getGroupDetails,
    addPlayer,
    removePlayer,
};

export default services;
