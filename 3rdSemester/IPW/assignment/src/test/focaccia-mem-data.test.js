import { expect } from "chai";
import { memData, __test__ } from "../main/data/foccacia-mem-data.mjs";

describe("Foccacia mem-data (Full Test Suite)", () => {

    const validPlayer = {
        id: 1,
        name: "Rafa",
        teamId: 10,
        teamName: "Benfica",
        position: "Forward",
        nationality: "PT",
        age: 21
    };

    beforeEach(() => {
        // Reset do estado global antes de cada teste
        __test__.users.length = 0;
        __test__.groups.length = 0;
    });

    afterEach(() => {
        // Limpar estado global após cada teste
        __test__.users.length = 0;
        __test__.groups.length = 0;
    });

    // ======================= USER MANAGEMENT ==========================
    describe("User Management", () => {

        it("deve adicionar um user válido", () => {
            memData.addUser("rafa", "t1");
            const users = memData.getAllUsers();
            expect(users).to.have.length(1);
            expect(users[0]).to.include({ username: "rafa", token: "t1" });
        });

        it("não deve adicionar user duplicado", () => {
            memData.addUser("rafa", "t1");
            expect(() => memData.addUser("rafa", "t2")).to.throw();
        });

        it("não deve aceitar token inválido ou username inválido", () => {
            expect(() => memData.addUser("joao", "")).to.throw();
            expect(() => memData.addUser("", "t1")).to.throw();
        });

        it("deleteUser deve remover user existente", () => {
            memData.addUser("maria", "t2");
            memData.deleteUser("t2");
            expect(memData.getAllUsers()).to.have.length(0);
        });

        it("deleteUser deve falhar se token inválido ou user não existir", () => {
            expect(() => memData.deleteUser("nonexistent")).to.throw();
        });

        it("checkUserToken deve validar corretamente", () => {
            memData.addUser("ze", "t20");
            expect(memData.checkUserToken("t20")).to.be.true;
            expect(memData.checkUserToken("wrong")).to.be.false;
        });
    });

    // ======================= GROUP MANAGEMENT ==========================
    describe("Group Management", () => {

        beforeEach(() => {
            memData.addUser("t1", "t1");
        });

        it("deve criar grupo válido", () => {
            memData.createGroup("t1", "G1", "desc", "league", 2024);
            const groups = __test__.groups;
            expect(groups).to.have.length(1);
            expect(groups[0]).to.include({ name: "G1", token: "t1" });
        });

        it("deleteGroup deve remover grupo existente", () => {
            memData.createGroup("t1", "G1", "desc", "league", 2024);
            const id = __test__.groups[0].id;
            memData.deleteGroup("t1", id);
            expect(__test__.groups).to.have.length(0);
        });

        it("deleteGroup deve falhar com token errado ou grupo inexistente", () => {
            memData.createGroup("t1", "G1", "desc", "league", 2024);
            const id = __test__.groups[0].id;
            expect(() => memData.deleteGroup("wrong", id)).to.throw();
            expect(() => memData.deleteGroup("t1", 999)).to.throw();
        });

        it("editGroup deve atualizar nome e descrição", () => {
            memData.createGroup("t1", "G1", "desc", "league", 2024);
            const id = __test__.groups[0].id;
            memData.editGroup("t1", id, "G2", "newdesc");
            expect(__test__.groups[0].name).to.equal("G2");
            expect(__test__.groups[0].description).to.equal("newdesc");
        });

        it("getGroupDetails e listAllGroups devem funcionar corretamente", () => {
            memData.createGroup("t1", "G1", "desc", "league", 2024);
            const id = __test__.groups[0].id;
            const group = memData.getGroupDetails("t1", id);
            expect(group.name).to.equal("G1");
            const list = memData.listAllGroups("t1");
            expect(list).to.have.length(1);
        });
    });

    // ======================= PLAYER MANAGEMENT ==========================
    describe("Player Management", () => {

        let groupId;

        beforeEach(() => {
            memData.addUser("t1", "t1");
            memData.createGroup("t1", "G1", "desc", "league", 2024);
            groupId = __test__.groups[0].id;
        });

        it("deve adicionar jogador válido", () => {
            memData.addPlayerToGroup("t1", groupId, validPlayer);
            expect(__test__.groups[0].players).to.have.length(1);
        });

        it("não deve adicionar jogador duplicado ou inválido", () => {
            memData.addPlayerToGroup("t1", groupId, validPlayer);
            expect(() => memData.addPlayerToGroup("t1", groupId, validPlayer)).to.throw();
            expect(() => memData.addPlayerToGroup("t1", groupId, { id: 0 })).to.throw();
        });

        it("removePlayerFromGroup deve funcionar", () => {
            memData.addPlayerToGroup("t1", groupId, validPlayer);
            memData.removePlayerFromGroup("t1", groupId, validPlayer.id);
            expect(__test__.groups[0].players).to.have.length(0);
        });

        it("não deve remover jogador com token errado ou id inexistente", () => {
            memData.addPlayerToGroup("t1", groupId, validPlayer);
            expect(() => memData.removePlayerFromGroup("wrong", groupId, validPlayer.id)).to.throw();
            expect(() => memData.removePlayerFromGroup("t1", groupId, 999)).to.throw();
        });

        it("não deve permitir mais de 11 jogadores", () => {
            for (let i = 1; i <= 11; i++) {
                memData.addPlayerToGroup("t1", groupId, { ...validPlayer, id: i });
            }
            expect(() => memData.addPlayerToGroup("t1", groupId, { ...validPlayer, id: 50 })).to.throw();
        });
    });

    // ======================= PLAYER BUILDER ==========================
    describe("Player Builder", () => {
        it("deve criar jogador válido", () => {
            const p = memData.playerObjectBuilder(1, "A", 2, "Team", "Pos", "PT", 20);
            expect(p).to.include({ id: 1, name: "A" });
        });

        it("não deve aceitar argumentos inválidos", () => {
            expect(() => memData.playerObjectBuilder(0, "A", 2, "Team", "Pos", "PT", 20)).to.throw();
            expect(() => memData.playerObjectBuilder(1, "", 2, "Team", "Pos", "PT", 20)).to.throw();
            expect(() => memData.playerObjectBuilder(1, "A", 0, "Team", "Pos", "PT", 20)).to.throw();
        });
    });

    // ======================= INTEGRAÇÃO (END-TO-END) ==========================
    describe("Integração Total", () => {

        it("user cria grupo, adiciona jogador, edita grupo e remove jogador", () => {
            memData.addUser("rafa", "t1");
            memData.createGroup("t1", "G1", "d", "liga", 2024);
            const groupId = __test__.groups[0].id;
            const p = memData.playerObjectBuilder(10, "X", 2, "Team", "Mid", "PT", 20);

            memData.addPlayerToGroup("t1", groupId, p);
            expect(__test__.groups[0].players).to.have.length(1);

            memData.editGroup("t1", groupId, "G2", "nova");
            expect(__test__.groups[0].name).to.equal("G2");

            memData.removePlayerFromGroup("t1", groupId, 10);
            expect(__test__.groups[0].players).to.have.length(0);
        });

        it("user A não pode aceder nem modificar grupos de user B", () => {
            memData.addUser("a", "ta");
            memData.addUser("b", "tb");
            memData.createGroup("ta", "GA", "d", "liga", 2024);
            const groupId = __test__.groups[0].id;

            expect(() => memData.getGroupDetails("tb", groupId)).to.throw();
            expect(() => memData.addPlayerToGroup("tb", groupId, { id: 1, name: "X" })).to.throw();
            expect(() => memData.deleteGroup("tb", groupId)).to.throw();
        });
    });
});
