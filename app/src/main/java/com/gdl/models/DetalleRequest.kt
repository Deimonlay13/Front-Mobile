package com.gdl.models

data class DetalleRequest(
    val idVenta: Long,
    val idProducto: Long,
    val cantidad: Int,
    val precio: Double
)
