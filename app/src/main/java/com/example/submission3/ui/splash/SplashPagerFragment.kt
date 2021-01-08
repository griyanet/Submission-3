package com.example.submission3.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.submission3.databinding.FragmentSplashPagerBinding
import com.example.submission3.ui.splash.screens.screen1
import com.example.submission3.ui.splash.screens.screen2
import com.example.submission3.ui.splash.screens.screen3
import com.google.android.material.tabs.TabLayoutMediator

class SplashPagerFragment : Fragment() {
    private var _binding: FragmentSplashPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashPagerBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            screen1(),
            screen2(),
            screen3()
        )

        val adapter = SplashAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )



        binding.splashPager.adapter = adapter

        return binding.root
    }


}