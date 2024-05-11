package org.codeforegypt.quickassestant.ui.fragments.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.data.model.UserSignUpModel
import org.codeforegypt.quickassestant.databinding.FragmentSignUpBinding

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var selectedGender: String? = null

    private var selectedBloodType: String? = null

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()
        observeSignUpResult()

        val spinnerGender = arrayOf("Male" , "Female")
        val genderArrayAdapter = ArrayAdapter<String>(requireContext() , android.R.layout.simple_spinner_dropdown_item, spinnerGender)
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = genderArrayAdapter

        binding.spinnerGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(requireContext() , "${spinnerGender[p2]}" , Toast.LENGTH_SHORT).show()

                selectedGender = spinnerGender[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        val spinnerBloodType = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val bloodTypeArrayAdapter = ArrayAdapter<String>(requireContext() , android.R.layout.simple_spinner_dropdown_item, spinnerBloodType)
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBloodType.adapter = bloodTypeArrayAdapter

        binding.spinnerBloodType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               Toast.makeText(requireContext(), "${spinnerBloodType[p2]}", Toast.LENGTH_SHORT).show()

                selectedBloodType = spinnerBloodType[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }



        binding.btnSignUp.setOnClickListener {
            val validationResult = validateSignUpDetails()
            if (validationResult != null) {
                Toast.makeText(requireContext(), validationResult, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val fullName = binding.etFullName.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val email = binding.etEmail.text.toString()
            val age = binding.etAge.text.toString()
            val password = binding.etPassword.text.toString()

            val userData = UserSignUpModel(
                age,
                selectedBloodType!!,
                email,
                fullName,
                selectedGender!!,
                phoneNumber,
                password
            )

            signUpViewModel.signUp(userData)
        }
    }

    private fun observeSignUpResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            signUpViewModel.operationStatus
                .collectLatest { result ->
                    when(result) {
                        SignUpResult.SUCCESS -> {
                            findNavController().navigate(R.id.action_signUpFragment_to_mainFragment)
                        }
                        SignUpResult.FAILED -> {
                            Toast.makeText(requireContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                        }
                        SignUpResult.NORMAL -> {}
                    }
                }
        }
    }


    private fun validateSignUpDetails(): String? {
        if (binding.etFullName.text.toString().isBlank())
            return "Full name is required"
        if (binding.etEmail.text.toString().isBlank())
            return "Email address is required"
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches())
            return "Please, enter valid email address"
        if (binding.etPassword.text.toString().isBlank())
            return "Password field is required"
        else if (binding.etPassword.text.toString().length < 8)
            return "Password length must me more then 8 characters and numbers"
        if (selectedGender.isNullOrBlank())
            return "Gender is required"
        if (selectedBloodType.isNullOrBlank())
            return "Gender is required"
        if (binding.etAge.text.isBlank())
            return "Age is required"
        val phoneNumber = binding.etPhoneNumber.text.toString()
        if (binding.etPhoneNumber.text.toString().isBlank())
            return "Phone number is required"
        else if (phoneNumber.length == 11 && !phoneNumber.startsWith("01"))
            return "Invalid Phone number"
        else if (phoneNumber.length == 13 && !phoneNumber.startsWith("+20"))
            return "Invalid phone number"

        return null

    }
    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
           signUpViewModel.loadingState
                .collectLatest { isLoading ->
                    showLoading(isLoading)
                }
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.flLoading.isVisible = visible
    }
}