package com.capstone.feminacare.ui.main.ui.checkuphistory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.FragmentCheckupHistoryBinding
import com.capstone.feminacare.databinding.FragmentPeriodDateBinding

class CheckupHistoryFragment : Fragment() {


    private var _binding: FragmentCheckupHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckupHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}