
import dotenv from "dotenv";
import error from "../error/foccacia-error.mjs";
import utils from "../utils/foccacia-utils.mjs";


dotenv.config({ quiet: true });

const API_KEY = process.env.FOOTBALL_API_KEY;
const isDev = process.env.NODE_ENV === "development";

const requestsPerMinute = 10;
const lastRequestTimestamps = [];

/**
 * Provides base URL and endpoint builders for accessing football data.
 * buildURL function constructs full API URLs based on endpoint and parameters.
 * @module apiConfig
 */
const apiConfig = {
    /**
     * Builds the full API URL for a given endpoint and parameters.
     * 
     * @param {string} endPoint - The endpoint identifier.
     * @param {Object} params - Parameters required to build the URL.
     * @returns {string} - The full API URL.
     * @throws Will throw an error if the endpoint is not found or required parameters are missing.
     */
    buildURL(endPoint, params = {}) {
        const path = this.endPoints[endPoint]

        if (!path) errorSheet.NotFound(`Endpoint ${endPoint} not found`, "apiConfig.buildURL");

        return this.baseUrl + path(params)
    },

    baseUrl: "http://api.football-data.org/v4/",

    endPoints: {
        competitions: ({ competitionCode }) => {
            return competitionCode ? `competitions/${competitionCode}/` : "competitions/"
        },

        teams: function ({ competitionCode, year }) {
            if (!competitionCode) {
                errorSheet.BadRequest("<competitionCode> parameter is required.", "apiConfig.endPoints.teams");
            }
            if (!year) {
                errorSheet.BadRequest("<year> parameter is required.", "apiConfig.endPoints.teams");
            }
            const base = apiConfig.endPoints.competitions({ competitionCode })
            return `${base}teams?season=${year}`
        },
    },
}


/**
 * Checks if the rate limit for API requests has been exceeded.
 * Throws an error if the limit is exceeded.
 */
function checkRateLimit() {
    const currentTime = Date.now();
    const oneMinuteAgo = currentTime - 60000;
    // Remove timestamps older than one minute
    while (lastRequestTimestamps.length > 0 && lastRequestTimestamps[0] < oneMinuteAgo) {
        lastRequestTimestamps.shift();
    }
    if (lastRequestTimestamps.length >= requestsPerMinute) {
        error.TooManyRequests(`Server is handling too many requests. Please try again later.`, "fapi.getCompetitions");
    }
}

/**
 * Updates the rate limit tracker by adding the current timestamp.
 */
function updateRateLimit() {
    if (isDev) return; // Skip rate limiting in test environment
    const currentTime = Date.now();
    lastRequestTimestamps.push(currentTime);
}

/**
 * Retrieves a list of competitions from the Football API.
 * Each competition includes its code and name.
 *
 * @async
 * @returns {Promise<Array<{code: string, name: string}>>} - A list of competitions.
 * @throws Will throw an error if the API request fails.
 */
async function getCompetitions() {
    const url = apiConfig.buildURL("competitions");
    checkRateLimit();
    const response = await fetch(url, {
        headers: {
            "X-Auth-Token": API_KEY,
        },
    });
    updateRateLimit();
    const data = await response.json();
    if (!response.ok) {
        error.throwError(
            response.status,
            `Error fetching competitions`,
            data.message,
            "fapi.getCompetitions"
        );
    }

    const simplifiedData = data.competitions.map((competition) => ({
        code: competition.code,
        name: competition.name,
    }));
    
    return simplifiedData;
}

/**
 * Retrieves a list of teams for a specific competition and season.
 * Each team includes its name, country, and a list of players.
 *
 * @async
 * @param {string} competitionCode - The code of the competition.
 * @param {number} year - The year of the competition season.
 * @returns {Promise<Array<{name: string, country: string, players: Array<{id: number, name: string, position: string, dateOfBirth: string, nationality: string, currentTeam: {id: number, name: string}}>}>>} - A list of teams with player details.
 * @throws Will throw an error if the API request fails.
 */
async function getTeams(competitionCode, year) {
    const url = apiConfig.buildURL("teams", { competitionCode, year });
    checkRateLimit();
    const response = await fetch(url, {
        headers: {
            "X-Auth-Token": API_KEY,
        },
    });
    updateRateLimit();
    const dataJson = await response.json();
    if (!response.ok) {
        error.throwError(
            response.status,
            `Error fetching teams`,
            dataJson.message,
            "fapi.getTeams"
        );
    }

    const teamsData = dataJson.teams.map((team) => ({
        name: team.name,
        country: team.area.name,
        players: team.squad.map((player) => ({
            id: player.id,
            name: player.name,
            position: player.position,
            dateOfBirth: player.dateOfBirth,
            nationality: player.nationality,
            currentTeam: { id: team.id, name: team.name },
        })),
    }));
    return teamsData;
}

export const fapi = {
    getCompetitions,
    getTeams,
};

export default fapi;
export { apiConfig };
