fun main()
{

	//Set up User Input
	println(">> Input seconds to convert, 0 to exit")
	print("<< ")
	
	var inputSeconds = readln().toInt()
	
	val hours = inputSeconds/3600
	val minutes = (inputSeconds%3600)/60
	val remainSeconds = (inputSeconds%3600)%60
	
	print(">> $hours h : $minutes m : $remainSeconds s\nPress enter to continue...")
	readln()
}