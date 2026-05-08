package viewer

import pt.isel.canvas.WHITE

data class Rect(val pos: Vec2 = Vec2(0, 0), val size: Vec2 = Vec2(0, 0), var color: Int = WHITE){
    fun draw(viewer: Viewer, newColor: Int = color, newRect: Rect? = null){
        val newPos = newRect?.pos ?: pos
        val newSize = newRect?.size ?: size
        viewer.screen.drawRect(newPos.x,newPos.y,newSize.x,newSize.y,newColor)
    }
}