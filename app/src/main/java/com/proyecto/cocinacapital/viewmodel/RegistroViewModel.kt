package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import com.proyecto.cocinacapital.model.RegistroErrores
import com.proyecto.cocinacapital.model.RegistroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistroViewModel : ViewModel() {
    private val _estado = MutableStateFlow(RegistroUiState())
    val estado: StateFlow<RegistroUiState> = _estado.asStateFlow()


    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onConfirmarClaveChange(valor: String) {
        _estado.update {
            it.copy(
                confirmarClave = valor,
                errores = it.errores.copy(confirmarClave = null)
            )
        }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(
                aceptaTerminos = valor,
                errores = it.errores.copy(aceptaTerminos = null)
            )
        }
    }

    private fun validarNombre(nombre: String): String? {
        if (nombre.isBlank()) {
            return "el nombre no puede estar vacío"
        }
        return null
    }

    private fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) {
            return "El correo no puede estar vacío"
        }
        if (!Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(correo)) {
            return "el correo no tiene un formato válido, intentelo nuevamente..."
        }
        return null

    }

    private fun validarClave(clave: String): String? {

        if (clave.length < 8) {
            return "la contraseña debe contener al menos 8 caracteres"
        }
        if (clave.none { it.isLetter() }) {
            return "La contraseña debe contener al menos una letra"
        }
        if (clave.none { it.isDigit() }) {
            return "la contraseña debe contener al menos un dígito"
        }

        return null
    }

    private fun validarConfirmacion(confirmarClave: String, clave: String): String? {
        if (confirmarClave != clave) {
            return "La contraseña debe ser igual"
        }
        return null
    }

    private fun validarTerminos(acepta: Boolean): String? {
        if (!acepta) {
            return "Debes aceptar los términos y condiciones"
        }
        return null
    }

    fun registrar(): Boolean {
        val actual = _estado.value
        val errores = RegistroErrores(
            nombre = validarNombre(actual.nombre),
            correo = validarCorreo(actual.correo),
            clave = validarClave(actual.clave),
            confirmarClave = validarConfirmacion(actual.confirmarClave, actual.clave),
            aceptaTerminos = validarTerminos(actual.aceptaTerminos)
        )
        _estado.update { it.copy(errores = errores) }
        return errores == RegistroErrores()
    }


}