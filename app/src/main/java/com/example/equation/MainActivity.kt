package com.example.equation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickSolve(view: View) {
        if (populationNumber.text.isEmpty() || mutationNumber.text.isEmpty() || iterationNumber.text.isEmpty())
            return
        val mutationChance = mutationNumber.text.toString().toDouble()
        if (mutationChance < 0 || mutationChance > 1)
            return

        val populationSize = populationNumber.text.toString().toInt()
        val maxIterations = iterationNumber.text.toString().toInt()

        var minTime: Long = 0
        var maxTime: Long = 0
        for (i in 1..100) {
            val answer = Random.nextInt(10, 50)
            val coefs = listOf(
                Random.nextInt(1, 10),
                Random.nextInt(1, 10),
                Random.nextInt(1, 10),
                Random.nextInt(1, 10)
            )
            val before = System.currentTimeMillis()
            solve(answer, coefs, mutationChance, populationSize, maxIterations)
            val after = System.currentTimeMillis()
            val elapsed = after - before
            if (elapsed > maxTime) {
                maxTime = elapsed
            }
            if (elapsed < minTime) {
                minTime = elapsed
            }
            val message = "The iteration " + i + " took " + elapsed + "ms."
            Log.d("userspace", message)
        }
        val result = "The longest: " + maxTime + "ms. The shortest: " + minTime + "ms."
        resultIterationsView.text = result
    }
}
