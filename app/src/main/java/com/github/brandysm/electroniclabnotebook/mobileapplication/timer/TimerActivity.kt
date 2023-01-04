package com.github.brandysm.electroniclabnotebook.mobileapplication.timer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityTimerBinding
import java.util.*


class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding

    var timerStarted: Boolean = false

    var time: Double = 0.0
    var timer: Timer = Timer()
    lateinit var timerTask: TimerTask
    lateinit var timerValueText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startButton: Button = binding.buttonStart
        val exitButton: Button = binding.buttonCancel
        val resetButton: Button = binding.buttonReset
        timerValueText = binding.textTimerValue

        exitButton.setOnClickListener {
            finish()
        }

        startButton.setOnClickListener {
            if (timerStarted == false) {
                timerStarted = true
                startButton.text = "Stop"
                startButton.setBackgroundColor(resources.getColor(R.color.red))
                startTimer()
            } else {
                timerStarted = false
                startButton.text = "Start"
                startButton.setBackgroundColor(resources.getColor(R.color.green))

                timerTask.cancel()
            }
        }

        resetButton.setOnClickListener {
            if (this::timerTask.isInitialized) {
                timerStarted = false
                startButton.text = "Start"
                startButton.setBackgroundColor(resources.getColor(R.color.green))
                time = 0.0
                timerTask.cancel()
                timerValueText.setText("00:00:00")
            }
        }
    }

    private fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    time++
                    timerValueText.setText(getTimerText())
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 1000, 1000)
    }

    private fun getTimerText(): String {
        val rounded = Math.round(time)
        val seconds = ((rounded %86400) %3600) %60
        val minutes = ((rounded %86400) %3600) /60
        val hours = ((rounded %86400) /3600)
        return String.format("%02d",hours) + ":" + String.format("%02d",minutes) + ":" + String.format("%02d",seconds)
    }
}