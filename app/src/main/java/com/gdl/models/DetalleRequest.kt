package com.gdl.models

data class DetalleRequest(
    val idVenta: Long,
    val idCarta: Long,
    val cantidad: Int,
    val precio: Double
)
