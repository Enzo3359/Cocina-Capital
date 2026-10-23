package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.Usuario
import com.proyecto.cocinacapital.model.UsuarioDao

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario): Long = dao.insertar(usuario)

    suspend fun existeCorreo(correo: String): Boolean = dao.obtenerPorCorreo(correo) != null
}
