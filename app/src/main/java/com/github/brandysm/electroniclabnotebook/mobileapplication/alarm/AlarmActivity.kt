package com.github.brandysm.electroniclabnotebook.mobileapplication.alarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityAlarmBinding
import kotlin.properties.Delegates

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    var hourOfDaySet = 0
    var minuteSet = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val saveButton: Button = binding.buttonSave
        val exitButton: Button = binding.buttonCancel
        val timePicker: TimePicker = binding.timePicker

        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            hourOfDaySet = hourOfDay
            minuteSet = minute
        }
        
        exitButton.setOnClickListener {
            finish()
        }
        
        saveButton.setOnClickListener {
            createAlarm("Alarm!", hourOfDaySet, minuteSet)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    fun createAlarm(message: String, hour: Int, minutes: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        }
        startActivity(intent)
    }

}