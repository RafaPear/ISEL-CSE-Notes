
import RouletteDisplay.cursor
import RouletteDisplay.timeANIM
import isel.leic.utils.Time
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Responsável pelos displays de 7 segmentos
 */
object RouletteDisplay {
    /**
     * Total de display na placa
     */
    const val TOTAL_DISPLAYS = 6

    /**
     * Representação máxima do hexadecimal 15 ou F
     */
    private const val MAX_VALUE = 0x1F

    /**
     * Número de bits para o comando
     */
    private const val CMD_SIZE = 3

    /**
     * Total de bits a serem enviados
     */
    const val TOTAL_SIZE = 8

    /**
     * Comando para atualizar os displays
     */
    private const val CMD_UPDATE = 0b0000_0110

    /**
     * Posição atual do cursor
     */
    var cursor = 0
    /**
     * Indice de cada display
     */
    val POS = listOf(
        0b000, 0b001,
        0b010, 0b011,
        0b100, 0b101
    )

    /**
     * Sequencia de código para efetuar um ciclo da animação de girar
     */
    val ANIM_A = listOf(
        0x14,
        0x15,
        0x16,
        0x11,
        0x12,
        0x13,
    )

    val ANIM_B = listOf(
        listOf(0x15, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x11, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x19, 0x19, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x1F, 0x19, 0x19, 0x1F, 0x1F, 0x1F),
        listOf(0x1F, 0x1F, 0x19, 0x19, 0x1F, 0x1F),
        listOf(0x1F, 0x1F, 0x1F, 0x19, 0x19, 0x1F),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x19, 0x19),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x12),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x13),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x14),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1C, 0x1C),
        listOf(0x1F, 0x1F, 0x1F, 0x1C, 0x1C, 0x1F),
        listOf(0x1F, 0x1F, 0x1C, 0x1C, 0x1F, 0x1F),
        listOf(0x1F, 0x1C, 0x1C, 0x1F, 0x1F, 0x1F),
        listOf(0x1C, 0x1C, 0x1F, 0x1F, 0x1F, 0x1F)
    )

    val ANIM_D_2 = listOf(
        listOf(0x18, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x18, 0x18, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x18, 0x18, 0x18, 0x1F, 0x1F, 0x1F),
        listOf(0x1F, 0x18, 0x18, 0x18, 0x1F, 0x1F),
        listOf(0x1F, 0x1F, 0x18, 0x18, 0x18, 0x1F),
        listOf(0x1F, 0x1F, 0x1F, 0x18, 0x18, 0x18),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x18, 0x18),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x18),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F)
    )
    val ANIM_D_1 = listOf(
        listOf(0x10, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x10, 0x10, 0x1F, 0x1F, 0x1F, 0x1F),
        listOf(0x10, 0x10, 0x10, 0x1F, 0x1F, 0x1F),
        listOf(0x1F, 0x10, 0x10, 0x10, 0x1F, 0x1F),
        listOf(0x1F, 0x1F, 0x10, 0x10, 0x10, 0x1F),
        listOf(0x1F, 0x1F, 0x1F, 0x10, 0x10, 0x10),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x10, 0x10),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x10),
        listOf(0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F)
    )

    const val TIME_FRAME_ANIME_A = 80L
    const val TIME_FRAME_ANIME_B = 70L
    val timeANIM = TIME_FRAME_ANIME_A * ANIM_A.size
    /**
     * Inicia a classe, estabelecendo os valores iniciais.
     */
    fun init() {
        SerialEmitter.init()
        off(false)
        clrAll()
    }

    /**
     * Realiza a animação do sorteio
     * Tem uma doração de [timeANIM], padrão é 480 ms
     */
    fun animationA() {
        for (i in ANIM_A) {
            for (j in 0 until TOTAL_DISPLAYS) {
                SerialEmitter.send(
                    SerialEmitter.Destination.ROULETTE,
                    i.shl(CMD_SIZE) or POS[j],
                    TOTAL_SIZE
                )
            }
            Time.sleep(TIME_FRAME_ANIME_A)
            update()
        }
    }

    fun animationB(){
        for (anim in ANIM_B) {
            for (j in 0 until TOTAL_DISPLAYS) {
                SerialEmitter.send(
                    SerialEmitter.Destination.ROULETTE,
                    anim[j].shl(CMD_SIZE) or POS[j],
                    TOTAL_SIZE
                )
            }
            Time.sleep(TIME_FRAME_ANIME_B)
            update()
        }
    }

    fun animationC() {
        for (i in 0..1) {
            Time.sleep(100)
            for (i in 0 until TOTAL_DISPLAYS) {
                SerialEmitter.send(
                    SerialEmitter.Destination.ROULETTE,
                    0x00.shl(CMD_SIZE) or POS[i],
                    TOTAL_SIZE
                )
            }
            update()
            Time.sleep(100)
            clrAll()
        }
    }

    fun animationD(ammount: Int = 1) {
        if (ammount !in 1..2) return
        val anim = if (ammount == 1) ANIM_D_1 else ANIM_D_2
        for (ani in anim) {
            for (j in 0 until TOTAL_DISPLAYS) {
                SerialEmitter.send(
                    SerialEmitter.Destination.ROULETTE,
                    ani[j].shl(CMD_SIZE) or POS[j],
                    TOTAL_SIZE
                )
            }
            Time.sleep(TIME_FRAME_ANIME_B/4)
            update()
        }
        clrAll()
    }

    fun printIntList(list: List<Int>) {
        clrAll()
        cursor = 0
        for (id in list) {
            setValue(id)
            cursor = (cursor + 1) % (TOTAL_DISPLAYS)
        }
        update()
    }

    fun printCharList(list: List<Char>, apply : (Char) -> Int = { it.toHexInt() }) {
        clrAll()
        cursor = 0
        for (id in list) {
            setValue(apply(id))
            cursor = (cursor + 1) % (TOTAL_DISPLAYS)
        }
        update()
    }

    /**
     * Escreve no display em que se situa o [cursor] o valor de [value]
     * @param value
     */
    fun setValue(value: Int) {
        if (value > MAX_VALUE || value < 0) {
            Logger.getLogger("RouletteDisplay").log(Level.WARNING, "Value too high. Max: $MAX_VALUE, Value: $value")
            return
        }

        SerialEmitter.send(
            SerialEmitter.Destination.ROULETTE,
            value.shl(CMD_SIZE) or POS[cursor],
            TOTAL_SIZE
        )
    }

    /**
     * Envia comando para desativar/ativar a visualização do mostrador da roleta
     * @param value false os displays estão ligados
     */
    fun off(value: Boolean) {
        if (value) {
            SerialEmitter.send(
                SerialEmitter.Destination.ROULETTE,
                0b0000_1111,
                TOTAL_SIZE
            )
        } else {
            SerialEmitter.send(
                SerialEmitter.Destination.ROULETTE,
                0b0000_0111,
                TOTAL_SIZE
            )
        }
    }

    /**
     * Realiza a atualização dos displays
     */
    fun update() {
        SerialEmitter.send(
            SerialEmitter.Destination.ROULETTE,
            CMD_UPDATE,
            TOTAL_SIZE
        )
    }

    /**
     * Deixa todos os displays com o valor 0
     */
    fun clrAll() {
        for (i in POS) {
            cursor = i
            setValue(0x1F)
        }
        update()
    }

    /**
     * Escreve no display correspondente ao [indice] o valor de [value]
     * @param value
     * @param indice
     */
    fun set(value: Int, indice: Int = 0) {
        cursor = indice
        setValue(value)
    }

    fun setChar(ch: Char) = setValue(ch.toHexInt())

    private fun setAllAny(value: Int) {
        for (i in POS) {
            cursor = i
            setValue(value)
        }
        update()
    }

    fun setAll(value: Int) {
        setAllAny(value)
    }

    fun setAll(value: Char) {
        setAllAny(value.toHexInt())
    }
}