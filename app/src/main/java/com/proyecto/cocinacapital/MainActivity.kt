package com.proyecto.cocinacapital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.proyecto.cocinacapital.model.AppDatabase
import com.proyecto.cocinacapital.repository.UsuarioRepository
import com.proyecto.cocinacapital.ui.theme.CocinaCapitalTheme
import com.proyecto.cocinacapital.navigation.AppNavHost
import com.proyecto.cocinacapital.viewmodel.InicioViewModel
import com.proyecto.cocinacapital.viewmodel.LoginViewModel
import com.proyecto.cocinacapital.viewmodel.RegistroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocinaCapitalTheme {
                val context = LocalContext.current
                val db = remember { AppDatabase.getDatabase(context) }
                val usuarioRepository = remember { UsuarioRepository(db.usuarioDao()) }
                val registroViewModel = remember { RegistroViewModel(usuarioRepository) }
                val loginViewModel = remember { LoginViewModel(usuarioRepository) }
                val inicioViewModel = remember { InicioViewModel() }

                AppNavHost(
                    registroViewModel = registroViewModel,
                    loginViewModel = loginViewModel,
                    inicioViewModel = inicioViewModel
                )
            }
        }
    }
}

