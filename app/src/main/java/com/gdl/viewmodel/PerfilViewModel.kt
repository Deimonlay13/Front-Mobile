package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.UsuarioEntity
import com.gdl.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val isLoading: Boolean = false,
    val user: UsuarioEntity? = null,
    val password: String = "",
    val message: String? = null
)

class PerfilViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    // Cargar usuario por ID
    fun loadUser(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val usuario = repository.getUsuario(id)
                _uiState.update { it.copy(user = usuario.copy(contraseña = ""), isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, message = "Error al cargar usuario") }
            }
        }
    }

    // Actualizar campos de nombre o apellido
    fun updateField(field: String, value: String) {
        _uiState.update { current ->
            current.copy(
                user = current.user?.let {
                    when (field) {
                        "nombre" -> it.copy(nombre = value)
                        "apellido" -> it.copy(apellido = value)
                        else -> it
                    }
                }
            )
        }
    }

    // Actualizar password temporalmente
    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    // Guardar cambios en backend
    fun saveChanges() {
        val usuario = _uiState.value.user ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            try {
                val actualizado = repository.updateUsuario(
                    usuario.idUsuario ?: return@launch,
                    usuario.copy(contraseña = if (_uiState.value.password.isBlank()) null else _uiState.value.password)
                )
                _uiState.update {
                    it.copy(
                        user = actualizado.copy(contraseña = ""),
                        password = "",
                        isLoading = false,
                        message = "Perfil actualizado correctamente"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, message = "Error al actualizar perfil") }
            }
        }
    }

    // Eliminar usuario
    fun deleteUser() {
        val usuario = _uiState.value.user ?: return
        viewModelScope.launch {
            try {
                repository.deleteUsuario(usuario.idUsuario ?: return@launch)
            } catch (e: Exception) {
                _uiState.update { it.copy(message = "Error al eliminar cuenta") }
            }
        }
    }
}
