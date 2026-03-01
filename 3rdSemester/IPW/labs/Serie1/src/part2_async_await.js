import fs from "fs/promises";

const outputFile = "teams.json";
const inputFile = "teams-ids.json";
const envFile = "env.json";

async function getToken(service, env = envFile, header = "X-Auth-Token") {
  const data = await fs.readFile(env, "utf-8");
  return JSON.parse(data)[service][header];
}

async function getTeam(id, token) {
  const resp = await fetch(`https://api.football-data.org/v4/teams/${id}`, {
    headers: { "X-Auth-Token": token },
  });
  if (!resp.ok) throw new Error(`Failed to fetch team ${id}`);
  return await resp.json();
}

function parseTeam(d, id) {
  return {
    id: d.id ?? id,
    name: d.name ?? "NOT FOUND",
    players: d.squad ? d.squad.map((p) => p.name) : [],
  };
}

async function getTeamsFromFile(file) {
  const data = await fs.readFile(file, "utf-8");
  return JSON.parse(data)["teams-ids"];
}

async function saveTeamsToFile(teams, file) {
  await fs.writeFile(file, JSON.stringify(teams, null, 2));
  console.log("WRITE TO", file, "DONE");
}

async function checkLastRun(seconds) {
  try {
    const stats = await fs.stat(outputFile);
    const age = (Date.now() - stats.mtimeMs) / 1000;
    return { recent: age < seconds, ageInSeconds: age };
  } catch {
    return { recent: false, ageInSeconds: 0 };
  }
}

async function main() {
  const { recent, ageInSeconds } = await checkLastRun(60);
  if (recent) {
    console.log("AGE IN SECONDS:", ageInSeconds);
    console.log("Teams file is recent, no need to fetch again.");
    return;
  }

  console.log("GETTING TEAMS...");

  try {
    const token = await getToken("football-data.org");
    const teamIds = await getTeamsFromFile(inputFile);
    const teams = [];

    for (const id of teamIds) {
      try {
        const team = await getTeam(id, token);
        teams.push(parseTeam(team, id));
      } catch {
        teams.push(parseTeam({}, id));
      }
    }

    await saveTeamsToFile(teams, outputFile);
  } catch (err) {
    console.error("ERROR:", err.message);
  } finally {
    console.log("END");
  }
}

main();
