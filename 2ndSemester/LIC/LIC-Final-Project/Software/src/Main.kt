// No intellij, quando testar a main certifique-se
// de que o caminho (working directory) da configuração
// está predefinido para o software. Caso contrário, as
// configurações do simulador não serão carregadas e irá
// criar configurações.

fun main(){
    APP.init()
    APP.run()
}


/*fun main(){
    KBD.init()

    while (true) {
        println(KBD.waitKey(-1))
    }
}*/
