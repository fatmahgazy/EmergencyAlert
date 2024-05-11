package org.codeforegypt.quickassestant.ui.fragments.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.ModifyContact
import org.codeforegypt.quickassestant.domain.repository.IContactRepo
import org.codeforegypt.quickassestant.ui.fragments.contacts.ContactResult
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepo: IContactRepo
): ViewModel(){
    private var _contact = MutableStateFlow(ContactResult.NORMAL)
    val contact = _contact.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ContactResult.NORMAL
    )
    private var _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun addContact(contact: ModifyContact){
        viewModelScope.launch(Dispatchers.IO){
            _loadingState.update { true }
            val addedContact = contactRepo.addContact(contact)
            _loadingState.update { false }
            _contact.update {
                if (addedContact)
                 ContactResult.SUCCESS
                else {
                    ContactResult.FAILED
                }
            }
        }
    }
}