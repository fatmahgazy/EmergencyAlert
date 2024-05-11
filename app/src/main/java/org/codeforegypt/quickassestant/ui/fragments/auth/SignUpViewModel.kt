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
import org.codeforegypt.quickassestant.data.model.UserSignUpModel
import org.codeforegypt.quickassestant.domain.repository.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: IAuthRepository
): ViewModel() {

    private val _operationStatus = MutableStateFlow(SignUpResult.NORMAL)
    val operationStatus = _operationStatus.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SignUpResult.NORMAL
    )
    private var _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun signUp(userSignUpModel: UserSignUpModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.update { true }
            val isSuccessfulOperation = authRepository.signUp(userSignUpModel)
            _loadingState.update { false }
            _operationStatus.update {
                if (isSuccessfulOperation)
                    SignUpResult.SUCCESS
                else
                    SignUpResult.FAILED
            }
        }
    }

}