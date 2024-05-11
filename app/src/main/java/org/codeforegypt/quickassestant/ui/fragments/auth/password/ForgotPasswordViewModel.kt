package org.codeforegypt.quickassestant.ui.fragments.auth.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.domain.repository.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: IAuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ForgotPasswordState())

    fun getCurrentViewState(): PasswordCurrentState {
        return _state.value.state
    }

    fun sendEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val isSuccessful = authRepository.forgotPasswordCode(email)
            _state.update {
                it.copy(
                    email = email,
                    isLoading = false,
                    state = if (isSuccessful) PasswordCurrentState.NEED_NEW_PASSWORD else it.state,
                    error = if (isSuccessful.not()) "Something went wrong!" else null
                )
            }
        }
    }

    fun resetPassword(code: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val error = authRepository.forgotPasswordNewPassword(_state.value.email!!, code, newPassword)
            _state.update {
                it.copy(
                    isLoading = false,
                    state = if (error == null) PasswordCurrentState.DONE else it.state,
                    error = error
                )
            }
        }
    }
}