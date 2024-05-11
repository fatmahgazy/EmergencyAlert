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
import org.codeforegypt.quickassestant.data.model.Contact
import org.codeforegypt.quickassestant.domain.repository.IContactRepo
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactRepo: IContactRepo
): ViewModel() {

    private var _getContacts= MutableStateFlow<List<Contact>>(emptyList())
    val getContact = _getContacts.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList<Contact>()
    )

    private var _deleteContacts= MutableStateFlow(ContactResult.NORMAL)
    val deleteContacts = _deleteContacts.stateIn(
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

    fun getContacts(){
        viewModelScope.launch(Dispatchers.IO){
            _loadingState.update { true }
            val getContacts = contactRepo.getContacts()
            _loadingState.update { false }
            _getContacts.update {
                getContacts
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.update { true }
            val isSuccess = contactRepo.deleteContact(contact)
            _loadingState.update { false }
            _deleteContacts.update {
                if (isSuccess)
                    ContactResult.SUCCESS
                else
                    ContactResult.FAILED
            }
        }
    }

}
