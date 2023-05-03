package com.idutvuk.go_maf.ui

import android.os.CountDownTimer
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView


object TimerHandler {
    private lateinit var timer: CountDownTimer
    private var remainingTime: Long = 0
    private var maxTime: Long = 60000L
    var isRunning = false

    /**
     * Starts the timer if it is **not running** right now
     */
    fun startTimer(tvTimer: TextView, pbTimer: ProgressBar, time: Long) {
        if (isRunning) return
        isRunning = true
        Log.i("GraphLog","Timer started")
        timer = object : CountDownTimer(time, 1000) { //TODO: increase countDown interval
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished

                // Calculate minutes and seconds from remaining time
                val minutes = remainingTime / 60000
                val seconds = (remainingTime % 60000) / 1000

                // Format the time into a string
                val timeText = String.format("%d:%02d", minutes, seconds)

                // Update the text of the TextView
                tvTimer.text = timeText

                // Calculate progress as a percentage of total time
//                pbTimer.visibility = ProgressBar.VISIBLE
                pbTimer.progress = (remainingTime/1000).toInt()

                // Your code for updating the UI with the remaining time
                if (remainingTime == 30000L || remainingTime == 10000L || remainingTime == 0L) {
                    Log.v("GameLog","${remainingTime/1000} secs remaining")
                }
            }

            override fun onFinish() {
                isRunning = false
                pbTimer.progress = 0
                Log.i("GraphLog", "time out")
            }
        }.start()
    }

    /**
     * Pauses the timer if it is running right now
     */
    fun pauseTimer() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }

    /**
     * Pauses the timer if it is running right now
     */
    fun resumeTimer(tvTimer: TextView, pbTimer: ProgressBar) {
        if (!isRunning) {
            startTimer(tvTimer, pbTimer, remainingTime)
            isRunning = true
        }
    }

    fun addTime(tvTimer: TextView, pbTimer: ProgressBar, timeToAdd: Long)  {
        if (isRunning) {
            timer.cancel()
            remainingTime += timeToAdd
            isRunning = false
            startTimer(tvTimer, pbTimer, remainingTime)
        } else {
            remainingTime += timeToAdd
            startTimer(tvTimer, pbTimer, remainingTime)
            isRunning = true
            pauseTimer()
        }
    }

    fun skipTimer(tvTimer: TextView, pbTimer: ProgressBar) {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
        remainingTime = 0
        pbTimer.progress = 0
        tvTimer.text = "0:00"
        // Your code for resetting the UI with the new remaining time
    }
}