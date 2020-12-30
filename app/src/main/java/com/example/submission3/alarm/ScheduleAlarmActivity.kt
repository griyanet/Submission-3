package com.example.submission3.alarm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.submission3.R
import com.example.submission3.databinding.ActivityScheduleAlarmBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAlarmActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    }

    private var binding: ActivityScheduleAlarmBinding? = null
    private lateinit var alarmReceiver: ScheduleAlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnOnceDate?.setOnClickListener(this)
        binding?.btnOnceTime?.setOnClickListener(this)
        binding?.btnSetOnceAlarm?.setOnClickListener(this)

        alarmReceiver = ScheduleAlarmReceiver()
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_once_date -> {
                    val datePickerFragment = DatePickerFragment()
                    datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
                }
                R.id.btn_once_time -> {
                    val timePickerFragmentOnce = TimePickerFragment()
                    timePickerFragmentOnce.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
                }
                R.id.btn_set_once_alarm -> {
                    val onceDate = binding?.tvOnceDate?.text.toString()
                    val onceTime = binding?.tvOnceTime?.text.toString()
                    val onceMessage = binding?.edtOnceMessage?.text.toString()

                    alarmReceiver.setOneTimeAlarm(
                        this, ScheduleAlarmReceiver.TYPE_ALARM,
                        onceDate,
                        onceTime,
                        onceMessage
                    )
                }
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        binding?.tvOnceDate?.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding?.tvOnceTime?.text = dateFormat.format(calendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}