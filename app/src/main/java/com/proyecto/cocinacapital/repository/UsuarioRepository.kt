package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.RolUsuario
import com.proyecto.cocinacapital.model.Usuario
import com.proyecto.cocinacapital.model.UsuarioDao

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario): Long = dao.insertar(usuario)

    suspend fun existeCorreo(correo: String): Boolean = dao.obtenerPorCorreo(correo) != null

    suspend fun sembrarAdminSiNoExiste() {
        val correoAdmin = "admin@cocinacapital.cl"
        if (!existeCorreo(correoAdmin)) {
            dao.insertar(
                Usuario(
                    nombre = "Administrador",
                    correo = correoAdmin,
                    clave = "admin123",
                    rol = RolUsuario.ADMINISTRADOR
                )
            )
        }
    }

    suspend fun sembrarPersonalSiNoExiste() {
        val correoPersonal = "personal@cocinacapital.cl"
        if (!existeCorreo(correoPersonal)) {
            dao.insertar(
                Usuario(
                    nombre = "Personal",
                    correo = correoPersonal,
                    clave = "personal123",
                    rol = RolUsuario.PERSONAL
                )
            )
        }
    }

    suspend fun iniciarSesion(correo: String, clave: String): Usuario? {
        val usuario = dao.obtenerPorCorreo(correo) ?: return null
        return if (usuario.clave == clave) usuario else null
    }
}
