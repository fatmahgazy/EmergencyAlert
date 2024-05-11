package org.codeforegypt.quickassestant.ui.fragments.auth.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentForgotPasswordBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeForgotPasswordState()
        binding.btnPost.setOnClickListener {
            when(viewModel.getCurrentViewState()) {
                PasswordCurrentState.NEED_EMAIL -> sendEmail()
                PasswordCurrentState.NEED_NEW_PASSWORD -> resetPassword()
                PasswordCurrentState.DONE -> {}
            }
        }
    }

    private fun resetPassword() {
        if (binding.edCode.text.toString().trim().isBlank()) {
            Toast.makeText(requireContext(), "Please enter a valid code.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (binding.edNewPassword.text.toString().trim().isBlank()) {
            Toast.makeText(requireContext(), "Please enter a valid code.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (binding.edNewPassword.text.toString().trim().length > 8) {
            Toast.makeText(requireContext(), "Please enter a password with at least 8 digits.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        viewModel.resetPassword(binding.edCode.text.toString(), binding.edNewPassword.text.toString())
    }

    private fun sendEmail() {
        if (binding.edEmail.text.toString().trim().isBlank()) {
            Toast.makeText(requireContext(), "Please enter a valid email.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        viewModel.sendEmail(binding.edEmail.text.toString())
    }

    private fun observeForgotPasswordState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                showLoading(state.isLoading)
                if (state.error != null) {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    return@collectLatest
                }

                when(state.state) {
                    PasswordCurrentState.NEED_EMAIL -> showEmailView()
                    PasswordCurrentState.NEED_NEW_PASSWORD -> showNewPasswordView()
                    PasswordCurrentState.DONE -> {
                        Toast.makeText(requireContext(), "New password reset successfully!", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun showEmailView() {
        binding.edEmail.isVisible = true
        binding.llNewPassword.isVisible = false
        binding.flLoading.isVisible = false
    }

    private fun showNewPasswordView() {
        binding.edEmail.isVisible = false
        binding.llNewPassword.isVisible = true
        binding.flLoading.isVisible = false
    }

    private fun showLoading(show: Boolean) {
        binding.flLoading.isVisible = show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}