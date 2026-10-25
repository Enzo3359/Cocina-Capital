package com.proyecto.cocinacapital.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun obtenerUbicacion(context: Context, onUbicacion: (Double, Double) -> Unit) {
    val cliente = LocationServices.getFusedLocationProviderClient(context)
    cliente.lastLocation.addOnSuccessListener { ubicacion ->
        if (ubicacion != null) {
            onUbicacion(ubicacion.latitude, ubicacion.longitude)
        }
    }
}
