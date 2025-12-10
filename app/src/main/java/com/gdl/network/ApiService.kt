package com.gdl.network

import com.gdl.models.UsuarioEntity
import com.gdl.models.DireccionEntity
import retrofit2.http.*

interface ApiService {

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
}
