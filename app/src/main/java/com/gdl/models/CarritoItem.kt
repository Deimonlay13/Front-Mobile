package com.gdl.models

import com.gdl.models.Producto
data class CarritoItem(
    val producto: Producto,
    val cantidad: Int,


){
    val subtotal: Int
        get() = producto.precio * cantidad
}
