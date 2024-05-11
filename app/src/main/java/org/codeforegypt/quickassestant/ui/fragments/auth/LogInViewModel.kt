package org.codeforegypt.quickassestant.ui.fragments.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.UserLoginModel
import org.codeforegypt.quickassestant.domain.repository.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val authRepository: IAuthRepository
): ViewModel() {
    private val _operationStatus = MutableStateFlow(LogInResult.NORMAL)
    val optionStatus = _operationStatus.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LogInResult.NORMAL
    )
    private val _userExists = MutableStateFlow(false)
    val userExists = _userExists.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _userExists.update { authRepository.userExists() }
        }
    }

    fun login(user: UserLoginModel){
        viewModelScope.launch(Dispatchers.IO){
            val isSuccessfulOperation = authRepository.login(user)
            _operationStatus.update {
                if (isSuccessfulOperation)
                    LogInResult.SUCCESS
                else
                    LogInResult.FAILED
            }
        }
    }


}