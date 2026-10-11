package com.proyecto.cocinacapital.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AzulPrimarioOscuro,
    onPrimary = OnAzulOscuro,
    secondary = CelesteSecundarioOscuro,
    onSecondary = OnCelesteOscuro,
    tertiary = VerdeAcentoOscuro,
    onTertiary = OnVerdeOscuro,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = FondoOscuro,
    onSurface = TextoClaro
)

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = OnAzul,
    secondary = CelesteSecundario,
    onSecondary = OnCeleste,
    tertiary = VerdeAcento,
    onTertiary = OnVerde,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = FondoClaro,
    onSurface = TextoOscuro
)

@Composable
fun CocinaCapitalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}