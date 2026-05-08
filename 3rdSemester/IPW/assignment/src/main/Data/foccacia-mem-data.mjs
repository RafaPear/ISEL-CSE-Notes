import { errorSheet } from "../error/foccacia-error.mjs";
import utils from "../utils/foccacia-utils.mjs";
import env from "dotenv";

env.config({ quiet: true });

const ADMIN_TOKEN = process.env.ADMIN_TOKEN;

// Usar over foccacia-data-mem.mjs para dados em memória

// Retorna promisses
// Promisse Resolve; Promisse Reject

// Users and Groups storage
const users = [];
const groups = [];

if (ADMIN_TOKEN.length !== 0) {
    // Create admin user if ADMIN_TOKEN is set
    users.push({ username: "admin", token: ADMIN_TOKEN, groupAmount: 0 });
}

// ---------------- User Management ----------------

/**
 * Adds a new user to the in-memory storage.
 *
 * @param {string} username - The username of the new user.
 * @param {string} token - The authentication token for the user.
 * @throws Will throw an error if the username already exists, the token is invalid, or the username is invalid.
 */
function addUser(username, token) {
    if (users.find((u) => u.username === username)) {
        errorSheet.Conflict(`User ${username} already exists`, "data.addUser");
    }
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token for user ${username}`, "data.addUser");
    }
    if (!utils.isUsernameValid(username)) {
        errorSheet.BadRequest(`Invalid username ${username}`, "data.addUser");
    }
    users.push({ username, token, groupAmount: 0 });
}

/**
 * Deletes a user from the in-memory storage based on their token.
 *
 * @param {string} token - The authentication token of the user to delete.
 * @throws Will throw an error if the token is invalid or the user is not found.
 */
function deleteUser(token) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token for user ${username}`, "data.deleteUser");
    }
    const index = users.findIndex((u) => u.token === token);
    if (index === -1) {
        errorSheet.NotFound(`User with token ${token} not found`, "data.deleteUser");
    }
    if (users[index].token !== token) {
        errorSheet.Unauthorized(`Invalid token for user with token ${token}`, "data.deleteUser");
    }
    users.splice(index, 1);
}

/**
 * Retrieves all users from the in-memory storage.
 *
 * @returns {Array<Object>} - A list of all users.
 */
function getAllUsers() {
    return users;
}

/**
 * Checks if a user token exists in the in-memory storage.
 *
 * @param {string} token - The authentication token to check.
 * @returns {boolean} - True if the token exists, false otherwise.
 */
function checkUserToken(token) {
    const user = users.find((u) => u.token === token);
    if (!user) {
        return false;
    }
    return user.token === token;
}

/**
 * Updates the group count for a user by a specified delta.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} delta - The amount to change the group count by.
 * @returns {number} - The updated group count.
 * @throws Will throw an error if the user is not found.
 */
function updateUserGroupAmount(token, delta) {
    const user = users.find((u) => u.token === token);  
    if (!user) {
        errorSheet.NotFound(`User with token ${token} not found`, "data.updateUserGroupAmount");
    }
    user.groupAmount = utils.clamp(((user.groupAmount || 0) + delta), 0, Infinity);
    return user.groupAmount;
}

// ---------------- Group Management ----------------

/**
 * Creates a new group for a user.
 *
 * @param {string} token - The user's authentication token.
 * @param {string} groupName - The name of the group.
 * @param {string} description - The description of the group.
 * @param {string} competition - The competition associated with the group.
 * @param {number} year - The year of the competition.
 * @throws Will throw an error if the group name, description, competition, or year is invalid, or if the group already exists.
 */
function createGroup(token, groupName, description, competition, year) {
    if (typeof groupName !== "string" || groupName.length === 0) {
        errorSheet.InternalServerError(`Invalid groupName ${groupName}`, "data.createGroup");
    }
    if (typeof description !== "string") {
        errorSheet.InternalServerError(`Invalid description ${description}`, "data.createGroup");
    }
    if (typeof competition !== "string" || competition.length === 0) {
        errorSheet.InternalServerError(`Invalid competition ${competition}`, "data.createGroup");
    } 
    if (typeof year !== "number" || year <= 0) {
        errorSheet.InternalServerError(`Invalid year ${year}`, "data.createGroup");
    }
    // Amount of groups associated with user token
    const id = updateUserGroupAmount(token, 1);
    groups.push({ 
        token: token, 
        id: id,
        name: groupName, 
        description: description, 
        competition: competition.toUpperCase(), 
        year: year, 
        players: [] }
    );
}


/**
 * Validates if a user owns a group or has admin privileges.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} id - The ID of the group to validate.
 * @returns {number} - The index of the group in the groups array.
 * @throws Will throw an error if the group is not found, the token is invalid, or the user is not authorized.
 */
function validateGroupOwnership(token, id) {
    const groupIndex = groups.findIndex(
        (g) => g.id === id && 
        (g.token === token || token === process.env.ADMIN_TOKEN)
    );
    const group = groups[groupIndex];
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "data.validateGroupOwnership");
    }
    if (!group) {
        errorSheet.NotFound(`Group not found`, "data.validateGroupOwnership");
    }
    if (group.token !== token && token !== process.env.ADMIN_TOKEN) {
        errorSheet.Unauthorized(`User is not authorized to modify group ${group.name}`, "data.validateGroupOwnership");
    }
    return groupIndex;
}

/**
 * Lists all groups associated with a user.
 *
 * @param {string} token - The user's authentication token.
 * @returns {Array<Object>} - A list of groups associated with the user.
 * @throws Will throw an error if the token is invalid.
 */
function listAllGroups(token) {
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "data.listAllGroups");
    }
    return groups.filter((g) => g.token === token);
}

/**
 * Retrieves the details of a specific group.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} id - The ID of the group.
 * @returns {Object} - The details of the group.
 * @throws Will throw an error if the group ID is invalid or the user does not own the group.
 */
function getGroupDetails(token, id) {
    if (!utils.isValidNumber(id)) {
        errorSheet.BadRequest(`Invalid group id`, "data.getGroupDetails");
    }
    const groupIndex = validateGroupOwnership(token, id);
    return groups[groupIndex];
}

/**
 * Edits the name and/or description of a group.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} id - The ID of the group.
 * @param {string} [newName] - The new name for the group.
 * @param {string} [newDescription] - The new description for the group.
 * @throws Will throw an error if the group ID, new name, or new description is invalid.
 */
function editGroup(token, id, newName, newDescription) {
    if (!utils.isValidNumber(id)) {
        errorSheet.BadRequest(`Invalid group id`, "data.editGroup");
    }
    const groupIndex = validateGroupOwnership(token, id);
    if (newName){
        if (!utils.isValidString(newName)) {
            errorSheet.BadRequest(`Invalid new group name`, "data.editGroup");
        }
        groups[groupIndex].name = newName;
    }
    if (newDescription){
        if (!utils.isValidString(newDescription)) {
            errorSheet.BadRequest(`Invalid new group description`, "data.editGroup");
        }
        groups[groupIndex].description = newDescription;
    }
}

/**
 * Deletes a group associated with a user.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} id - The ID of the group to delete.
 * @throws Will throw an error if the group ID or token is invalid, or if the user does not own the group.
 */
function deleteGroup(token, id) {
    if (!utils.isValidNumber(id)) {
        errorSheet.BadRequest(`Invalid group id`, "data.deleteGroup");
    }
    if (!utils.isValidString(token)) {
        errorSheet.BadRequest(`Invalid token ${token}`, "data.deleteGroup");
    }
    const groupIndex = validateGroupOwnership(token, id);
    updateUserGroupAmount(token, -1);
    groups.splice(groupIndex, 1);
}

/**
 * Adds a player to a group.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} id - The ID of the group.
 * @param {Object} player - The player object to add.
 * @throws Will throw an error if the group ID is invalid, the player is invalid, or the group already has the maximum number of players.
 */
function addPlayerToGroup(token, id, player) {
    if (!utils.isValidNumber(id)) {
        errorSheet.BadRequest(`Invalid group id`, "data.addPlayerToGroup");
    }

    const groupIndex = validateGroupOwnership(token, id);
    validatePlayerInGroup(player, groups[groupIndex]);
    if (groups[groupIndex].players.length >= 11) {
        errorSheet.BadRequest(`Group already has maximum number of players`, "data.addPlayerToGroup");
    }
    groups[groupIndex].players.push(player);
}

/**
 * Removes a player from a group.
 *
 * @param {string} token - The user's authentication token.
 * @param {number} id - The ID of the group.
 * @param {number} playerId - The ID of the player to remove.
 * @throws Will throw an error if the player ID is not found in the group.
 */
function removePlayerFromGroup(token, id, playerId) {
    const groupIndex = validateGroupOwnership(token, id);
    const playerIndex = groups[groupIndex].players.findIndex((p) => p.id === playerId);
    if (playerIndex === -1) {
        errorSheet.NotFound(`Player with ID ${playerId} not found in group ${id}`, "data.removePlayerFromGroup");
    }
    groups[groupIndex].players.splice(playerIndex, 1);
}

// -- Player Utils --

// Validates if a player can be added to a group
function validatePlayerInGroup(player, group) {
    if (!utils.isValidPlayer(player)) {
        errorSheet.InternalServerError(`Invalid player object`, "data.validatePlayerInGroup");
    }
    validatePlayerIdInGroup(player.id, group);
}

// Validates if a player ID exists in a group
function validatePlayerIdInGroup(playerId, group) {
    const existingPlayer = group.players.find((p) => p.id === playerId);
    if (existingPlayer) {
        errorSheet.Conflict(`Player with id ${playerId} already in group`, "data.validatePlayerIdInGroup");
    }
}

// ---------------- Builders ----------------

/**
 * Builds a player object with the specified properties.
 *
 * @param {number} playerId - The ID of the player.
 * @param {string} playerName - The name of the player.
 * @param {number} teamId - The ID of the team the player belongs to.
 * @param {string} teamName - The name of the team the player belongs to.
 * @param {string} position - The position of the player.
 * @param {string} nationality - The nationality of the player.
 * @param {number} age - The age of the player.
 * @returns {Object} - The constructed player object.
 * @throws Will throw an error if any of the parameters are invalid.
 */
function playerObjectBuilder(
    playerId,
    playerName,
    teamId,
    teamName,
    position,
    nationality,
    age
) {
    if (typeof playerId !== "number" || playerId <= 0) {
        errorSheet.InternalServerError(`Invalid playerId ${playerId}`, "data.playerObjectBuilder");
    }
    if (!utils.isValidString(playerName)) {
        errorSheet.InternalServerError(`Invalid playerName ${playerName}`, "data.playerObjectBuilder");
    }
    if (typeof teamId !== "number" || teamId <= 0) {
        errorSheet.InternalServerError(`Invalid teamId ${teamId}`, "data.playerObjectBuilder");
    }
    if (!utils.isValidString(teamName)) {
        errorSheet.InternalServerError(`Invalid teamName ${teamName}`, "data.playerObjectBuilder");
    }
    if (!utils.isValidString(position)) {
        errorSheet.InternalServerError(`Invalid position ${position}`, "data.playerObjectBuilder");
    }
    if (!utils.isValidString(nationality)) {
        errorSheet.InternalServerError(`Invalid nationality ${nationality}`, "data.playerObjectBuilder");
    }
    if (typeof age !== "number" || age <= 0) {
        errorSheet.InternalServerError(`Invalid age ${age}`, "data.playerObjectBuilder");
    }
    return {
        id: playerId,
        name: playerName,
        teamId: teamId,
        teamName: teamName,
        position: position,
        nationality: nationality,
        age: age
    };
}


export const memData = {
    // User management
    addUser,
    deleteUser,
    getAllUsers,
    checkUserToken,

    // Group management
    createGroup,
    deleteGroup,
    editGroup,
    getGroupDetails,
    listAllGroups,

    // Player management
    addPlayerToGroup,
    removePlayerFromGroup,

    // Builders
    playerObjectBuilder
};

export default memData;

export const __test__ = {
    users,
    groups,
    validateGroupOwnership,
    validatePlayerInGroup,
    validatePlayerIdInGroup,
};
