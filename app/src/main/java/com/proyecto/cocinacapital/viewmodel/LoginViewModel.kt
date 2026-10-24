package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.model.Usuario
import com.proyecto.cocinacapital.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UsuarioRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {
    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sembrarAdminSiNoExiste()
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    private fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) {
            return "El correo no puede estar vacío"
        }
        return null
    }

    private fun validarClave(clave: String): String? {
        if (clave.isBlank()) {
            return "La contraseña no puede estar vacía"
        }
        return null
    }

    fun iniciarSesion(onExito: (Usuario) -> Unit) {
        val actual = _estado.value
        val errores = LoginErrores(
            correo = validarCorreo(actual.correo),
            clave = validarClave(actual.clave)
        )
        _estado.update { it.copy(errores = errores) }
        if (errores != LoginErrores()) return

        viewModelScope.launch {
            val usuario = repository.iniciarSesion(actual.correo, actual.clave)
            if (usuario == null) {
                _estado.update {
                    it.copy(errores = it.errores.copy(clave = "Correo o contraseña incorrectos"))
                }
                return@launch
            }
            sesionViewModel.iniciarSesion(usuario)
            onExito(usuario)
        }
    }
}
