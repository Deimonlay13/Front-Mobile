package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.Venta
import com.gdl.models.Detalle
import com.gdl.repository.ComprasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ComprasUiState(
    val isLoading: Boolean = true,
    val ventas: List<Venta> = emptyList(),
    val detalles: List<Detalle> = emptyList(),
    val ventaSeleccionada: Long? = null,
    val message: String? = null
)

class ComprasViewModel(private val repository: ComprasRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ComprasUiState())
    val uiState: StateFlow<ComprasUiState> = _uiState

    fun loadCompras(idUsuario: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val ventas = repository.getVentasByUsuario(idUsuario)
                _uiState.value = _uiState.value.copy(ventas = ventas, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error al cargar compras"
                )
            }
        }
    }

    fun verDetalle(idVenta: Long) {
        viewModelScope.launch {
            try {
                val detalles = repository.getDetallesByVenta(idVenta)
                _uiState.value = _uiState.value.copy(
                    detalles = detalles,
                    ventaSeleccionada = idVenta
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(message = "Error al cargar detalles")
            }
        }
    }
}
