package com.gdl.models

data class Venta(
    val idVenta: Long,
    val fechaCreacion: String,
    val total: Double
)

data class Detalle(
    val idDetalle: Long,
    val nombreProducto: String?,
    val cantidad: Int,
    val precio: Double,
    val imagenProducto: String? = null
)
