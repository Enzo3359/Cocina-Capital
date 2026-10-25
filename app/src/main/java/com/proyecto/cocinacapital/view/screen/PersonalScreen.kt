package com.proyecto.cocinacapital.view.screen

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.viewmodel.ValidarTicketViewModel

@Composable
fun PantallaPersonal(
    navController: NavController,
    viewModel: ValidarTicketViewModel
) {
    val context = LocalContext.current
    val resultado by viewModel.resultado.collectAsState()

    val opciones = remember {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
    val scanner = remember { GmsBarcodeScanning.getClient(context as Activity, opciones) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text("Validación de tickets", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        viewModel.validarCodigo(barcode.rawValue ?: "")
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Escanear ticket")
        }

        resultado?.let {
            Text(
                it.mensaje,
                style = MaterialTheme.typography.bodyLarge,
                color = if (it.valido) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion()
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(Rutas.PERSONAL) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
