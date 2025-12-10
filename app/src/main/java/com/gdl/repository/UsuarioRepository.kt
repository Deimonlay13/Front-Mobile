package com.gdl.repository

import com.gdl.models.UsuarioEntity
import com.gdl.network.ApiService

class UsuarioRepository(
    private val api: ApiService
) {

    suspend fun getUsuario(id: Long): UsuarioEntity {
        return api.getUsuarioById(id)
    }

    suspend fun updateUsuario(id: Long, usuario: UsuarioEntity): UsuarioEntity {
        return api.updateUsuario(id, usuario)
    }
}