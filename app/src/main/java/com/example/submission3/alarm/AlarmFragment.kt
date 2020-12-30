package com.example.submission3.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.submission3.R
import com.example.submission3.databinding.FragmentAlarmBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment(), View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    }

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        binding.btnOnceDate.setOnClickListener(this)
        binding.btnOnceTime.setOnClickListener(this)
        binding.btnSetOnceAlarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()

        return binding.root
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_once_date -> {
                    val datePickerFragment = DatePickerFragment()
                    datePickerFragment.show(parentFragmentManager, DATE_PICKER_TAG)
                }
                R.id.btn_once_time -> {
                    val timePickerFragmentOnce = TimePickerFragment()
                    timePickerFragmentOnce.show(parentFragmentManager, TIME_PICKER_ONCE_TAG)
                }
                R.id.btn_set_once_alarm -> {
                    val onceDate = binding.tvOnceDate.text.toString()
                    val onceTime = binding.tvOnceTime.text.toString()
                    val onceMessage = binding.edtOnceMessage.text.toString()

                    context?.let {
                        alarmReceiver.setOneTimeAlarm(
                            it, AlarmReceiver.TYPE_ALARM,
                            onceDate,
                            onceTime,
                            onceMessage
                        )
                    }
                }
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        binding.tvOnceDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.tvOnceTime.text = dateFormat.format(calendar.time)
    }
}