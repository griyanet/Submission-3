package com.example.submission3.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.submission3.R
import com.example.submission3.databinding.ActivityAlarmBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    }

    private var binding: ActivityAlarmBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnOnceDate?.setOnClickListener(this)
        binding?.btnOnceTime?.setOnClickListener(this)
        binding?.btnSetOnceAlarm?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
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
                        this, AlarmReceiver.TYPE_ALARM,
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