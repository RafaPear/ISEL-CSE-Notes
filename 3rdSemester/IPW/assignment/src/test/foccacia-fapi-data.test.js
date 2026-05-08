import sinon from "sinon";
import { expect } from "chai";
import { fapi } from "../main/data/foccacia-fapi-data.mjs";
import { getMock } from "./utils/readJson.js";
import { __test__ } from "../main/error/foccacia-error.mjs";

describe("Foccacia fapi-data", () => {
    let fetchStub;

    beforeEach(() => {
        fetchStub = sinon.stub(global, "fetch");
    });

    afterEach(() => {
        fetchStub.restore();
    });

    describe("getCompetitions", () => {
        it("gets competitions successfully", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaCompetitions.json'),
            });

            const expectedResult = [
                { code: 'ACL', name: 'AFC Champions League' },
                { code: 'QCAF', name: 'WC Qualification CAF' },
                { code: 'AC', name: 'Africa Cup' }
            ]
            const competitions = await fapi.getCompetitions();

            expect(competitions).to.deep.equal(expectedResult);
        });

        it("error when getCompetitions receives error response", async () => {
            fetchStub.resolves({
                ok: false,
                status: 401,
                json: async () => ({ message: "Invalid API key" }),
            });

            try {
                await fapi.getCompetitions();
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {
                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(err.code).to.equal(401);
            }
        });

        it("when getCompetitions receives empty competitions, returns empty array", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => ({ competitions: [] }),
            });
            const competitions = await fapi.getCompetitions();
            expect(competitions).to.deep.equal([]);
        });
    });

    describe("getTeams", () => {
        it("gets teams successfully", async () => {
            const jsonData = await getMock('./src/test/utils/mockaTeams.json');
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => jsonData,
            });
            const result = await fapi.getTeams('PL', 2025);

            jsonData.teams.forEach((team, index) => {
                expect(result[index].name).to.equal(team.name);
                expect(result[index].country).to.equal(team.area.name);
                team.squad.forEach((player, pIndex) => {
                    expect(result[index].players[pIndex].id).to.equal(player.id);
                    expect(result[index].players[pIndex].name).to.equal(player.name);
                    expect(result[index].players[pIndex].position).to.equal(player.position);
                    expect(result[index].players[pIndex].dateOfBirth).to.equal(player.dateOfBirth);
                    expect(result[index].players[pIndex].nationality).to.equal(player.nationality);
                    expect(result[index].players[pIndex].currentTeam.id).to.equal(team.id);
                    expect(result[index].players[pIndex].currentTeam.name).to.equal(team.name);
                });
            });
        });

        it("error when getTeams receives error response", async () => {
            fetchStub.resolves({
                ok: false,
                status: 403,
                json: async () => ({ message: "Access forbidden" }),
            });

            try {
                await fapi.getTeams('PL', 2025);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {
                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(err.code).to.equal(403);
            }
        });

        it("when getTeams receives empty teams, returns empty array", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => ({ teams: [] }),
            });
            const teams = await fapi.getTeams('PL', 2025);
            expect(teams).to.deep.equal([]);
        });
    });
});
