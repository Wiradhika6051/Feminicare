package com.capstone.feminacare.ui.main.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.FragmentScreen2Binding


class Screen2Fragment : Fragment() {
    private lateinit var binding: FragmentScreen2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreen2Binding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerFragment2)
        binding.button3.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }


}