package serie2.problema.imp2

import serie2.problema.CoreInterface
import serie2.part4.HashMap
import serie2.problema.GREEN
import serie2.problema.RED
import serie2.problema.RESET
import serie2.problema.YELLOW
import serie2.problema.BLUE
import serie2.problema.ProgressBar
import serie2.problema.UniquePair
import java.io.File

/**
 * Implementação de [CoreInterface] que utiliza uma estrutura de dados [HashMap]
 * personalizada para associar pontos a ficheiros de origem.
 */
class CoreImp2 : CoreInterface {

    /**
     * Extensão para adicionar um valor a um [UniquePair], tratando o caso
     * em que o par é `null`.
     *
     * @param value Valor a adicionar ao par.
     * @return Um [UniquePair] atualizado com o novo valor.
     */
    fun <T> UniquePair<T>?.add(value: T): UniquePair<T> {
        if (this == null) return UniquePair(value)
        this.add(value)
        return this
    }

    /** Indica se os dados foram carregados com sucesso. */
    private var isDataLoaded = false

    /**
     * Estrutura principal que associa cada ponto 2D ([Point]) ao par de ficheiros
     * onde ele foi encontrado.
     */
    private var points: HashMap<Point, UniquePair<String>> = HashMap()

    /** Guarda os nomes dos ficheiros carregados */
    private var loadedFileNames: UniquePair<String> = UniquePair()

    /** Representa um ponto no espaço bidimensional. */
    private data class Point(val x: Double, val y: Double)

    /**
     * Carrega os ficheiros especificados em [pathA] e [pathB],
     * extraindo os pontos válidos e armazenando-os com referência
     * aos ficheiros de origem.
     */
    override fun load(pathA: String, pathB: String) {
        println("${BLUE}Code: Loading $pathA and $pathB$RESET")
        try {
            val fileA = File(pathA).name
            val fileB = File(pathB).name
            points = loadPointsMap(pathA to fileA, pathB to fileB)
            loadedFileNames.x = fileA
            loadedFileNames.y = fileB
            isDataLoaded = true
            println("${GREEN}Core: Data loaded successfully. $RESET")
        } catch (e: Exception) {
            points = HashMap()
            loadedFileNames = UniquePair()
            isDataLoaded = false
            println("${RED}Core error: An error occurred: ${e.message}${RESET}")
        }
    }

    /**
     * Escreve todos os pontos carregados para o ficheiro indicado em [outName].
     */
    override fun union(outName: String) {
        if (!checkData()) return
        writePointsToFile(points, outName)
        println("${BLUE}Code: Union written to $outName$RESET")
    }

    /**
     * Escreve apenas os pontos que aparecem em ambos os ficheiros
     * (i.e., interseção) para o ficheiro indicado em [outName].
     */
    override fun intersection(outName: String) {
        if (!checkData()) return
        writePointsToFile(points, outName) { it.isSimilar(loadedFileNames) }
        println("${BLUE}Code: Intersection written to $outName$RESET")
    }

    /**
     * Escreve os pontos que estão apenas no ficheiro A e não no B
     * (i.e., diferença) para o ficheiro indicado em [outName].
     */
    override fun difference(outName: String) {
        if (!checkData()) return
        val diffCause = UniquePair(loadedFileNames.x, null)
        writePointsToFile(points, outName) { it.isSimilar(diffCause) }
        println("${BLUE}Code: Difference written to $outName$RESET")
    }

    /**
     * Carrega vários ficheiros em simultâneo e atualiza o mapa de pontos.
     *
     * @param files Lista de pares (caminho, etiqueta) dos ficheiros a processar.
     * @return Um mapa de pontos com as etiquetas dos ficheiros onde ocorrem.
     */
    private fun loadPointsMap(vararg files: Pair<String, String>): HashMap<Point, UniquePair<String>> {
        val map = HashMap<Point, UniquePair<String>>()
        for ((path, label) in files) {
            parseFile(path, map, label)
        }
        return map
    }

    /**
     * Lê um ficheiro linha a linha, extrai pontos válidos e
     * adiciona-os ao mapa fornecido, associando-os ao rótulo fornecido.
     */
    private fun parseFile(file: String, map: HashMap<Point, UniquePair<String>>, label: String) {
        try {
            val startTime = System.currentTimeMillis()
            val file = File(file)
            var count = 0
            val total = file.useLines { it.count() }
            val progressBar = ProgressBar(total, startTime)
            progressBar.start()

            file.forEachLine { line ->
                if (line.startsWith("v")) {
                    val parts = line.trim().split(" ")
                    if (parts.size >= 4) {
                        val x = parts[2].toDoubleOrNull()
                        val y = parts[3].toDoubleOrNull()
                        if (x == null || y == null) {
                            println("${YELLOW}Core warning: Invalid line in file $file -> $line$RESET")
                            throw IllegalArgumentException("Invalid coordinates")
                        }
                        val point = Point(x, y)
                        map.putList(point, label) { add(label) }
                    }
                }
                count++
                progressBar.updateProgress(count)
            }
            progressBar.updateProgress(count)
            progressBar.stop()
        } catch (e: Exception) {
            println("${RED}Core error: Failed to parse file $file due to ${e.message}$RESET")
        }
    }

    /**
     * Escreve os pontos presentes em [points] para um ficheiro.
     * A filtragem dos pontos pode ser feita usando o parâmetro [condition].
     */
    private fun writePointsToFile(
        points: HashMap<Point, UniquePair<String>>,
        filename: String,
        condition: (point: UniquePair<String>) -> Boolean = { true }
    ) {
        try {
            val startTime = System.currentTimeMillis()
            val file = File(filename)
            var count = 0
            val total = points.size
            val progressBar = ProgressBar(total, startTime)
            progressBar.start()

            file.bufferedWriter().use { writer ->
                for (ele in points) {
                    val files = ele.value
                    val point = ele.key
                    if (condition(files)) {
                        writer.write("${point.x.toLong()} ${point.y.toLong()}\n")
                    }
                    count++
                    progressBar.updateProgress(count)
                }
            }
            progressBar.updateProgress(count)
            progressBar.stop()
        } catch (e: Exception) {
            println("${RED}Core error: Failed to write to file $filename due to ${e.message}$RESET")
        }
    }

    /**
     * Verifica se os dados foram carregados antes de permitir
     * operações como união, interseção ou diferença.
     */
    private fun checkData(): Boolean {
        if (!isDataLoaded) {
            println("${RED}Core error: Data not loaded. Use load <fileA> <fileB> first.$RESET")
            return false
        }
        return true
    }
}
