fun main(){
	print("Input the first number to compare: ")
	val a = readln().toInt()
	print("Input the second number to compare: ")
	val b = readln().toInt()
	if (a>b) println("$a is bigger than $b")
	else if (a<b) println("$b is bigger than $a")
	else if (a==b) println("$a is equal to $b")
}