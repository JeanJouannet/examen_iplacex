package com.example.examen_iplacex

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.examen_iplacex.db.AppDatabase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapUI(appVM: AppVM, formVM: FormVM, permissionLauncher: ActivityResultLauncher<Array<String>>) {
    val routineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var lonText by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf("") }

    val lon = lonText.toDoubleOrNull() ?: 0.0
    val lat = latText.toDoubleOrNull() ?: 0.0

    Column {
        permissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
        TextField(
            value = lonText,
            onValueChange = {
                if (it.matches(Regex("^-?\\d*\\.?\\d*$")) || it.isEmpty()) {
                    lonText = it
                }
            },
            label = { Text(text = "Longitud") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = ImeAction.Done)
        )

        TextField(
            value = latText,
            onValueChange = {
                if (it.matches(Regex("^-?\\d*\\.?\\d*$")) || it.isEmpty()) {
                    latText = it
                }
            },
            label = { Text(text = "Latitud") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = ImeAction.Done)
        )

        Button(onClick = {

            routineScope.launch(Dispatchers.IO) {
                AppDatabase.getInstace(context).placeDao().updateLatLon(formVM.id.value, lat, lon)

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Lugar agregado", Toast.LENGTH_SHORT).show()
                }
            }

        }) {
            Text(text = "Guardar estas coordenadas al lugar")
        }
        Text(text = "Lat: $lat Lon: $lon")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { appVM.currentScreen.value = Screen.Form }) {
            Text(text = "Volver")
        }
        Spacer(modifier = Modifier.height(200.dp))
        AndroidView(factory = {
            MapView(it).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                org.osmdroid.config.Configuration.getInstance().userAgentValue = context.packageName
                controller.setZoom(15.0)
            }
        }, update = {

            it.overlays.removeIf { true }
            it.invalidate()
            val geoPoint = GeoPoint(lat, lon)
            it.controller.animateTo(geoPoint)

            val marcador = Marker(it)
            marcador.position = geoPoint
            marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            it.overlays.add(marcador)

        })
    }
}





fun getLocation(context: Context, onSuccess: (location: Location?) -> Unit) {
    try {
        val service = LocationServices.getFusedLocationProviderClient(context)
        val task = service.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        )
        task.addOnSuccessListener { location ->
            Log.d("Location", "Location retrieved: $location")
            onSuccess(location)
        }
        task.addOnFailureListener { exception ->
            Log.e("Location", "Failed to retrieve location: $exception")
            onSuccess(null)
        }
    } catch (e: SecurityException) {
        Log.e("Location", "Failed to retrieve location: $e")
        onSuccess(null)
    }
}