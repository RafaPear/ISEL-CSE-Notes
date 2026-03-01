fun main()
{
	print(">> Input your number\n<< ")
	val input = readln()
	
	if (input.toInt() < 999 && input.toInt() > 99)
	{
		a = Input[0]
		b = Input[1]
		c = Input[2]
		print(a+b+c)
	}
	else
	{
		println("Error: Number must have only 3 digits")
	}
}


//val (a,b,c) = listOf(input[0],input[1],input[2])
//		println(a.digitToInt()+b.digitToInt()+c.digitToInt())