import pt.isel.canvas.*
const val GREY = 0x848484

/**Draws a Rectangle on the given [pos], [size]
 * and/or [thickness] and/or [color]. If the [thickness] or [color] parameters are ignored,
 * they default to 0 and WHITE respectively. This function is an extension to the Canvas Lib
 * that allows drawing rectangles with [Vector2] instead of the default x y parameters.*/
fun Canvas.drawRect(pos: Vector2, size: Vector2, thickness: Int? = null, color: Int? = null) {
    val newColor: Int = color ?: WHITE
    val newThickness: Int = thickness ?: 0
    drawRect(pos.x, pos.y, size.x, size.y, newColor, newThickness)
}

/**Draws an Image on the given [pos] and [size].
 * The [fileName] supports cropping with the syntax "file-name|topX,topY,width,height".
 * This function is an extension to the Canvas Lib that allows drawing images
 * with [Vector2] instead of the default x y parameters.*/
fun Canvas.drawImage(fileName: String, pos: Vector2, size: Vector2) {
    drawImage(fileName, pos.x, pos.y, size.x, size.y)
}

/**Draws a Line from the given [origin] to the given [destination]. [thickness] and [color] are available
 * but can be ignored. If the [thickness] or [color] parameters are ignored,
 * they default to 0 and WHITE respectively. This function is an extension to the Canvas Lib
 * that allows drawing lines with [Vector2] instead of the default x y parameters.*/
fun Canvas.drawLine(origin: Vector2, destination: Vector2, thickness: Int? = null, color: Int? = null) {
    val newColor: Int = color ?: WHITE
    val newThickness: Int = thickness ?: 0
    drawLine(origin.x,origin.y,destination.x,destination.y,newColor,newThickness)
}

/**Draws a given [text] on the given [pos]. [fontSize] and [color] are available
 * but can be ignored. If the [fontSize] or [color] parameters are ignored,
 * they default to 10 and WHITE respectively. This function is an extension to the Canvas Lib
 * that allows drawing text with [Vector2] instead of the default x y parameters.*/
fun Canvas.drawText(pos: Vector2, text: String, color: Int? = null, fontSize: Int? = null) {
    val newColor: Int = color ?: WHITE
    val newFontSize: Int = fontSize ?: 10
    drawText(pos.x,pos.y,text,newColor,newFontSize)
}