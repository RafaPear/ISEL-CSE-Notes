fun main(){
	println("Type F to convert to fahrenheit, C to celsius")
	val a = readln()
	if (a == "F") {
		println("Input value in celsius")
		val c = readln().toDouble()
		println("$c Cº are "+(c*9/5+32)+" Fº")
	}
	else if (a == "C") {
		println("Input value in fahrenheit")
		val c  = readln().toDouble
		println("$c Fº are "+((c-32)*5/9)+" Cº")
	}
}
