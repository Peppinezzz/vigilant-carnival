package com.example.equation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickSolve(view: View) {
        if (coefANumber.text.isEmpty() || coefBNumber.text.isEmpty() || coefCNumber.text.isEmpty() || coefDNumber.text.isEmpty())
            return
        if (answerNumber.text.isEmpty()) return
        if (populationNumber.text.isEmpty() || mutationNumber.text.isEmpty() || iterationNumber.text.isEmpty())
            return
        val mutationChance = mutationNumber.text.toString().toDouble()
        if (mutationChance < 0 || mutationChance > 1)
            return

        val answer = answerNumber.text.toString().toInt()
        val coefs = listOf(
            coefANumber.text.toString().toInt(),
            coefBNumber.text.toString().toInt(),
            coefCNumber.text.toString().toInt(),
            coefDNumber.text.toString().toInt()
        )
        val populationSize = populationNumber.text.toString().toInt()
        val maxIterations = iterationNumber.text.toString().toInt()

        val (results, iterations) = solve(answer, coefs, mutationChance, populationSize, maxIterations)

        if (iterations >= maxIterations) {
            resultView.text = "Results have not been found"
        } else {
            resultView.text = results.toString()
        }
        resultIterationsView.text = iterations.toString()
    }
}