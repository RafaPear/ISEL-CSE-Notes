package serie2.problema.imp1

import serie2.problema.CoreInterface
import serie2.problema.GREEN
import serie2.problema.ProgressBar
import serie2.problema.RED
import serie2.problema.RESET
import serie2.problema.UniquePair
import serie2.problema.YELLOW
import java.io.File

/**
 * Implementação 1 da interface [CoreInterface] utilizando `kotlin.collections`.
 * Esta classe trata da leitura de pontos de ficheiros, armazenamento num mapa e operações
 * como união, interseção e diferença entre dois ficheiros.
 */
class CoreImp1 : CoreInterface {

    /** Indica se os dados foram carregados com sucesso */
    private var isDataLoaded = false

    /**
     * Mapa que associa pontos (x, y) a pares únicos de ficheiros em que aparecem.
     * A chave é um [Point] e o valor é um [UniquePair] com os nomes dos ficheiros.
     */
    private var points: HashMap<Point, UniquePair<String>> = HashMap()

    /** Guarda os nomes dos ficheiros carregados */
    private var loadedFileNames: UniquePair<String> = UniquePair()

    /**
     * Representa um ponto no plano com coordenadas [x] e [y].
     */
    private data class Point(val x: Double, val y: Double)

    /**
     * Carrega dois ficheiros contendo pontos no plano.
     *
     * Em caso de erro, limpa os dados carregados anteriormente.
     *
     * @param pathA Caminho para o primeiro ficheiro.
     * @param pathB Caminho para o segundo ficheiro.
     */
    override fun load(pathA: String, pathB: String) {
        println("$YELLOW Core: Loading $pathA and $pathB $RESET")
        try {
            val fileA = File(pathA).name
            val fileB = File(pathB).name
            points = loadPointsMap(pathA to fileA, pathB to fileB)
            loadedFileNames.x = fileA
            loadedFileNames.y = fileB
            isDataLoaded = true
            println("$GREEN Core: Data loaded successfully. $RESET")
        } catch (e: Exception) {
            points.clear()
            loadedFileNames = UniquePair()
            isDataLoaded = false
            println("${RED}Core error: An error occurred: ${e.message}${RESET}")
        }
    }

    /**
     * Escreve no ficheiro [outName] a união de todos os pontos lidos.
     *
     * @param outName Caminho ou nome do ficheiro de saída.
     */
    override fun union(outName: String) {
        if (!checkData()) return
        writePointsToFile(points, outName)
        println("$GREEN Core: Union written to $outName $RESET")
    }

    /**
     * Escreve no ficheiro [outName] apenas os pontos que aparecem nos dois ficheiros.
     *
     * @param outName Caminho ou nome do ficheiro de saída.
     */
    override fun intersection(outName: String) {
        if (!checkData()) return
        writePointsToFile(points, outName) { it.isSimilar(loadedFileNames) }
        println("$GREEN Core: Intersection written to $outName $RESET")
    }

    /**
     * Escreve no ficheiro [outName] os pontos que aparecem apenas no primeiro ficheiro.
     *
     * @param outName Caminho ou nome do ficheiro de saída.
     */
    override fun difference(outName: String) {
        if (!checkData()) return
        val diffCause = UniquePair(loadedFileNames.x, null)
        writePointsToFile(points, outName) { it.isSimilar(diffCause) }
        println("$GREEN Core: Difference written to $outName $RESET")
    }

    /**
     * Carrega múltiplos ficheiros de pontos para um mapa.
     *
     * @param files Tuplos contendo o caminho e a label (nome) do ficheiro.
     * @return [HashMap] que associa cada ponto ao(s) ficheiro(s) onde aparece.
     */
    private fun loadPointsMap(vararg files: Pair<String, String>): HashMap<Point, UniquePair<String>> {
        val map = HashMap<Point, UniquePair<String>>()
        for ((path, label) in files) {
            parseFile(path, map, label)
        }
        return map
    }

    /**
     * Lê um ficheiro linha a linha, extrai os pontos e atualiza o mapa.
     *
     * @param file Caminho do ficheiro.
     * @param map Mapa onde os pontos serão guardados.
     * @param label Nome/identificador do ficheiro.
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
                        val x = parts[2].toDouble()
                        val y = parts[3].toDouble()
                        val point = Point(x, y)
                        map.computeIfAbsent(point) { UniquePair() }.add(label)
                    }
                }
                count++

                progressBar.updateProgress(count)
            }
            progressBar.stop()
        } catch (e: Exception) {
            println("${RED}Core error: Failed to parse file $file due to ${e.message} $RESET")
        }
    }

    /**
     * Escreve pontos num ficheiro, com uma condição opcional.
     *
     * @param points Mapa de pontos e ficheiros onde aparecem.
     * @param filename Caminho do ficheiro de saída.
     * @param condition Função, lambda que define se o ponto deve ser escrito ou não.
     */
    private fun writePointsToFile(
        points: HashMap<Point, UniquePair<String>>,
        filename: String,
        condition: (point: UniquePair<String>) -> Boolean = { true }
    ) {
        try {
            val startTime = System.currentTimeMillis()
            val total = points.size
            var count = 0
            val progressBar = ProgressBar(total, startTime)
            progressBar.start()
            File(filename).bufferedWriter().use { writer ->
                for ((point, files) in points) {
                    if (condition(files)) {
                        writer.write("${point.x.toLong()} ${point.y.toLong()}\n")
                    }
                    count++
                    progressBar.updateProgress(count)  // Corrigido aqui, era 'i', agora é 'count'
                }
            }
            progressBar.stop()
        } catch (e: Exception) {
            println("$RED Core error: Failed to write to file $filename due to ${e.message} $RESET")
        }
    }


    /**
     * Verifica se os dados foram carregados.
     *
     * @return `true` se os dados estão carregados, `false` caso contrário.
     */
    private fun checkData(): Boolean {
        if (!isDataLoaded) {
            println("$RED Core error: Data not loaded. Use load <fileA> <fileB> first. $RESET")
            return false
        }
        return true
    }
}
