package com.example.submission3.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.submission3.R
import com.example.submission3.databinding.FragmentSplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            if (onBoardingDone()) {
                findNavController().navigate(R.id.action_splashFragment_to_splashPagerFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_navigation_home)
            }
        }

        return binding.root
    }

    private fun onBoardingDone(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", true)
    }

}