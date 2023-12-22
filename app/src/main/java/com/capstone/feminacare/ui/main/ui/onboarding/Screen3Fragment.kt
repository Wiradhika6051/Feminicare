package com.capstone.feminacare.ui.main.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.FragmentScreen3Binding

class Screen3Fragment : Fragment() {
    private lateinit var binding: FragmentScreen3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreen3Binding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerFragment2)
        binding.button5.setOnClickListener {
            println("ToLOGIN")
            findNavController().navigate(R.id.action_viewPagerFragment2_to_loginActivity)
            onBoardingFinished()
        }

        return binding.root
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}