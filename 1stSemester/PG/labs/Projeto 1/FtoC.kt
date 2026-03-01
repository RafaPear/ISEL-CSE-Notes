fun clear(){print("\u001b[H\u001b[2J")}

fun main(){
	while (true){
		clear()
		print(">> Type F to convert to fahrenheit, C to celsius\n<< ")
		val a = readln()[0]
		if (a == 'F' || a == 'f') {
			clear()
			print(">> Input value in celsius\n<< ")
			val c = readln().toDouble()
			clear()
			println(">> $c Cº is "+(c*9/5+32)+" Fº\nPress enter to continue...")
			readln()
			clear()
			break
		}
		else if (a == 'C' || a == 'c') {
			clear()
			print(">> Input value in fahrenheit\n<< ")
			val c = readln().toDouble()
			clear()
			println(">> $c Fº is "+((c-32)*5/9)+" Cº\nPress enter to continue...")
			readln()
			clear()
			break
		}
	}
}