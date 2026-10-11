package com.proyecto.cocinacapital.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.cocinacapital.view.screen.PantallaInicio
import com.proyecto.cocinacapital.view.screen.PantallaLogin

@Composable
fun AppNavHost(){
    val navController  = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN) { PantallaLogin(navController) }
        composable(Rutas.INICIO) { PantallaInicio(navController) }
    }
}