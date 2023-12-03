package com.capstone.feminacare.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.FragmentHomeBinding
import com.capstone.feminacare.ui.chatbot.ChatBotActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.timeOfDay.observe(viewLifecycleOwner) {
            val greet = getString(R.string.greetings, it)
            binding.tvGreetingTime.text = greet
        }

        binding.fabChatbot.setOnClickListener {
            startActivity(Intent(context, ChatBotActivity::class.java))
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}