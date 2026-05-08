package visualizer

data class RenderConfig(
    var nodeRadius: Int = 24,
    var edgeColor: Int = 0x444444,
    var nodeColor: Int = 0x2196F3,
    var labelColor: Int = 0xFFFFFF,
    var linkDistance: Int = 140
)