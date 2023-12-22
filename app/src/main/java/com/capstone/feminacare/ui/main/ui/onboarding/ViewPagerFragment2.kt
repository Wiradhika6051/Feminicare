package com.jovanovic.stefan.mytestapp.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.feminacare.ui.main.ui.onboarding.Screen1Fragment
import com.capstone.feminacare.ui.main.ui.onboarding.Screen2Fragment

class ViewPagerFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//            val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            Screen1Fragment(),
            Screen2Fragment(),
            Screen2Fragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

//        view.viewPager.adapter = adapter

        return view
    }

}