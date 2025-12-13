package com.gdl.repository

import com.gdl.models.VentaRequest
import com.gdl.models.DetalleRequest
import com.gdl.models.VentaResponse
import com.gdl.models.DetalleResponse
import com.gdl.network.ApiService

class VentaRepository(private val api: ApiService) {

    // ---------- CREAR VENTA ----------
    suspend fun crearVenta(idUsuario: Long, total: Int): VentaResponse {
        val body = VentaRequest(
            idUsuario = idUsuario,
            total = total.toDouble()
        )

        return api.crearVenta(body)
    }

    // ---------- CREAR DETALLE ----------
    suspend fun crearDetalle(
        idVenta: Long,
        idCarta: Long,
        cantidad: Int,
        precio: Double
    ): DetalleResponse {

        val body = DetalleRequest(
            idVenta = idVenta,
            idProducto = idCarta,
            cantidad = cantidad,
            precio = precio
        )

        return api.crearDetalle(body)
    }
}
