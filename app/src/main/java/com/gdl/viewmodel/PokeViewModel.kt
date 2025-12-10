package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdl.models.Producto
import com.gdl.models.CarritoItem
import com.gdl.network.ApiClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PokeViewModel : ViewModel() {

    // Lista de productos
    private val _cartas = MutableStateFlow<List<Producto>>(emptyList())
    val cartas: StateFlow<List<Producto>> = _cartas

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    // CARRITO
    private val _carrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carrito: StateFlow<List<CarritoItem>> = _carrito

    init {
        cargarProductos()
    }

    // ⭐ Carga los productos desde tu API
    private fun cargarProductos() {
        viewModelScope.launch {
            try {
                _cargando.value = true

                val api = ApiClient.retrofit.create(com.gdl.network.ProductoApi::class.java)
                val resultado = api.getCartas()

                _cartas.value = resultado

            } catch (e: Exception) {
                println("❌ ERROR cargando productos: ${e.message}")
            } finally {
                _cargando.value = false
            }
        }
    }

    // ⭐ AGREGAR AL CARRITO
    fun agregarAlCarrito(producto: Producto, cantidad: Int) {
        if (cantidad <= 0) return

        val listaActual = _carrito.value.toMutableList()

        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val itemExistente = listaActual[index]
            listaActual[index] = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad
            )
        } else {
            listaActual.add(CarritoItem(producto, cantidad))
        }

        _carrito.value = listaActual
    }



    fun restarCantidad(producto: Producto) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val item = listaActual[index]
            val nuevaCantidad = item.cantidad - 1

            if (nuevaCantidad <= 0) {
                listaActual.removeAt(index)
            } else {
                listaActual[index] = item.copy(cantidad = nuevaCantidad)
            }

            _carrito.value = listaActual
        }
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito.value = _carrito.value.filter { it.producto.id != producto.id }
    }
}
