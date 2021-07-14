package com.fastcampus.alarmapp

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.alarmapp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initChangeAlarmTimeButton()
    }

    private fun initChangeAlarmTimeButton() {
        binding.btnChangeAlarmTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()

            TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    val model = saveAlarmModel(hourOfDay, minute, false)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun saveAlarmModel(
        hourOfDay: Int,
        minute: Int,
        onOff: Boolean
    ): AlarmDisplayModel {
        val model = AlarmDisplayModel(hour = hourOfDay, minute = minute, onOff = false)

        val pref = getSharedPreferences(SHARED_PREFERENCE_TIME, Context.MODE_PRIVATE)

        with(pref.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }

    companion object {

        private const val SHARED_PREFERENCE_TIME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "ofOff"

        private const val ALARM_REQUEST_CODE = 1000
    }
}