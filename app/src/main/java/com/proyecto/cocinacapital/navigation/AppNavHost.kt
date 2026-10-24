package com.proyecto.cocinacapital.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.cocinacapital.view.screen.PantallaAdmin
import com.proyecto.cocinacapital.view.screen.PantallaInicio
import com.proyecto.cocinacapital.view.screen.PantallaLogin
import com.proyecto.cocinacapital.view.screen.PantallaMisTickets
import com.proyecto.cocinacapital.view.screen.PantallaPersonal
import com.proyecto.cocinacapital.view.screen.PantallaRegistro
import com.proyecto.cocinacapital.viewmodel.AdminViewModel
import com.proyecto.cocinacapital.viewmodel.InicioViewModel
import com.proyecto.cocinacapital.viewmodel.LoginViewModel
import com.proyecto.cocinacapital.viewmodel.RegistroViewModel
import com.proyecto.cocinacapital.viewmodel.SesionViewModel
import com.proyecto.cocinacapital.viewmodel.TicketViewModel

@Composable
fun AppNavHost(
    registroViewModel: RegistroViewModel,
    loginViewModel: LoginViewModel,
    inicioViewModel: InicioViewModel,
    ticketViewModel: TicketViewModel,
    adminViewModel: AdminViewModel,
    sesionViewModel: SesionViewModel
) {
    val navController  = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {

        composable(Rutas.REGISTRO) { PantallaRegistro(navController, registroViewModel) }
        composable(Rutas.LOGIN) { PantallaLogin(navController, loginViewModel) }
        composable(Rutas.INICIO) { PantallaInicio(navController, inicioViewModel) }
        composable(Rutas.MIS_TICKETS) { PantallaMisTickets(navController, ticketViewModel) }
        composable(Rutas.ADMIN) { PantallaAdmin(navController, adminViewModel) }
        composable(Rutas.PERSONAL) { PantallaPersonal(navController, sesionViewModel) }
    }
}