package viewmodel.graph

import androidx.compose.ui.unit.dp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import model.Complex
import model.recursiveFFT

class TFDPLayout() {
    /**
     * longRangeAttractionConstant - strength of attractive force (long-range) - B nearAttractionConstant - strength of
     * attractive t-force (near) - A repulsiveConstant - extent and magnitude of the repulsive t-force that controls the
     * longest distance of neighbors in the layout - Y
     */
    fun fft2D(input: Array<Array<Complex>>): Array<Array<Complex>> {
        val rows = input.size
        val cols = input[0].size
        val output = Array(rows) { Array(cols) { Complex(0.0, 0.0) } }

        // Transform each row
        for (i in 0 until rows) {
            output[i] = recursiveFFT(input[i])
        }

        // Transpose the output matrix
        val transposedOutput = Array(cols) { Array(rows) { Complex(0.0, 0.0) } }
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                transposedOutput[j][i] = output[i][j]
            }
        }

        // Transform each column
        for (j in 0 until cols) {
            val column = Array(rows) { i -> transposedOutput[j][i] }
            val transformedCol = recursiveFFT(column)
            for (i in 0 until rows) {
                output[i][j] = transformedCol[i]
            }
        }

        return output
    }

    fun ifft2D(input: Array<Array<Complex>>): Array<Array<Complex>> {
        val rows = input.size
        val cols = input[0].size
        val output = Array(rows) { Array(cols) { Complex() } }

        // Transform each row
        for (i in 0 until rows) {
            output[i] = recursiveFFT(input[i])
        }

        // Transpose the output matrix
        val transposedOutput = Array(cols) { Array(rows) { Complex() } }
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                transposedOutput[j][i] = output[i][j]
            }
        }

        // Transform each column
        for (j in 0 until cols) {
            val column = Array(rows) { i -> transposedOutput[j][i] }
            val transformedCol = recursiveFFT(column)
            for (i in 0 until rows) {
                output[i][j] = transformedCol[i]
            }
        }

        // Divide by the total number of elements to complete the inverse transformation
        val totalElements = rows * cols
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                output[i][j] = output[i][j] / totalElements.toDouble()
            }
        }

        return output
    }

    fun <D> place(
        width: Double,
        height: Double,
        vertices: Collection<VertexViewModel<D>>,
        gridSize: Int = 128,
        longRangeAttractionConstant: Double,
        nearAttractionConstant: Double,
        repulsiveConstant: Double
    ) {
        val forces = Array(vertices.size) { Pair(0.0, 0.0) }
        val grid = Array(gridSize) { Array(gridSize) { Complex(0.0, 0.0) } }
        val deltaX = width / gridSize
        val deltaY = height / gridSize

        // Assign particles to the grid
        vertices.forEach { vertex ->
            val i = (vertex.x.value / deltaX.dp).toInt().coerceIn(0, gridSize - 1)
            val j = (vertex.y.value / deltaY.dp).toInt().coerceIn(0, gridSize - 1)
            grid[i][j] = grid[i][j].plus(Complex(1.0, 0.0)) // Add particle mass
        }

        // Compute potential using FFT
        val potential = fft2D(grid)
        // Apply the Green's function in frequency domain
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val kx = if (i <= gridSize / 2) i else i - gridSize
                val ky = if (j <= gridSize / 2) j else j - gridSize
                val kSquared = (kx * kx + ky * ky).toDouble()
                if (kSquared != 0.0) {
                    potential[i][j] = potential[i][j].div(kSquared)
                }
            }
        }
        val potentialRealSpace = ifft2D(potential)

        // Compute forces from potential
        val forceX = Array(gridSize) { Array(gridSize) { 0.0 } }
        val forceY = Array(gridSize) { Array(gridSize) { 0.0 } }
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val right = potentialRealSpace[(i + 1) % gridSize][j].r - potentialRealSpace[i][j].r
                val up = potentialRealSpace[i][(j + 1) % gridSize].r - potentialRealSpace[i][j].r

                val distance = sqrt(deltaX * deltaX + deltaY * deltaY)

                val repulsion = (distance) / (1 + distance * distance).pow(repulsiveConstant)

                forceX[i][j] -= right / deltaX / distance * repulsion
                forceY[i][j] -= up / deltaY / distance * repulsion

                val attraction =
                    longRangeAttractionConstant *
                        (distance + ((nearAttractionConstant * distance) / (1 + distance * distance)))

                forceX[i][j] -= right / deltaX / distance * attraction
                forceY[i][j] -= up / deltaY / distance * attraction
            }
        }

        // Interpolate forces back to vertices
        vertices.forEachIndexed { index, vertex ->
            val i = (vertex.x.value / deltaX.dp).toInt().coerceIn(0, gridSize - 1)
            val j = (vertex.y.value / deltaY.dp).toInt().coerceIn(0, gridSize - 1)
            val fx = forceX[i][j]
            val fy = forceY[i][j]
            forces[index] = Pair(fx, fy)
        }

        // Update positions
        vertices.forEachIndexed { index, vertex ->
            vertex.x.value += forces[index].first.dp
            vertex.y.value += forces[index].second.dp
        }
    }

    fun <D> randomize(width: Double, height: Double, vertices: Collection<VertexViewModel<D>>) {
        vertices.forEach { vertex ->
            val randomX = Random.nextDouble(0.0, width * 1.5 - 360.0 - vertex.radius.value * 2).toFloat().dp
            val randomY = Random.nextDouble(0.0, height * 1.5 - vertex.radius.value * 2).toFloat().dp

            vertex.x.value = randomX
            vertex.y.value = randomY
        }
    }
}
