package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.LoginRequest
import com.gdl.models.LoginResponse
import com.gdl.network.ApiClient
import com.gdl.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado de UI
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginResponse: LoginResponse? = null,
    val errorMessage: String? = null,
    val passwordVisible: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Crear instancia de ApiService
    private val api: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    private fun validateFields(): Boolean {
        val emailError = if (!_uiState.value.email.contains("@")) "Email inválido" else null
        val passwordError = if (_uiState.value.password.length < 6) "Mínimo 6 caracteres" else null
        _uiState.value = _uiState.value.copy(emailError = emailError, passwordError = passwordError)
        return emailError == null && passwordError == null
    }

    fun onLoginClick() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, loginResponse = null)
            try {
                val response: LoginResponse = api.login(
                    LoginRequest(
                        email = _uiState.value.email,
                        contraseña = _uiState.value.password
                    )
                )
                _uiState.value = _uiState.value.copy(isLoading = false, loginResponse = response)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun resetLogin() {
        _uiState.value = _uiState.value.copy(
            loginResponse = null,
            errorMessage = null,
            isLoading = false
        )
    }
}
