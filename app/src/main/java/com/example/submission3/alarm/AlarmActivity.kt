package com.example.submission3.alarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.submission3.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {

    private var _binding: ActivityAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        const val ALARM_PREF = "AlarmPreference"
        const val IS_ENABLED = "isChecked"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAlarmBinding.inflate(layoutInflater)

        binding.toggleAlarm.isChecked = getToggle()
        binding.toggleAlarm.isClickable
        binding.toggleAlarm.setOnCheckedChangeListener() { _, isChecked ->
            setAlarm(isChecked)
        }

        binding.btnSetAlarm.setOnClickListener {
            val intent = Intent(this, ScheduleAlarmActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun setAlarm(isChecked: Boolean) {
        if (isChecked) {
            val title = "My Github App"
            val message = "This alarm set at 9am"
            alarmReceiver = AlarmReceiver()
            alarmReceiver.setOneTimeAlarm(applicationContext, title, message)
            Toast.makeText(
                applicationContext,
                "One time alarm at 09.00 AM has been set up!",
                Toast.LENGTH_SHORT
            ).show()
            toggleOn()
        } else {
            alarmReceiver = AlarmReceiver()
            alarmReceiver.cancelAlarm(applicationContext)
            Toast.makeText(
                applicationContext,
                "One time alarm at 09.00 AM has been cancelled!",
                Toast.LENGTH_SHORT
            ).show()
            toggleOff()
        }
    }

    private fun toggleOn() {
        val sharedPref = applicationContext.getSharedPreferences(ALARM_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(IS_ENABLED, true)
        editor.apply()
    }

    private fun toggleOff() {
        val sharedPref = applicationContext.getSharedPreferences(ALARM_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(IS_ENABLED, false)
        editor.apply()
    }

    private fun getToggle(): Boolean {
        val sharedPref = applicationContext.getSharedPreferences(ALARM_PREF, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(IS_ENABLED, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}