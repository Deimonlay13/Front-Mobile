package com.gdl.repository

import com.gdl.models.UsuarioEntity
import com.gdl.network.ApiService

class UserRepository(private val api: ApiService) {
    suspend fun getUsuario(id: Long): UsuarioEntity = api.getUsuarioById(id)
    suspend fun updateUsuario(id: Long, usuario: UsuarioEntity): UsuarioEntity = api.updateUsuario(id, usuario)
    suspend fun deleteUsuario(id: Long): Boolean {
        val response = api.deleteUsuario(id)
        return response.isSuccessful
    }

}
