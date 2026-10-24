package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.Usuario
import com.proyecto.cocinacapital.model.UsuarioDao

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario): Long = dao.insertar(usuario)

    suspend fun existeCorreo(correo: String): Boolean = dao.obtenerPorCorreo(correo) != null

    suspend fun iniciarSesion(correo: String, clave: String): Usuario? {
        val usuario = dao.obtenerPorCorreo(correo) ?: return null
        return if (usuario.clave == clave) usuario else null
    }
}
