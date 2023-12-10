package com.capstone.feminacare.ui.main.ui.checkuphistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.feminacare.databinding.FragmentCheckupHistoryBinding
import com.capstone.feminacare.ui.CheckupViewModelFactory

class CheckupHistoryFragment : Fragment() {


    private var _binding: FragmentCheckupHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CheckupHistoryViewModel> {
        CheckupViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckupHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recycler = binding.rvCheckupHistory
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CheckupHistoryAdapter()
        recycler.adapter = adapter

        viewModel.checkupHistory.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}