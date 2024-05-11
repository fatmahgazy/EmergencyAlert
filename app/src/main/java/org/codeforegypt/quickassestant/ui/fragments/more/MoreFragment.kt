package org.codeforegypt.quickassestant.ui.fragments.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentMoreBinding

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    val viewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReturn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvProfile.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment2_to_userProfileFragment)
        }

        binding.tvMyEmergencies.setOnClickListener {
            findNavController().navigate(MoreFragmentDirections.actionMoreFragment2ToMyEmergencyFragment())
        }
        binding.llLogout.setOnClickListener {
            viewModel.logout()
        }

        observeLogout()
    }

    private fun observeLogout() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.logout.collectLatest {
                if (it) {
                    findNavController().navigate(MoreFragmentDirections.actionMoreFragment2ToLogInFragment())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}