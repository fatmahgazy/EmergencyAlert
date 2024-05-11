package org.codeforegypt.quickassestant.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentChangePasswordBinding

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etReturnChangePass.setOnClickListener {
            findNavController().navigateUp()
        }

        //? & forgetPass?
        binding.btnChangePass.setOnClickListener {
            //findNavController().navigate(R.id.action_changePasswordFragment_to_editProfileFragment)
        }
    }
}