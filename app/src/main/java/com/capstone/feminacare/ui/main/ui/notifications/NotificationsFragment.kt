package com.capstone.feminacare.ui.main.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.feminacare.databinding.FragmentNotificationsBinding
import com.capstone.feminacare.data.Result
import com.capstone.feminacare.ui.ViewModelFactory
import com.capstone.feminacare.ui.main.ui.profile.ProfileActivity

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private val viewModel by viewModels<NotificationsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.editButton.setOnClickListener {
//            val intent = Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
//        }

        viewModel.profileResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        Log.d("NotificationsFragment", "Username: ${result.data.data?.username}")
                        binding.apply {
                            profileFirstName.text = result.data.data?.firstName
                            profileName.text = result.data.data?.lastName
                            profileEmail.text = result.data.data?.email
                            profileUsername.text = result.data.data?.username
                            progressBar.visibility = View.GONE
                        }
                    }
                    is Result.Error -> {

                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}
