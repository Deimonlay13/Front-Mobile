package com.gdl.repository

import com.gdl.models.CarritoItem
import com.gdl.models.VentaRequest
import com.gdl.models.DetalleRequest
import com.gdl.network.ApiService

class CompraRepository(private val api: ApiService) {

    suspend fun guardarVenta(idUsuario: Long, carrito: List<CarritoItem>, total: Int) {
        try {

            // ---------- CREAR VENTA ----------
            val ventaRequest = VentaRequest(
                idUsuario = idUsuario,
                total = total.toDouble()
            )

            println("ENVIANDO /venta → $ventaRequest")

            val ventaResponse = api.crearVenta(ventaRequest)

            println("RESPUESTA /venta → $ventaResponse")

            val idVenta = ventaResponse.idVenta


            // ---------- CREAR DETALLES ----------
            carrito.forEach { item ->

                val detalleRequest = DetalleRequest(
                    idVenta = idVenta,
                    idCarta = item.producto.id.toLong(),
                    cantidad = item.cantidad,
                    precio = item.producto.precio.toDouble()
                )

                println("ENVIANDO /detalle-venta → $detalleRequest")

                val detalleResponse = api.crearDetalle(detalleRequest)

                println("RESPUESTA /detalle-venta → $detalleResponse")
            }

        } catch (e: Exception) {
            println("ERROR REAL EN COMPRA: ${e.message}")
            e.printStackTrace()
            throw Exception("Hubo un error al registrar la compra: ${e.message}")
        }
    }
}
