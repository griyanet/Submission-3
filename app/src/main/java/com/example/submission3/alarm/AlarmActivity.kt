package com.example.submission3.alarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.submission3.databinding.ActivityAlarmBinding

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
                this, AlarmReceiver.TYPE_ALARM, message
            )
        }
        binding?.btnSetAlarm?.setOnClickListener {
            val intent = Intent(this, ScheduleAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}