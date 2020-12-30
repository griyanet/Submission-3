package com.example.submission3.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.submission3.R
import com.example.submission3.databinding.ActivityAlarmBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity() {

    private var binding: ActivityAlarmBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        alarmReceiver = AlarmReceiver()
        val message = "This alarm set at 9am"
        binding?.btnSetOnceAlarm?.setOnClickListener {
            alarmReceiver.setOneTimeAlarm(
                this, AlarmReceiver.TYPE_ALARM, message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}