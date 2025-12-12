package com.gdl.network

import com.gdl.models.DetalleRequest
import com.gdl.models.DetalleResponse
import com.gdl.models.UsuarioEntity
import com.gdl.models.DireccionEntity
import retrofit2.http.*
import com.gdl.models.LoginRequest
import com.gdl.models.LoginResponse
import com.gdl.models.RegisterRequest
import com.gdl.models.VentaRequest
import com.gdl.models.VentaResponse

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    //   REGISTER
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): String


    //   USUARIO
    @GET("usuario/{id}")
    suspend fun getUsuarioById(
        @Path("id") id: Long
    ): UsuarioEntity

    @PUT("usuario/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Long,
        @Body usuario: UsuarioEntity
    ): UsuarioEntity


    //   DIRECCIÓN
    @GET("direccion/usuario/{idUsuario}")
    suspend fun getDireccionByUsuario(
        @Path("idUsuario") idUsuario: Long
    ): DireccionEntity

    @POST("direccion/usuario/{idUsuario}")
    suspend fun agregarDireccion(
        @Path("idUsuario") idUsuario: Long,
        @Body direccion: DireccionEntity
    ): DireccionEntity


    //   VENTAS
    @POST("venta")
    suspend fun crearVenta(
        @Body venta: VentaRequest
    ): VentaResponse

    @POST("detalle-venta")
    suspend fun crearDetalle(
        @Body detalle: DetalleRequest
    ): DetalleResponse
}
