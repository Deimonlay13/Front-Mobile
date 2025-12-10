package com.gdl.repository

import com.gdl.models.DireccionEntity
import com.gdl.network.ApiService

class DireccionRepository(private val api: ApiService) {
    suspend fun obtenerDireccion(idUsuario: Long): DireccionEntity = api.getDireccionByUsuario(idUsuario)
    suspend fun guardarDireccion(idUsuario: Long, direccion: DireccionEntity): DireccionEntity = api.agregarDireccion(idUsuario, direccion)
}
