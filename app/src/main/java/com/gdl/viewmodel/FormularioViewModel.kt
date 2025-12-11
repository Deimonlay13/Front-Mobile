package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.DireccionEntity
import com.gdl.models.FormularioUiState
import com.gdl.models.UsuarioEntity
import com.gdl.network.ApiClient
import com.gdl.network.ApiService
import com.gdl.repository.UserRepository
import com.gdl.repository.DireccionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FormularioViewModel : ViewModel() {

    private val userRepository = UserRepository(
        ApiClient.retrofit.create(ApiService::class.java)
    )

    private val direccionRepository = DireccionRepository(
        ApiClient.retrofit.create(ApiService::class.java)
    )

    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState


    // ============================================================
    //   CARGAR DATOS INICIALES
    // ============================================================
    fun cargarDatosIniciales(idUsuario: Long) {
        if (idUsuario == 0L) return

        viewModelScope.launch {
            try {
                // Datos del usuario
                val usuario = userRepository.getUsuario(idUsuario)

                _uiState.value = _uiState.value.copy(
                    nombre = usuario.nombre,
                    apellido = usuario.apellido,
                    correo = usuario.email,
                    rut = usuario.rut
                )

                // Dirección (si existe)
                try {
                    val direccion = direccionRepository.obtenerDireccion(idUsuario)

                    _uiState.value = _uiState.value.copy(
                        region = direccion.region,
                        comuna = direccion.comuna,
                        calle = direccion.calle,
                        numero = direccion.numero.toString()
                    )
                } catch (_: Exception) {
                    // Usuario aún no tiene dirección
                }

            } catch (e: Exception) {
                mostrarModal("Error", "No se pudieron cargar los datos del usuario.")
            }
        }
    }


    // ============================================================
    //   CAMPOS EDITABLES
    // ============================================================
    fun onRegionChange(v: String) = update { it.copy(region = v) }
    fun onComunaChange(v: String) = update { it.copy(comuna = v) }
    fun onCalleChange(v: String) = update { it.copy(calle = v) }
    fun onNumeroChange(v: String) = update { it.copy(numero = v) }

    private fun update(block: (FormularioUiState) -> FormularioUiState) {
        _uiState.value = block(_uiState.value)
    }


    // ============================================================
    //   VALIDACIÓN
    // ============================================================
    private fun validarFormulario(): Boolean {
        val s = _uiState.value

        val invalid =
            s.region.isBlank() ||
                    s.comuna.isBlank() ||
                    s.calle.isBlank() ||
                    s.numero.isBlank()

        update { it.copy(validated = true) }

        return !invalid
    }


    // ============================================================
    //   GUARDAR DIRECCIÓN
    // ============================================================
    fun guardarDireccion(idUsuario: Long) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            try {
                val s = _uiState.value

                val direccion = DireccionEntity(
                    region = s.region,
                    comuna = s.comuna,
                    calle = s.calle,
                    numero = s.numero.toIntOrNull()
                )

                // POST /direccion/usuario/{idUsuario}
                direccionRepository.agregarDireccion(idUsuario, direccion)

                mostrarModal("Dirección guardada", "Tu dirección se guardó correctamente.")

            } catch (e: Exception) {
                mostrarModal("Error", "Ocurrió un problema al guardar tu dirección.")
            }
        }
    }


    // ============================================================
    //   CONTINUAR
    // ============================================================
    fun continuarConPago() {
        if (validarFormulario()) {
            update { it.copy(navigateToPago = true) }
        }
    }


    // ============================================================
    //   MODALES
    // ============================================================
    private fun mostrarModal(titulo: String, mensaje: String) {
        update { it.copy(showModal = true, modalTitle = titulo, modalMessage = mensaje) }
    }

    fun cerrarModal() {
        update { it.copy(showModal = false) }
    }
}
