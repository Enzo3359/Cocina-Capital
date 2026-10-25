package com.proyecto.cocinacapital.view.screen

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
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
    var codigoIngresado by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Validación de tickets") },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.cerrarSesion()
                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.PERSONAL) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Button(
                onClick = {
                    scanner.startScan()
                        .addOnSuccessListener { barcode ->
                            viewModel.validarCodigo(barcode.rawValue ?: "")
                        }
                        .addOnFailureListener {
                            viewModel.errorEscaneo()
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escanear ticket")
            }

            Text(
                "o ingresá el código manualmente (útil si no hay cámara, por ejemplo desde un PC)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = codigoIngresado,
                onValueChange = { codigoIngresado = it },
                label = { Text("Código del ticket") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = {
                    viewModel.validarCodigo(codigoIngresado)
                    codigoIngresado = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Validar código")
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
        }
    }
}
