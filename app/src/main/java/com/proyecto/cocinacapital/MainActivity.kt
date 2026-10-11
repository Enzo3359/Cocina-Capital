package com.proyecto.cocinacapital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.proyecto.cocinacapital.ui.theme.CocinaCapitalTheme
import com.proyecto.cocinacapital.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocinaCapitalTheme {
                AppNavHost()
            }
        }
    }
}

