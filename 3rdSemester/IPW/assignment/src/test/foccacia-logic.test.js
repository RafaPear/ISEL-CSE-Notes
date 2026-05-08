import { expect } from "chai";
import sinon from "sinon";
import { fapi } from "../main/Data/foccacia-fapi-data.mjs";
import { memData, __test__ as testData } from "../main/data/foccacia-mem-data.mjs";
import { __test__ } from "../main/error/foccacia-error.mjs";
import { services } from "../main/Services/foccacia-logic.mjs";
import { getMock } from "./utils/readJson.js";

describe("Foccacia logic", () => {
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
            expect(await services.getCompetitions()).to.deep.equal(await fapi.getCompetitions());
        });
    });

    describe("getTeams", () => {
        it("gets teams successfully", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            expect(await services.getTeams('PL', 2025)).to.deep.equal(await fapi.getTeams('PL', 2025));
        });

        it("empty arguments throw error", async () => {
            try {
                await services.getTeams();
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("invalid year argument throws error", async () => {
            try {
                await services.getTeams('PL', 'invalidYear');
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("missing year argument throws error", async () => {
            try {
                await services.getTeams('PL');
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("empty competitionCode argument throws error", async () => {
            try {
                await services.getTeams("", 2025);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });
    });

    describe("createUser", () => {
        it("creates user successfully", async () => {
            testData.users.length = 0;

            const username = "testuser";
            const tokten = "t1";

            await services.createUser(username, tokten);

            const users = memData.getAllUsers();
            expect(users).to.have.length(1);
            expect(users[0]).to.include({ username: username, token: tokten });
            testData.users.length = 0;

        });

        it("creating user with empty username throws error", async () => {
            try {
                await services.createUser("", "t1");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("creating user with invalid useranem throws error", async () => {
            try {
                await services.createUser(2, "t1");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("creating user with empty token throws error", async () => {
            try {
                await services.createUser("testuser", "");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("creating user with invalid token throws error", async () => {
            try {
                await services.createUser("testuser", 123);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });
    });

    describe("deleteUser", () => {
        beforeEach(() => {
            testData.users.length = 0;
            memData.addUser("user1", "t1");
        });

        afterEach(() => {
            testData.users.length = 0;
        });

        it("deletes user successfully", async () => {
            memData.addUser("user2", "t2");
            await services.deleteUser("t2");

            const users = memData.getAllUsers();
            expect(users).to.have.length(1);
            expect(users[0]).to.deep.equal({ username: "user1", token: "t1", groupAmount: 0 });
        });

        it("deleting user with non-existing token throws error", async () => {
            try {
                await services.deleteUser("nonexistent");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
                expect(testData.users).to.have.length(1);
            }
        });

        it("deleting user with empty token throws error", async () => {
            try {
                await services.deleteUser("");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
                expect(testData.users).to.have.length(1);
            }
        });

        it("deleting user with invalid token type throws error", async () => {
            try {
                await services.deleteUser(123);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
                expect(testData.users).to.have.length(1);
            }
        });
    });

    describe("getAllUsers", () => {
        beforeEach(() => {
            testData.users.length = 0;
        });

        afterEach(() => {
            testData.users.length = 0;
        });

        it("retrieves all users successfully", async () => {
            memData.addUser("user1", "t1");
            memData.addUser("user2", "t2");

            const users = await services.getAllUsers();
            expect(users).to.deep.equal(memData.getAllUsers())
        });
    });

    describe("authUser", () => {
        beforeEach(() => {
            testData.users.length = 0;
            memData.addUser("user1", "t1");
        });

        afterEach(() => {
            testData.users.length = 0;
        });

        it("authenticates user successfully", async () => {
            const user = await services.authUser("t1");
            expect(user).to.equal(true);
        });

        it("authenticating with empty token throws error", async () => {
            try {
                await services.authUser("");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("authenticating with invalid token type throws error", async () => {
            try {
                await services.authUser(123);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });
    });




    describe("Group Management", () => {
        beforeEach(() => {
            testData.users.length = 0;
            testData.groups.length = 0;
        });

        beforeEach(() => {
            memData.addUser("user1", "t1");
        });

        it("creates group successfully", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            const groups = memData.listAllGroups("t1");
            expect(groups).to.have.length(1);
            expect(groups[0]).to.include({ name: "G1", description: "desc", competition: "LEAGUE", year: 2024 });
        });

        it("creating group with invalid token throws error", async () => {
            try {
                await services.createGroup("", "G1", "desc", "league", 2024);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("deletes group successfully", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            const groupId = memData.listAllGroups("t1")[0].id;
            await services.deleteGroup("t1", groupId);
            const groups = memData.listAllGroups("t1");
            expect(groups).to.have.length(0);
        });

        it("deleting non-existing group throws error", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            try {
                await services.deleteGroup("t1", 999);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("deleting group with invalid token throws error", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.deleteGroup("", groupId);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("editing non-existing group throws error", async () => {
            try {
                await services.editGroup("t1", 999, "G2", "newdesc");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("editing group successfully", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            const groupId = memData.listAllGroups("t1")[0].id;
            await services.editGroup("t1", groupId, "G2", "newdesc");
            const g = memData.getGroupDetails("t1", groupId);
            expect(g.name).to.equal("G2");
            expect(g.description).to.equal("newdesc");
        });

        it("editing group with invalid token throws error", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.editGroup("", groupId, "G2", "newdesc");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("listing groups successfully", async () => {
            await services.createGroup("t1", "G1", "desc", "league", 2024);
            await services.createGroup("t1", "G2", "desc", "league", 2024);
            const list = await services.listAllGroups("t1");
            expect(list).to.have.length(2);
        });
    });

    describe("Player Management", () => {
        beforeEach(() => {
            testData.users.length = 0;
            testData.groups.length = 0;
            memData.addUser("user1", "t1");
            services.createGroup("t1", "G1", "desc", "PL", 2025);
        });


        it("adds player successfully", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            await services.addPlayer("t1", groupId, 3189);
            const group = memData.getGroupDetails("t1", groupId);
            expect(group.players).to.have.length(1);
            expect(group.players[0].id).to.equal(3189);
        });

        it("adding non-existing player throws error", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.addPlayer("t1", groupId, 1);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("adding duplicate player throws error", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            await services.addPlayer("t1", groupId, 3189);
            try {
                await services.addPlayer("t1", groupId, 3189);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("adding player with invalid token throws error", async () => {
            try {
                const groupId = memData.listAllGroups("t1")[0].id;
                await services.addPlayer("", groupId, 3189);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("adding player to non-existing group throws error", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            try {
                await services.addPlayer("t1", 999, 3189);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("adding player with invalid playerId type throws error", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.addPlayer("t1", groupId, "invalidId");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("adding player with negative playerId throws error", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.addPlayer("t1", groupId, -5);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("adding player with zero playerId throws error", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.addPlayer("t1", groupId, 0);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

    
        it("removes player successfully", async () => {
            fetchStub.resolves({
                ok: true,
                status: 200,
                json: async () => await getMock('./src/test/utils/mockaTeams.json'),
            });
            const groupId = memData.listAllGroups("t1")[0].id;
            await services.addPlayer("t1", groupId, 3189);
            await services.removePlayer("t1", groupId, 3189);
            const group = memData.getGroupDetails("t1", groupId);
            expect(group.players).to.have.length(0);
        });

        it("removing non-existing player throws error", async () => {
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.removePlayer("t1", groupId, 1);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("removing player with invalid token throws error", async () => {
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.removePlayer("", groupId, 3189);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });

        it("removing player from non-existing group throws error", async () => {
            try {
                await services.removePlayer("t1", 999, 3189);
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {


                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });
        it("removing player with invalid playerId type throws error", async () => {
            const groupId = memData.listAllGroups("t1")[0].id;
            try {
                await services.removePlayer("t1", groupId, "invalidId");
                expect.fail("Expected error");
            } catch (err) {
                if (!__test__.isValidError(err)) {
                    expect.fail(
                        `Esperado erro foccacia (com .code), mas recebi erro JS: ${err}`
                    );
                } expect(true).to.equal(true)
            }
        });
    });
});