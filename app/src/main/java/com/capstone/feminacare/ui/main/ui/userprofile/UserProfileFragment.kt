package com.capstone.feminacare.ui.main.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {


//    private lateinit var viewModel: UserProfileViewModel
    private var binding : FragmentUserProfileBinding? = null
    private val _binding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

}