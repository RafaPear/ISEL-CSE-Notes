fun main(){
	
	print("Input price tag: ")
	val price : Double = readln().toDouble()
	print("Input given amount: ")
	val money : Double = readln().toDouble()
	val change : Double = money - price

	val euro2  : Int = (change/2.0).toInt()
    val euro1  : Int = ((change%2.0)/1.0).toInt()
    val cent50 : Int = (((change%2.0)%1.0)/0.5).toInt()
    val cent20 : Int = ((((change%2.0)%1.0)%0.5)/0.2).toInt()
    val cent10 : Int = (((((change%2.0)%1.0)%0.5)%0.2)/0.1).toInt()
    val cent5  : Int = ((((((change%2.0)%1.0)%0.5)%0.2)%0.1)/0.05).toInt()
    val cent2  : Int = (((((((change%2.0)%1.0)%0.5)%0.2)%0.1)%0.05)/0.02).toInt()
    var cent1  : Int = ((((((((change%2.0)%1.0)%0.5)%0.2)%0.1)%0.05)%0.02)/0.01).toInt()

    val n = euro2*2 + euro1*1 + cent50*0.5 + cent20*0.2 + cent10*0.1 + cent5*0.05 + cent2*0.02 + cent1*0.01

    if(n != change) cent1++

    if (euro2 !=0)   print("$euro2 2euro coins\n")
    if (euro1 != 0)  print("$euro1 1euro coins\n")
	if (cent50 != 0) print("$cent50 50c coins\n")
    if (cent20 != 0) print("$cent50 20c coins\n")
    if (cent10 != 0) print("$cent10 10c coins\n")
    if (cent5 != 0)  print("$cent5 5c coins\n")
    if (cent2 != 0)  print("$cent2 2c coins\n")
    if (cent1 != 0)  print("$cent1 1c coins\n")

}
