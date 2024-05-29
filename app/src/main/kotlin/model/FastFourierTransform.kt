package model

/**
 *  Copyright (C) 2021 José Alexandre Nalon
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
 */

/**************************************************************************************************
 * Fast Fourier Transform -- Kotlin Version
 * This version implements Cooley-Tukey algorithm for composite-length sequences.
 *
 * José Alexandre Nalon
 **************************************************************************************************
 * This program can be compiled by issuing the command:
 *
 * $ kotlinc anyfft.kt
 *
 * It will generate a file named 'FftKt.class' in the same directory. It can be run by issuing the
 * command:
 *
 * $ kotlin AnyfftKt
 *
 **************************************************************************************************/

/**************************************************************************************************
 * Include necessary libraries:
 **************************************************************************************************/
import kotlin.math.*                   // Math functions;


/**************************************************************************************************
 * Mini-library to deal with complex numbers.
 **************************************************************************************************/
class Complex(val r: Double, val i: Double) {

    // Constructor:
    constructor() : this(0.0, 0.0) {}

    // Add the argument to this, giving the result as a new complex number:
    operator fun plus(c: Complex): Complex {
        return Complex(r + c.r, i + c.i)
    }

    // Subtract the argument from this, giving the result as a new complex number:
    operator fun minus(c: Complex): Complex {
        return Complex(r - c.r, i - c.i)
    }

    // Multiply the argument with this, giving the result as a new complex number:
    operator fun times(c: Complex): Complex {
        return Complex(r * c.r - i * c.i, r * c.i + i * c.r)
    }

    // Multiply with an scalar, giving the reulst as a new complex number:
    operator fun times(a: Double): Complex {
        return Complex(a * r, a * i)
    }

    // Divide this by the argument, giving the result as a new complex number:
    operator fun div(a: Double): Complex {
        return Complex(r / a, i / a)
    }

}

// Complex exponential of an angle:
fun Cexp(a: Double): Complex {
    return Complex(cos(a), sin(a))
}


/**************************************************************************************************
 * Auxiliary Function: complexShow
 *   Pretty printing of an array of complex numbers, used to inspect results.
 *
 * Parameters:
 *   x
 *     A vector of complex numbers, according to the definition above;
 **************************************************************************************************/
fun complexShow(x: Array<Complex>) {
    for (i in 0..x.size - 1)
        println("( " + x[i].r + ", " + x[i].i + ")")
}


/**************************************************************************************************
 * Auxiliary Function: timeIt
 *   Measure execution time through repeated calls to a (Fast) Fourier Transform function.
 *
 * Parameters:
 *  f
 *    Function to be called, with the given prototype. The first complex vector is the input
 *    vector, the second complex vector is the result of the computation, and the integer is the
 *    number of elements in the vector;
 *  size
 *    Number of elements in the vector on which the transform will be applied;
 *  repeat
 *    Number of times the function will be called.
 *
 * Returns:
 *   The average execution time for that function with a vector of the given size.
 **************************************************************************************************/
fun timeIt(f: (x: Array<Complex>) -> Array<Complex>, size: Int, repeat: Int): Double {
    val x = Array<Complex>(size) { i -> Complex(i.toDouble(), 0.0) }
    val start = System.currentTimeMillis()
    for (j in 1..repeat) {
        f(x)
    }
    return (System.currentTimeMillis() - start).toDouble() / (1000 * repeat).toDouble()
}


/**************************************************************************************************
 * Function: directFT
 *   Discrete Fourier Transform directly from the definition, an algorithm that has O(N^2)
 *   complexity.
 *
 * Parameters:
 *   x
 *     The vector of which the DFT will be computed. Given the nature of the implementation, there
 *     is no restriction on the size of the vector, although it will almost always be called with a
 *     power of two size to give a fair comparison;
 *
 * Returns:
 *   A complex-number vector of the same size, with the coefficients of the DFT.
 **************************************************************************************************/
fun directFT(x: Array<Complex>): Array<Complex> {
    // Implement the direct DFT computation
    // This is usually O(N^2) but can be acceptable for small N
    val N = x.size
    val X = Array(N) { Complex(0.0, 0.0) }
    for (k in 0 until N) {
        for (n in 0 until N) {
            val theta = -2.0 * PI * k * n / N
            X[k] = X[k] + x[n] * Cexp(theta)
        }
    }
    return X
}

/**************************************************************************************************
 * Function: factor
 *   Smallest prime factor of a given number. If the argument is prime itself, then it is the
 *   return value.
 *
 * Parameters:
 *   n
 *     Number to be inspected.
 *
 * Returns:
 *   The smallest prime factor, or the number itself if it is already a prime.
 **************************************************************************************************/
fun factor(n: Int): Int {
    val rn = ceil(sqrt(n.toDouble())).toInt()      // Search up to the square root of the number;
    for (i in 2..rn) {
        if (n % i == 0) return i                     // If remainder is zero, a factor is found;
    }
    return n
}


/**************************************************************************************************
 * Function: recursiveFFT
 *   Fast Fourier Transform using a recursive decimation in time algorithm. This has smaller
 *   complexity than the direct FT, though the exact value is difficult to compute.
 *
 * Parameters:
 *   x
 *     The vector of which the FFT will be computed. Its length must be a composite number, or else
 *     the computation will be defered to the direct FT, and there will be no efficiency gain.
 *
 *  Returns:
 *   A complex-number vector of the same size, with the coefficients of the DFT.
 **************************************************************************************************/
fun recursiveFFT(x: Array<Complex>): Array<Complex> {
    val N = x.size

    val N1 = factor(N)  // Smallest prime factor of length
    if (N == N1) {
        return directFT(x)  // Direct transform if length is prime
    } else {
        val N2 = N / N1  // Decompose in two factors, N1 being prime

        val X = Array(N) { Complex(0.0, 0.0) }  // Allocate memory for computation

        val W = Cexp(-2 * PI / N.toDouble())  // Twiddle factor
        var Wj = Complex(1.0, 0.0)
        for (j in 0 until N1) {  // Compute subsequences of size N2
            val xj = Array(N2) { n -> x[n * N1 + j] }  // Create the subsequence
            val Xj = recursiveFFT(xj)  // Compute the DFT of the subsequence
            var Wkj = Complex(1.0, 0.0)
            for (k in 0 until N) {
                X[k] = X[k] + Xj[k % N2] * Wkj  // Recombine results
                Wkj = Wkj * Wj  // Update twiddle factors
            }
            Wj = Wj * W
        }
        return X
    }
}


///**************************************************************************************************
// * Main function:
// **************************************************************************************************/
//fun main() {
//
//    val sizes = listOf(2*3, 2*2*3, 2*3*3, 2*3*5, 2*2*3*3, 2*2*5*5, 2*3*5*7, 2*2*3*3*5*5)
//    val repeat: Int = 500                      // Number of executions to compute average time;
//
//    // Start by printing the table with time comparisons:
//    println("+---------+---------+---------+---------+")
//    println("|    N    |   N^2   | Direct  | Recurs. |")
//    println("+---------+---------+---------+---------+")
//
//    // Try it with vectors with the given sizes:
//    for (n in sizes) {
//
//        // Compute the average execution time:
//        var dtime = timeIt(::directFT, n, repeat)
//        var rtime = timeIt(::recursiveFFT, n, repeat)
//
//        // Print the results:
//        val results = "| %7d | %7d | %7.4f | %7.4f |".format(n, n*n, dtime, rtime)
//        println(results);
//    }
//
//    println("+---------+---------+---------+---------+")
//
//}