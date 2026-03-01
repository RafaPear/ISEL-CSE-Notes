fun main(){
	
	print(">> Input first operand\n<< ")
	val a = readln().toInt()
	
	print(">> Input operator(Valid inputs: +,-,*,/)\n<<")
	val operator = readln()[0]
	
	print(">> Input Second operand\n<< ")
	val b = readln().toInt()
	
	if (operator == '/' && b != 0){
		when(operator){
			'-' -> print(a-b)
			'+' -> print(a+b)
			'*' -> print(a*b)
			'/' -> print(a/b)
		}
	}
	else println(">> IF YOU HAVE 0 FRIENDS AND YOU HAVE 0 COOKIES, HOW MANY COOKIES CAN YOU TO YOUR FRIENDS? SEE NOTHING. YOU SADLY DONT HAVE FRIENDS AND WORSE.... DONT HAVE ANY COOKIES")
}