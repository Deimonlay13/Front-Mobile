package com.gdl.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.RegisterRequest
import com.gdl.network.ApiClient
import com.gdl.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val apellido: String = "",
    val rut: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val apellidoError: String? = null,
    val rutError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val registerResult: RegisterResult? = null
)

sealed class RegisterResult {
    data class Success(val message: String) : RegisterResult()
    data class Error(val message: String) : RegisterResult()
    object Idle : RegisterResult()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val api = ApiClient.retrofit.create(ApiService::class.java)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) { _uiState.value = _uiState.value.copy(name = name, nameError = null) }
    fun onApellidoChange(apellido: String) { _uiState.value = _uiState.value.copy(apellido = apellido, apellidoError = null) }
    fun onRutChange(rut: String) { _uiState.value = _uiState.value.copy(rut = rut, rutError = null) }
    fun onEmailChange(email: String) { _uiState.value = _uiState.value.copy(email = email, emailError = null) }
    fun onPasswordChange(password: String) { _uiState.value = _uiState.value.copy(password = password, passwordError = null) }
    fun onConfirmPasswordChange(confirmPassword: String) { _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }

    fun onRegisterClick() {
        val name = _uiState.value.name
        val apellido = _uiState.value.apellido
        val rut = _uiState.value.rut
        val email = _uiState.value.email
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        var valid = true
        if (name.isBlank()) { _uiState.value = _uiState.value.copy(nameError = "Nombre requerido"); valid = false }
        if (apellido.isBlank()) { _uiState.value = _uiState.value.copy(apellidoError = "Apellido requerido"); valid = false }
        if (rut.isBlank()) { _uiState.value = _uiState.value.copy(rutError = "RUT requerido"); valid = false }
        if (!email.contains("@")) { _uiState.value = _uiState.value.copy(emailError = "Email inválido"); valid = false }
        if (password.length < 6) { _uiState.value = _uiState.value.copy(passwordError = "Mínimo 6 caracteres"); valid = false }
        if (password != confirmPassword) { _uiState.value = _uiState.value.copy(confirmPasswordError = "Contraseñas no coinciden"); valid = false }
        if (!valid) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, registerResult = RegisterResult.Idle)
            try {
                val request = RegisterRequest(name, apellido, email, password, rut)
                val response: String = api.register(request)
                _uiState.value = _uiState.value.copy(isLoading = false, registerResult = RegisterResult.Success(response))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, registerResult = RegisterResult.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    fun resetRegisterResult() {
        _uiState.value = _uiState.value.copy(registerResult = RegisterResult.Idle)
    }
}
