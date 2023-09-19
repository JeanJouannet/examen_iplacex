package com.example.examen_iplacex

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.examen_iplacex.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CameraUI(cameraController: LifecycleCameraController, permissionLauncher: ActivityResultLauncher<Array<String>>,
             appVM: AppVM, formVM: FormVM) {
    permissionLauncher.launch(arrayOf(android.Manifest.permission.CAMERA))

    val context = LocalContext.current
    val routineScope = rememberCoroutineScope()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PreviewView(it).apply { controller = cameraController }
        })
    Button(
        onClick = {
            takePhoto(
                cameraController = cameraController,
                file = makePrivatePhotoFile(context),
                context = context)
            {
                routineScope.launch(Dispatchers.IO){
                    val dao = AppDatabase.getInstace(context).placeDao()
                    formVM.photo.value = it
                    dao.updateImgRef(formVM.id.value, it)
                }
                appVM.currentScreen.value = Screen.Form
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Tomar foto")
    }
}
fun takePhoto(
    cameraController: LifecycleCameraController,
    file: File,
    context: Context,
    onCaptureImage: (Bitmap) -> Unit // Cambiar de Uri a Bitmap
) {
    val options = ImageCapture.OutputFileOptions.Builder(file).build()

    cameraController.takePicture(
        options,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let { uri ->
                    // Cargar la imagen desde la Uri y convertirla en un Bitmap
                    val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                    bitmap?.let {
                        onCaptureImage(it)
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Error taking photo", exception)
            }
        }
    )
}

fun makePrivatePhotoFile(context: Context): File = File(
    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
    "${System.currentTimeMillis()}.jpg"
)
