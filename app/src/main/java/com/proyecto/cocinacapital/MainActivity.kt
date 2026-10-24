package com.proyecto.cocinacapital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.proyecto.cocinacapital.model.AppDatabase
import com.proyecto.cocinacapital.repository.PromocionRepository
import com.proyecto.cocinacapital.repository.TicketRepository
import com.proyecto.cocinacapital.repository.UsuarioRepository
import com.proyecto.cocinacapital.ui.theme.CocinaCapitalTheme
import com.proyecto.cocinacapital.navigation.AppNavHost
import com.proyecto.cocinacapital.viewmodel.AdminViewModel
import com.proyecto.cocinacapital.viewmodel.InicioViewModel
import com.proyecto.cocinacapital.viewmodel.LoginViewModel
import com.proyecto.cocinacapital.viewmodel.RegistroViewModel
import com.proyecto.cocinacapital.viewmodel.SesionViewModel
import com.proyecto.cocinacapital.viewmodel.TicketViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocinaCapitalTheme {
                val context = LocalContext.current
                val db = remember { AppDatabase.getDatabase(context) }
                val usuarioRepository = remember { UsuarioRepository(db.usuarioDao()) }
                val promocionRepository = remember { PromocionRepository(db.promocionDao()) }
                val ticketRepository = remember { TicketRepository(db.ticketDao()) }
                val sesionViewModel = remember { SesionViewModel() }
                val registroViewModel = remember { RegistroViewModel(usuarioRepository) }
                val loginViewModel = remember { LoginViewModel(usuarioRepository, sesionViewModel) }
                val inicioViewModel = remember {
                    InicioViewModel(promocionRepository, ticketRepository, sesionViewModel)
                }
                val ticketViewModel = remember {
                    TicketViewModel(ticketRepository, promocionRepository, sesionViewModel)
                }
                val adminViewModel = remember {
                    AdminViewModel(promocionRepository, sesionViewModel)
                }

                AppNavHost(
                    registroViewModel = registroViewModel,
                    loginViewModel = loginViewModel,
                    inicioViewModel = inicioViewModel,
                    ticketViewModel = ticketViewModel,
                    adminViewModel = adminViewModel,
                    sesionViewModel = sesionViewModel
                )
            }
        }
    }
}

