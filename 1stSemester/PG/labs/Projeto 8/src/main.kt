fun main(){
    println(firstChar("Hello World"))
    println(upper("Hello World"))
}

fun firstChar(str: String): Char{
    return str[0]
}

fun upper(str: String): String{
    var rStr : String = ""
    for(i in 0..str.length-1){
        if (str[i] in 'a'..'z'){
            rStr += str[i] - 32
    } else rStr += str[i]
    }

    return rStr
}
