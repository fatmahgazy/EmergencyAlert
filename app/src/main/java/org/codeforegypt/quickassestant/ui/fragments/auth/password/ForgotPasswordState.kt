package org.codeforegypt.quickassestant.ui.fragments.auth.password

data class ForgotPasswordState(
    val email: String? = null,
    val state: PasswordCurrentState = PasswordCurrentState.NEED_EMAIL,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class PasswordCurrentState {
    NEED_EMAIL, NEED_NEW_PASSWORD, DONE
}