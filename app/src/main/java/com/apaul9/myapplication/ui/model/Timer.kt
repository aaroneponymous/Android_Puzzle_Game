package com.apaul9.myapplication.ui.model
import android.os.CountDownTimer


class Timer(private val totalTime: Long, val onTimerTick: (Long) -> Unit, val onTimerFinish: () -> Unit) {
    private var timeLeft = totalTime
    private var timer: CountDownTimer? = null

    fun start() {
        stop()
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                onTimerTick(timeLeft)
            }

            override fun onFinish() {
                onTimerFinish()
            }
        }.start()
    }

    fun stop() {
        timer?.cancel()
    }

    fun getTimeRemaining(): Pair<Long, Long> {
        val minutes = timeLeft / 1000 / 60
        val seconds = timeLeft / 1000 % 60
        return Pair(minutes, seconds)
    }
}





