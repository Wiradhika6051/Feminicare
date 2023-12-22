package com.capstone.feminacare.ui.main.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.FragmentScreen1Binding


class Screen1Fragment : Fragment() {

    private  var _binding: FragmentScreen1Binding? = null
    private val binding get() = _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreen1Binding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerFragment2)
        binding.buttonNext.setOnClickListener {
            Log.d("Clicked1", "dasdasd")
            viewPager?.currentItem = 2
        }

        return binding.root
    }


}