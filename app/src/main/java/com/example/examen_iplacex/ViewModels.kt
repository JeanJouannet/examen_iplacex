package com.example.examen_iplacex

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

enum class Screen {
    Form,
    Camera,
    Map,
    AddPlace,
    ModifyPlace
}
class AppVM: ViewModel() {
    val currentScreen = mutableStateOf(Screen.Form)
    val onCameraPermissionOk:() -> Unit = {}
    var locationPermissionOk:() -> Unit = {}
}

class FormVM: ViewModel() {
    val id = mutableStateOf(0)
    val placeVisited = mutableStateOf("")
    val photo = mutableStateOf<Bitmap?>(null)
    val lat = mutableStateOf(0.0);
    val lon = mutableStateOf(0.0);
    val order = mutableStateOf(0)
    val price = mutableStateOf(0.0)
    val movePrice = mutableStateOf(0.0)
    val comments = mutableStateOf("")
    val dolar = mutableStateOf(0.0)
}