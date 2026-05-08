/**
 * Utility functions for validation, logging, and token parsing.
 *
 * @module foccacia-utils
 */

import error from "../error/foccacia-error.mjs";
import dotenv from "dotenv";

dotenv.config({ quiet: true });
const ADMIN_TOKEN = process.env.ADMIN_TOKEN;
if (!ADMIN_TOKEN || ADMIN_TOKEN.length === 0) {
    console.warn("WARNING: ADMIN_TOKEN is not set. Admin routes will be unsecured.");
}

/**
 * Validates if a value is a non-empty string.
 *
 * @param {any} value - The value to validate.
 * @returns {boolean} - True if the value is a valid string, false otherwise.
 */
function isValidString(value) {
    return value && typeof value === "string" && value.length > 0;
}

/**
 * Validates if a value is a valid number.
 *
 * @param {any} value - The value to validate.
 * @returns {boolean} - True if the value is a valid number, false otherwise.
 */
function isValidNumber(value) {
    return value && typeof value === "number" && !isNaN(value) && isFinite(value);
}

/**
 * Validates if a username is valid.
 *
 * @param {string} username - The username to validate.
 * @returns {boolean} - True if the username is valid, false otherwise.
 */
function isUsernameValid(username) {
    const regex = /^[a-zA-Z0-9_ ]+$/;
    return (
        isValidString(username) &&
        username.length <= 30 &&
        regex.test(username)
    );
}

/**
 * Validates if a player object is valid.
 *
 * @param {Object} player - The player object to validate.
 * @returns {boolean} - True if the player object is valid, false otherwise.
 */
function isValidPlayer(player) {
    return (
        player &&
        isValidNumber(player.id) &&
        isValidString(player.name) &&
        isValidNumber(player.teamId) &&
        isValidString(player.teamName) &&
        isValidString(player.position) &&
        isValidString(player.nationality) &&
        isValidNumber(player.age)
    );
}

/**
 * Returns a number whose value is limited to the given range.
 *
 * @param {number} value - The value to clamp.
 * @param {number} min - The lower boundary of the range.
 * @param {number} max - The upper boundary of the range.
 * @returns {number} - A number in the range [min, max].
 */
function clamp(value, min, max) {
    return Math.min(Math.max(value, min), max);
}

/**
 * Parses the authorization token from the request headers.
 *
 * @param {Object} req - The HTTP request object.
 * @returns {string} - The parsed token.
 */
function parseAuthToken(req) {
    const raw = req.headers.authorization;
    if (!raw || !raw.startsWith("Bearer ")) {
        throw error.BadRequest("Authorization header is missing or invalid", "utils.parseAuthToken");
    }
    const token = raw.replace("Bearer ", "").trim();
    if (!isValidString(token)) {
        throw error.BadRequest("Parsed token is invalid", "utils.parseAuthToken");
    }
    return token;
}

/**
 * Logs a warning message.
 *
 * @param {string} message - The warning message to log.
 * @param {string} [method=null] - The method where the warning occurred.
 */
function logWarning(message, method = null) {
    if (method) {
        console.warn(`[WARNING] (${method}) - ${message}`);
    } else {
        console.warn(`[WARNING] ${message}`);
    }
}

/**
 * Logs an informational message.
 *
 * @param {string} message - The informational message to log.
 * @param {string} [method=null] - The method where the log occurred.
 */
function logInfo(message, method = null) {
    if (method) {
        console.log(`[INFO] (${method}) - ${message}`);
    } else {
        console.log(`[INFO] ${message}`);
    }
}

/**
 * Logs an error message.
 *
 * @param {string} message - The error message to log.
 * @param {string} [method=null] - The method where the error occurred.
 */
function logError(message, method = null) {
    if (method) {
        console.error(`[ERROR] (${method}) - ${message}`);
    } else {
        console.error(`[ERROR] ${message}`);
    }
}

/**
 * Logs a critical error message.
 *
 * @param {string} message - The critical error message to log.
 * @param {string} [method=null] - The method where the critical error occurred.
 */
function logCritical(message, method = null) {
    if (method) {
        console.error(`[CRITICAL] (${method}) - ${message}`);
    } else {
        console.error(`[CRITICAL] ${message}`);
    }
}

export const utils = {
    isValidString,
    isValidNumber,
    isUsernameValid,
    isValidPlayer,
    clamp,
    parseAuthToken,
    logWarning,
    logInfo,
    logError,
    logCritical,
};

export default utils;