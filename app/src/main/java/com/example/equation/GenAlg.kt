package com.example.equation
import kotlin.random.Random
import kotlin.math.abs

fun result(coefs: List<Int>, xs: List<Int>) = coefs.zip(xs) {a, x -> a * x}.sum()

fun delta(expected: Int, actual: Int) = abs(expected - actual)

fun possibility(curDelta: Int, sumDelta: Double): Double = (1.0 / curDelta.toDouble()) / sumDelta

fun crossover(d1: List<Int>, d2: List<Int>, threshold: Int): List<Int> =
    d1.mapIndexed{ index, it -> if (index <= threshold) it else d2[index] }

fun invertedSum(data: List<Int>): Double = data.map{ 1.0 / it.toDouble() }.sum()

// return index of the first element which returns true on given predicate or index of the last element
fun<T> firstTrue(d: List<T>, p: (T) -> Boolean): Int {
    for (i in d.indices) {
        if (p(d[i])) return i
    }
    return d.size - 1
}

// returns new List with mutation by offset on the given position
fun mutate(d: List<Int>, pos: Int, offset: Int) =
    d.mapIndexed{ index, it -> if (index == pos) it + offset else it }.toList()

fun solve(answer: Int, coefs: List<Int>, mutationChance: Double, populationSize: Int, maxIterations: Int): Pair<List<Int>, Int> {
    var data = Array(populationSize) {listOf(0, 0, 0, 0)}
    for (i in 0 until populationSize) {
        data[i] = listOf(Random.nextInt(0, answer / 2),
            Random.nextInt(0, answer / 2),
            Random.nextInt(0, answer / 2),
            Random.nextInt(0, answer / 2))
    }
    var answers: List<Int> = listOf(0, 0, 0, 0)
    var iterations = 0
    while (true) {
        if (iterations++ > maxIterations)
            break
        val results = data.map{ result(coefs, it) }
        val deltas = results.map{ delta(answer, it) }
        var exit = false
        deltas.forEachIndexed{ index, it ->
            if (it == 0) {
                answers = data[index]
                exit = true
            }
        }
        if (exit) break
        val possibilities = deltas.map{ possibility(it, invertedSum(deltas)) }
        val sectors = possibilities.mapIndexed{ index, _ -> possibilities.take(index).sum() }
        val dataCopy = data
        for (i in 0 until populationSize) {
            val fatherChance = Random.nextDouble()
            val motherChance = Random.nextDouble()
            val fatherIndex = firstTrue(sectors) { x -> x >= fatherChance } - 1
            val motherIndex = firstTrue(sectors) { x -> x >= motherChance } - 1
            val father = dataCopy[fatherIndex]
            val mother = dataCopy[motherIndex]
            val threshold = Random.nextInt(0, populationSize - 1)
            val child = crossover(father, mother, threshold)
            if (Random.nextDouble() <= mutationChance) {
                val mutatedChild = mutate(child, Random.nextInt(0, populationSize - 1), Random.nextInt(-1, 1))
                data[i] = mutatedChild
            } else {
                data[i] = child
            }
        }
    }
    return answers to iterations
}

fun main() {
    val answer = 15
    val coefs = listOf(1, 2, 3, 4)
    val populationSize = 20
    val maxIterations = 1000
    val mutationChance = 0.1
    println(solve(answer, coefs, mutationChance, populationSize, maxIterations))
}
