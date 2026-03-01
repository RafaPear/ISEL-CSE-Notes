fun clear(){print("\u001b[H\u001b[2J")}

@Throws(NumberFormatException::class)

fun main(){
	
	var program = true
	
	while(program){
		while(program){
			//Set up User Input
			clear()
			println(">> Input seconds to convert, 0 to exit")
			print("<< ")
			var inputSeconds : Int = 1
			
			//Get user input and test for int
			try{
				inputSeconds = readln().toInt()
			}catch(e: Exception){
				clear()
				println("Input must be an integer: $e\nPress enter to continue...")
				readln()
				break
			}
			
			if (inputSeconds < 0){
				clear()
				println("Error: input must be greater than zero\nPress enter to continue...")
				readln()
				break
			}
			else if (inputSeconds == 0) program = false
			else{
				//if the input is greater than zero and is an integer:
				//Convert time
				if (inputSeconds > 0){
					val days = inputSeconds/86400
					val hours = (inputSeconds%86400)/3600
					val minutes = (inputSeconds%3600)/60
					val remainSeconds = (inputSeconds%3600)%60
					clear()
					print(">> $days d : $hours h : $minutes m : $remainSeconds s\nPress enter to continue...")
					readln()
				}
			}
		}
	}
}	