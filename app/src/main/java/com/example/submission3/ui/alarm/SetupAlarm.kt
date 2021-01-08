package com.example.submission3.ui.alarm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.submission3.alarm.AlarmActivity
import com.example.submission3.alarm.AlarmReceiver
import com.example.submission3.databinding.FragmentSetupAlarmBinding

class SetupAlarm : Fragment() {

    private var _binding: FragmentSetupAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        const val ALARM_PREF = "AlarmPreference"
        const val IS_ENABLED = "isChecked"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupAlarmBinding.inflate(inflater, container, false)

        binding.toggleAlarm.isChecked = getToggle()
        binding.toggleAlarm.isClickable
        binding.toggleAlarm.setOnCheckedChangeListener() { _, isChecked ->
            setAlarm(isChecked)
        }

        binding.btnSetAlarm.setOnClickListener {

        }

        return binding.root
    }

    private fun setAlarm(isChecked: Boolean) {
        if (isChecked) {
            val title = "My Github App"
            val message = "This alarm set at 9am"
            alarmReceiver = AlarmReceiver()
            context?.let { alarmReceiver.setOneTimeAlarm(it, title, message) }
            Toast.makeText(
                context,
                "One time alarm at 09.00 AM has been set up!",
                Toast.LENGTH_SHORT
            ).show()
            toggleOn()
        } else {
            alarmReceiver = AlarmReceiver()
            context?.let { alarmReceiver.cancelAlarm(it) }
            Toast.makeText(
                context,
                "One time alarm at 09.00 AM has been cancelled!",
                Toast.LENGTH_SHORT
            ).show()
            toggleOff()
        }
    }

    private fun toggleOn() {
        val sharedPref =
            requireActivity().getSharedPreferences(AlarmActivity.ALARM_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(AlarmActivity.IS_ENABLED, true)
        editor.apply()
    }

    private fun toggleOff() {
        val sharedPref =
            requireActivity().getSharedPreferences(AlarmActivity.ALARM_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(AlarmActivity.IS_ENABLED, false)
        editor.apply()
    }

    private fun getToggle(): Boolean {
        val sharedPref =
            requireActivity().getSharedPreferences(AlarmActivity.ALARM_PREF, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(AlarmActivity.IS_ENABLED, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}