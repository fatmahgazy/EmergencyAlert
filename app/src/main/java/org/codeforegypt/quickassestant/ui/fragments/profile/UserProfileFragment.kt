package org.codeforegypt.quickassestant.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentUserProfileBinding

@AndroidEntryPoint
class UserProfileFragment : Fragment() {


    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    val userProfileViewModel: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentUserProfileBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivReturn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_editProfileFragment)
        }

        observeUserData()

    }

    override fun onResume() {
        super.onResume()
        userProfileViewModel.getUserProfile()
    }

    private fun observeUserData(){
        viewLifecycleOwner.lifecycleScope.launch {
            userProfileViewModel.userData.collectLatest { user ->

                if(user != null) {
                    Glide.with(requireContext())
                        .load(user.email)
                        .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.user_profile_placeholder))
                        .error(ContextCompat.getDrawable(requireContext(), R.drawable.user_profile_placeholder))
                        .into(binding.ivUser)

                    binding.tvUserFullName.text = user.fullName
                    binding.tvUserPhoneNumber.text = user.phone
                    binding.tvUserEmail.text = user.email
                    binding.tvUserGender.text = user.gender
                    binding.tvUserAge.text = user.age
                    binding.tvUserBloodType.text = user.bloodType
                }

            }
        }

    }

}