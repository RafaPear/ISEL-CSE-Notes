
import KBD.kPins
import KBD.valPins
import isel.leic.utils.Time
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Objeto responsável pela captura das teclas através do [HAL]
 */
object KBD {
	/**
	 * Define uma tecla vazia ou inválida
	 */
	const val NONE = 0.toChar()

	/**
	 * [CharArray] corresponde ao formato e ordem das teclas no teclado utilizado,
	 * o código de cada tecla corresponde ao índice no [Array]
	 */
	val CHAR_LIST = charArrayOf(
		'1', '2', '3', 'A',
		'4', '5', '6', 'B',
		'7', '8', '9', 'C',
		'*', '0', '#', 'D'
	)

	/**
	 * Define a posição, no [isel.leic.UsbPort], do bit que valida o código da tecla
	 */
	var valPins = 0b0001_0000

	/**
	 * Define a posição, no [isel.leic.UsbPort], dos bits correspondentes ao código da tecla
	 */
	var kPins = 0b0000_1111

	var ack = 0b1000_0000

	/**
	 * Inicia o objeto, definindo as variáveis [valPins] e [kPins], consoante [HAL.configPath]
	 */
	fun init() {
		HAL.init()
		try {
			val file = File(HAL.configPath)
			valPins = 0
			kPins = 0

			file.forEachLine { i ->
				if (i.contains("ob.Q[0-3]")) {
					kPins += getInputPins(i)
				} else if (i.contains("ob.Dval")) {
					valPins += getInputPins(i)
				}
			}
		} catch(_:Exception){
			Logger.getLogger("KBD").log(Level.WARNING, "No config file found, using default values")

		}
	}

	/**
	 * Retorna de imediato a tecla premida ou NONE se não houver tecla premida.
	 * @return [Char]
	 */
	fun getKey(): Char {
		if (HAL.isBit(valPins)) {
			val key = CHAR_LIST[HAL.readBits(kPins)]
			HAL.setBits(ack)
			while (HAL.isBit(valPins)) { }
			HAL.clrBits(ack)
			return key
		} else return NONE
	}

	/**
	 * Retorna de imediato o código da tecla premida ou -1 se não houver tecla premida.
	 * @return [Int]
	 */
	fun getKeyVal(): Int {
		return if (HAL.isBit(valPins))
			HAL.readBits(kPins)
		else -1
	}

	/**
	 * Retorna a tecla premida, caso ocorra antes do [timeout],
	 * ou NONE caso contrario.
	 * @param timeout em milissegundo
	 * @return [Char]
	 */
	fun waitKey(timeout: Long): Char {
		if (timeout == -1L) {
			while (true) {
				val key = getKey()
				if (key != NONE) {
					return key
				}
			}
		}
		else {
			val startTime = Time.getTimeInMillis()
			while (startTime + timeout > Time.getTimeInMillis()) {
				val key = getKey()
				if (key != NONE) {
					return key
				}
			}
		}
		return NONE
	}

	/**
	 * Retorna o código da tecla premida, caso ocorra antes do [timeout],
	 * ou -1 caso contrario.
	 * @param timeout em milissegundos
	 * @return [Int]
	 */
	fun waitKeyVal(timeout: Long): Int {
		val startTime = Time.getTimeInMillis()
		while (startTime + timeout > Time.getTimeInMillis()) {
			val key = getKeyVal()
			if (key != -1) {
				return key
			}
		}
		return -1
	}
}