package org.codeforegypt.quickassestant.ui.fragments.contacts

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
import org.codeforegypt.quickassestant.data.model.ModifyContact
import org.codeforegypt.quickassestant.databinding.FragmentAddContactBinding

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

     val contactViewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)

        return binding.root

}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAddLoading()
        observeAddContact()
        binding.ivContactsReturn.setOnClickListener { findNavController().navigateUp() }
        binding.btnAddContact.setOnClickListener {

            binding.btnAddContact.setOnClickListener {
                val validation = validationContacts()
                if (validation != null) {
                    Toast.makeText(requireContext(), validation, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val name = binding.edName.text.toString()
                val phone = binding.edPhoneNumber.text.toString()

                val contact = ModifyContact(
                    name,
                    phone,
                    ""
                )
                //viewModel.addContact(contact)
                contactViewModel.addContact(contact)

            }
        }
    }

    private fun observeAddContact() {
        viewLifecycleOwner.lifecycleScope.launch {
            contactViewModel.contact.collectLatest { state ->
                when(state) {
                    ContactResult.FAILED -> Toast.makeText(requireContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                    ContactResult.SUCCESS -> findNavController().navigateUp()
                    ContactResult.NORMAL -> {}
                }
            }
        }
    }

    private fun validationContacts(): String? {
            if (binding.edName.text.toString().isBlank())
                return "Name is required"
            val phoneNumber = binding.edPhoneNumber.text.toString()
            if (binding.edPhoneNumber.text.toString().isBlank())
                return "PhoneNumber is required"
            else if (phoneNumber.length == 11 && !phoneNumber.startsWith("01"))
                return "Invalid phone number"
            else if (phoneNumber.length == 13 && !phoneNumber.startsWith("+20"))
                 return "Invalid phone number"

            return null
        }

    private fun observeAddLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            contactViewModel.loadingState
                .collectLatest { isLoading ->
                    addContactLoading(isLoading)
                }
        }
    }

    private fun addContactLoading(visible: Boolean) {
        binding.flLoading.isVisible = visible
    }

    }



