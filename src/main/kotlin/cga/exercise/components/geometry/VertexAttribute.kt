package cga.exercise.components.geometry

/**
 * Simple class that holds all information about a single vertex attribute
 * @param index      Index of the attribute
 * @param n          Number of components of this attribute
 * @param type       Type of this attribute
 * @param normalized Whether this vertex should be normalized
 * @param stride     Size in bytes of a whole vertex
 * @param offset     Offset in bytes from the beginning of the vertex to the location of this attribute data
 */
data class VertexAttribute(
        var index: Int,
        var n: Int,
        var type: Int,
        var normalized: Boolean,
        var stride: Int,
        var offset: Long
)

