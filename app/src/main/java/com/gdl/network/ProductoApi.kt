package com.gdl.network
import com.gdl.models.Producto
import retrofit2.http.GET
interface ProductoApi {
    @GET("Producto/")
    suspend fun getCartas(): List<Producto>
}