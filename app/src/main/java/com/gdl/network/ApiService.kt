package com.gdl.network

import com.gdl.models.Detalle
import com.gdl.models.UsuarioEntity
import com.gdl.models.DireccionEntity
import retrofit2.http.*
import com.gdl.models.LoginRequest
import com.gdl.models.LoginResponse
import com.gdl.models.RegisterRequest
import com.gdl.models.Venta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // =======================
    //   USUARIO
    // =======================

    @GET("usuario/{id}")
    suspend fun getUsuarioById(
        @Path("id") id: Long
    ): UsuarioEntity

    @PUT("usuario/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Long,
        @Body usuario: UsuarioEntity
    ): UsuarioEntity


    @DELETE("usuario/{id}")
    suspend fun deleteUsuario(@Path("id") id: Long): Response<Unit>

    // =======================
    //   DIRECCIÓN
    // =======================

    @GET("direccion/usuario/{idUsuario}")
    suspend fun getDireccionByUsuario(
        @Path("idUsuario") idUsuario: Long
    ): DireccionEntity

    @POST("direccion/usuario/{idUsuario}")
    suspend fun agregarDireccion(
        @Path("idUsuario") idUsuario: Long,
        @Body direccion: DireccionEntity
    ): DireccionEntity
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): String


    @GET("venta/usuario/{idUsuario}")
    suspend fun getVentasByUsuario(@Path("idUsuario") idUsuario: Long): List<Venta>

    @GET("detalle-venta/venta/{idVenta}")
    suspend fun getDetallesByVenta(@Path("idVenta") idVenta: Long): List<Detalle>
}



