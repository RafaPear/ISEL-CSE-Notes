package serie2.problema

/**
 * Interface que define os métodos principais para a execução de operações de conjuntos.
 * Esta interface é utilizada para implementar operações de união, interseção e diferença entre conjuntos.
 */
interface CoreInterface {

    /**
     * Método que carrega dois conjuntos a partir de ficheiros.
     * Os ficheiros devem conter os elementos dos conjuntos a serem carregados.
     *
     * @param pathA O caminho do ficheiro que contém o primeiro conjunto.
     * @param pathB O caminho do ficheiro que contém o segundo conjunto.
     */
    fun load(pathA: String, pathB: String)

    /**
     * Método que executa a operação de união entre os dois conjuntos carregados.
     * O resultado da união é guardado num novo conjunto com o nome especificado.
     *
     * @param outName O nome do novo conjunto que irá conter o resultado da união.
     */
    fun union(outName: String)

    /**
     * Método que executa a operação de interseção entre os dois conjuntos carregados.
     * O resultado da interseção é guardado num novo conjunto com o nome especificado.
     *
     * @param outName O nome do novo conjunto que irá conter o resultado da interseção.
     */
    fun intersection(outName: String)

    /**
     * Método que executa a operação de diferença entre os dois conjuntos carregados.
     * O resultado da diferença é guardado num novo conjunto com o nome especificado.
     *
     * @param outName O nome do novo conjunto que irá conter o resultado da diferença.
     */
    fun difference(outName: String)
}