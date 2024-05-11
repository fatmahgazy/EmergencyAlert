package org.codeforegypt.quickassestant.ui.fragments.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.db.UserDao
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val userDao: UserDao
): ViewModel() {

    private val _logout = MutableStateFlow(false)
    val logout = _logout.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteAllUsers()
            _logout.update { true }
        }
    }

}