package org.codeforegypt.quickassestant.ui.fragments.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentContactsBinding

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private var isGeneral: Boolean = true

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->

    }

    private val viewModel: ContactsViewModel by viewModels()

    private val contactsAdapter = ContactAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isGeneral = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getContacts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContactsList()
        observeContacts()
        observeContactDeletion()
        observeLoading()
        contactsAdapter.onCallClick = {
            if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                callPhone(it.phone)
            else
                permission.launch(Manifest.permission.CALL_PHONE)
        }
        contactsAdapter.onDeleteClick = {
            viewModel.deleteContact(it)
        }
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState
                .collectLatest { isLoading ->
                    showLoading(isLoading)
                }
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.flLoading.isVisible = visible
    }


    private fun initToolbar() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imgAddContact.setOnClickListener {
            val action = ContactsFragmentDirections.actionContactsFragment2ToAddContactFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeContactDeletion() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteContacts.collectLatest { deletionState ->
                when(deletionState) {
                    ContactResult.FAILED -> Toast.makeText(requireContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                    ContactResult.SUCCESS -> findNavController().navigateUp()
                    ContactResult.NORMAL -> {}
                }
            }
        }
    }

    private fun callPhone(phone: String) {
        if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            permission.launch(Manifest.permission.CALL_PHONE)
        else{
            val dial = "tel: $phone"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(dial))
            startActivity(intent)
        }

    }

    private fun observeContacts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getContact.collectLatest { contacts ->
                contactsAdapter.submitList(contacts)
            }
        }
    }

    private fun initContactsList() {
        binding.rvContacts.apply {
            setHasFixedSize(true)
            adapter = contactsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(isGeneral: Boolean) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, isGeneral)
                }
            }
    }
}