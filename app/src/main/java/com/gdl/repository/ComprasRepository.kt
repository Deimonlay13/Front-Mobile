package com.gdl.repository

import com.gdl.models.Venta
import com.gdl.models.Detalle
import com.gdl.network.ApiService

class ComprasRepository(private val api: ApiService) {

    suspend fun getVentasByUsuario(idUsuario: Long): List<Venta> {
        return api.getVentasByUsuario(idUsuario)
    }

    suspend fun getDetallesByVenta(idVenta: Long): List<Detalle> {
        return api.getDetallesByVenta(idVenta)
    }
}
