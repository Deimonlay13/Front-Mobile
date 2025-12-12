package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.DireccionEntity
import com.gdl.models.FormularioUiState
import com.gdl.models.CarritoItem
import com.gdl.network.ApiClient
import com.gdl.network.ApiService
import com.gdl.repository.DireccionRepository
import com.gdl.repository.UserRepository
import com.gdl.repository.CompraRepository
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

    private val compraRepository = CompraRepository(
        ApiClient.retrofit.create(ApiService::class.java)
    )

    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState

    private fun update(block: (FormularioUiState) -> FormularioUiState) {
        _uiState.value = block(_uiState.value)
    }

    // Cargar datos iniciales
    fun cargarDatosIniciales(idUsuario: Long) {
        if (idUsuario == 0L) return

        viewModelScope.launch {
            try {
                val usuario = userRepository.getUsuario(idUsuario)
                update {
                    it.copy(
                        nombre = usuario.nombre,
                        apellido = usuario.apellido,
                        correo = usuario.email,
                        rut = usuario.rut
                    )
                }

                try {
                    val direccion = direccionRepository.obtenerDireccion(idUsuario)
                    update {
                        it.copy(
                            region = direccion.region,
                            comuna = direccion.comuna,
                            calle = direccion.calle,
                            numero = direccion.numero?.toString() ?: ""
                        )
                    }
                } catch (_: Exception) { }

            } catch (_: Exception) {
                mostrarModal("Error", "No se pudieron cargar los datos del usuario.")
            }
        }
    }

    // Campos editables
    fun onRegionChange(v: String) = update { it.copy(region = v) }
    fun onComunaChange(v: String) = update { it.copy(comuna = v) }
    fun onCalleChange(v: String) = update { it.copy(calle = v) }
    fun onNumeroChange(v: String) = update { it.copy(numero = v) }

    // Guardar dirección
    fun guardarDireccion(idUsuario: Long) {
        viewModelScope.launch {
            val s = _uiState.value

            if (s.region.isBlank() || s.comuna.isBlank() || s.calle.isBlank() || s.numero.isBlank()) {
                update { it.copy(validated = true) }
                mostrarModal("Campos incompletos", "Completa los campos obligatorios.")
                return@launch
            }

            try {
                val direccion = DireccionEntity(
                    region = s.region,
                    comuna = s.comuna,
                    calle = s.calle,
                    numero = s.numero.toIntOrNull()
                )

                direccionRepository.agregarDireccion(idUsuario, direccion)

                mostrarModal("Dirección guardada", "Tu dirección se guardó correctamente.")

            } catch (_: Exception) {
                mostrarModal("Error", "No se pudo guardar la dirección.")
            }
        }
    }

    // VALIDAR
    private fun validarFormulario(): Boolean {
        val s = _uiState.value

        val invalid = s.region.isBlank() ||
                s.comuna.isBlank() ||
                s.calle.isBlank() ||
                s.numero.isBlank()

        if (invalid) update { it.copy(validated = true) }

        return !invalid
    }

    fun setTotal(total: Int) {
        update { it.copy(totalAmount = total) }
    }

    fun resetNavigation() {
        update { it.copy(navigateToPago = false) }
    }

    // NUEVA FUNCIÓN: REGISTRAR COMPRA
    fun realizarCompra(idUsuario: Long, carrito: List<CarritoItem>) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            try {
                val total = _uiState.value.totalAmount

                compraRepository.guardarVenta(
                    idUsuario = idUsuario,
                    carrito = carrito,
                    total = total
                )

                update { it.copy(navigateToPago = true) }

            } catch (e: Exception) {
                e.printStackTrace()
                mostrarModal("Error", "Hubo un problema al registrar la compra.")
            }
        }
    }

    // Modales
    private fun mostrarModal(titulo: String, mensaje: String) {
        update { it.copy(showModal = true, modalTitle = titulo, modalMessage = mensaje) }
    }

    fun cerrarModal() {
        update { it.copy(showModal = false) }
    }
}
