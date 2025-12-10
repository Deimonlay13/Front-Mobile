package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.DireccionEntity
import com.gdl.models.FormularioUiState
import com.gdl.repository.DireccionRepository
import com.gdl.repository.UsuarioRepository
import com.gdl.network.ApiClient
import com.gdl.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FormularioViewModel(
    private val usuarioRepository: UsuarioRepository = UsuarioRepository(
        ApiClient.retrofit.create(ApiService::class.java)
    ),
    private val direccionRepository: DireccionRepository = DireccionRepository(
        ApiClient.retrofit.create(ApiService::class.java)
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState

    // CARGA INICIAL
    fun cargarDatosIniciales(idUsuario: Long) {
        viewModelScope.launch {
            try {
                val user = usuarioRepository.getUsuario(idUsuario)
                _uiState.value = _uiState.value.copy(
                    nombre = user.nombre,
                    apellido = user.apellido,
                    correo = user.email,
                    rut = user.rut
                )

                try {
                    val direccion = direccionRepository.obtenerDireccion(idUsuario)
                    _uiState.value = _uiState.value.copy(
                        region = direccion.region,
                        comuna = direccion.comuna,
                        calle = direccion.calle,
                        numero = direccion.numero
                    )
                } catch (_: Exception) {
                    // No tiene dirección → ignoramos
                }

            } catch (_: Exception) {
                mostrarModal("Error", "No se pudo cargar la información del usuario.")
            }
        }
    }

    // ACTUALIZACIÓN DE CAMPOS
    fun onCorreoChange(v: String) = update { it.copy(correo = v) }
    fun onNombreChange(v: String) = update { it.copy(nombre = v) }
    fun onApellidoChange(v: String) = update { it.copy(apellido = v) }
    fun onRutChange(v: String) = update { it.copy(rut = v) }
    fun onRegionChange(v: String) = update { it.copy(region = v, comuna = "", otraComuna = "") }
    fun onComunaChange(v: String) = update { it.copy(comuna = v) }
    fun onOtraComunaChange(v: String) = update { it.copy(otraComuna = v) }
    fun onCalleChange(v: String) = update { it.copy(calle = v) }
    fun onNumeroChange(v: String) = update { it.copy(numero = v) }

    private fun update(block: (FormularioUiState) -> FormularioUiState) {
        _uiState.value = block(_uiState.value)
    }

    // VALIDACIÓN
    fun validarFormulario(): Boolean {
        val s = _uiState.value
        val camposInvalidos =
            s.correo.isBlank() ||
                    s.nombre.isBlank() ||
                    s.apellido.isBlank() ||
                    s.rut.isBlank() ||
                    s.calle.isBlank() ||
                    s.numero.isBlank()

        if (camposInvalidos) {
            update { it.copy(validated = true) }
            return false
        }
        update { it.copy(validated = true) }
        return true
    }

    // GUARDAR DIRECCIÓN
    fun guardarDireccion(idUsuario: Long) {
        viewModelScope.launch {
            try {
                update { it.copy(isSaving = true) }
                val s = _uiState.value
                val direccion = DireccionEntity(
                    region = s.region,
                    comuna = if (s.comuna == "otra") s.otraComuna else s.comuna,
                    calle = s.calle,
                    numero = s.numero
                )
                direccionRepository.guardarDireccion(idUsuario, direccion)
                mostrarModal("Dirección guardada", "✔ La dirección se ha guardado correctamente.")
            } catch (_: Exception) {
                mostrarModal("Error", "Ocurrió un error al guardar la dirección.")
            } finally {
                update { it.copy(isSaving = false) }
            }
        }
    }

    // CONTINUAR AL PAGO
    fun continuarConPago() {
        if (!validarFormulario()) return
        update { it.copy(showSuccess = true, navigateToPago = true) }
    }

    // MODALES
    fun mostrarModal(titulo: String, mensaje: String) {
        update { it.copy(showModal = true, modalTitle = titulo, modalMessage = mensaje) }
    }

    fun cerrarModal() {
        update { it.copy(showModal = false) }
    }
}
