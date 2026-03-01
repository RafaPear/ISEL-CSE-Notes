fun main(){
	print("Input the first number to compare: ")
	val a = readln().toInt()
	print("Input the second number to compare: ")
	val b = readln().toInt()
	print("Input the third number to compare: ")
	val c = readln().toInt()
	if (a>=b && a>=c) println("$a is bigger than $b or $c")
	else if (b>=a && b>=c) println("$b is bigger than $a or $c")
	else println("$c is bigger than $a or $b")
}
