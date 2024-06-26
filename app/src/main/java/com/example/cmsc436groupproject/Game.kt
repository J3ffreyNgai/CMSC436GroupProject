package com.example.cmsc436groupproject

import android.util.Log
import android.widget.Toast
import kotlin.random.Random

class Game(private var level: Int) {
    private var currScore: Float = 0.0F
    private var answer: Long = 0
    private var remainingGuesses: Int = level + 1
    private var isGameOver: Boolean = false
    private var wrongAnswer : MutableSet<Int>
    init {
        genAnswer()
        wrongAnswer = mutableSetOf()
    }

    fun setAnswer(answer: Long) {
        this.answer = answer
    }

    fun checkCorrectDigits(guess: List<Int>): List<String> {
        val answerDigits = answer.toString().toCharArray().map { it.toString().toInt() }

        val statusList = mutableListOf<String>()

        val answerMap = mutableMapOf<Int, Int>()
        val guessMap = mutableMapOf<Int, Int>()

        for (digit in answerDigits) {
            answerMap[digit] = answerMap.getOrDefault(digit, 0) + 1
        }

        for ((index, digit) in guess.withIndex()) {
            if (digit == answerDigits[index]) {
                statusList.add("o")

                guessMap[digit] = guessMap.getOrDefault(digit, 0) + 1
            } else {
                statusList.add("x")
                wrongAnswer.add(digit)
            }
        }

        for ((index, digit) in guess.withIndex()) {
            if (statusList[index] == "x" && digit in answerDigits) {
                if (guessMap.getOrDefault(digit, 0) < answerMap.getOrDefault(digit, 0)) {
                    statusList[index] = "-"
                    guessMap[digit] = guessMap.getOrDefault(digit, 0) + 1
                }
            }
        }
        for (digit in wrongAnswer.toList()) {
            if (digit in answerDigits) {
                Log.d("Game", wrongAnswer.toString())
                wrongAnswer.remove(digit)
                Log.d("Game", wrongAnswer.toString())
            }
        }

        remainingGuesses--
        if (remainingGuesses <= 0 && (guess.joinToString("").toLong() != answer)) {
            isGameOver = true
        }

        return statusList
    }


    fun newStage() {
        level++
        remainingGuesses = level + 1
        wrongAnswer = mutableSetOf()
        genAnswer()
    }

    private fun genAnswer() {
        val firstDigit = Random.nextInt(1, 10)
        val remainingDigits = (1 until level).map { Random.nextInt(0, 10) }.joinToString("")
        val randomNumberAsString = "$firstDigit$remainingDigits"
        answer = randomNumberAsString.toLong()
        Log.w("Game", "Generated answer: $answer")
    }

    fun getScore(): Float {
        return currScore
    }

    fun getAnswer(): Long {
        return answer
    }

    fun isGameOver(): Boolean {
        return isGameOver
    }

    fun getWrongAnswers(): MutableSet<Int> {
        return wrongAnswer
    }

}