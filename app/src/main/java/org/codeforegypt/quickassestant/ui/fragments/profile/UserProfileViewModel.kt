package org.codeforegypt.quickassestant.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.User
import org.codeforegypt.quickassestant.data.repository.ProfileRepo
import org.codeforegypt.quickassestant.domain.repository.IProfileRepo
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val profileRepo: IProfileRepo
): ViewModel() {
    private val _user= MutableStateFlow<User?>(null)
    val userData = _user.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun getUserProfile(){
        viewModelScope.launch (Dispatchers.IO){
            val userProfile = profileRepo.userProfile()
            _user.update {
                userProfile
            }
        }
    }
}