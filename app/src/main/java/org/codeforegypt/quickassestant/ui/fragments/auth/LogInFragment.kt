package org.codeforegypt.quickassestant.ui.fragments.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.data.model.UserLoginModel
import org.codeforegypt.quickassestant.databinding.FragmentLogInBinding

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    val logInViewModel: LogInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLoginResult()
        observeUserExistence()

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToForgotPasswordFragment())
        }

        binding.btnSignIn.setOnClickListener {
            val errorMessage = validateLogInDetails()
            if (errorMessage == null) {

                val userData = UserLoginModel(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )

                logInViewModel.login(userData)
            } else
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }
    }

    private fun observeUserExistence() {
        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.userExists
                .collectLatest { userExists ->
                    if (userExists)
                        findNavController().navigate(R.id.action_logInFragment_to_mainFragment2)
                }
        }
    }

    private fun validateLogInDetails(): String? {
        val email = binding.etEmail.text.toString()
        if (email.isBlank())
            return "Email address is required"
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Please, enter valid email address"
        val password = binding.etPassword.toString()
        if (password.isBlank())
            return "Password field is required"
            else if (password.length < 8)
                return "Password length must me more then 8 characters and numbers"

        return null
    }

    private fun observeLoginResult(){
        viewLifecycleOwner.lifecycleScope.launch {
            logInViewModel.optionStatus
                .collectLatest {result ->
                    when(result) {
                        LogInResult.SUCCESS -> {
                            findNavController().navigate(R.id.action_logInFragment_to_mainFragment2)
                            Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT)
                                .show()
                        }
                        LogInResult.FAILED -> {
                            Toast.makeText(requireContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                        }
                        LogInResult.NORMAL -> {}
                    }
                }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }




}