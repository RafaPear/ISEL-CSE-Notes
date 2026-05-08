package viewer

class Vec2(var x: Int, var y: Int) {
    operator fun plus(a: Int) = Vec2(x + a, y + a)
    operator fun plus(a: Vec2) = Vec2(x + a.x, y + a.y)

    operator fun minus(a: Int) = Vec2(x - a, y - a)
    operator fun minus(a: Vec2) = Vec2(x - a.x, y - a.y)

    operator fun times(a: Int) = Vec2(x * a, y * a)
    operator fun times(a: Vec2) = Vec2(x * a.x, y * a.y)

    operator fun div(a: Int) = Vec2(x / a, y / a)
    operator fun div(a: Vec2) = Vec2(x / a.x, y / a.y)

    operator fun rem(a: Int) = Vec2(x % a, y % a)
    operator fun rem(a: Vec2) = Vec2(x % a.x, y % a.y)
}