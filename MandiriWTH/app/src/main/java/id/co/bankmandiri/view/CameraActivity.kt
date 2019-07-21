package id.co.bankmandiri.view

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.bankmandiri.R
import id.co.bankmandiri.common.extension.*
import id.co.bankmandiri.common.view.Alert
import id.co.bankmandiri.common.view.CardID1BorderView
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.UpdateConfiguration
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

const val RC_CAMERA = 1212

/**
 * @author hendrawd on 02 Apr 2019
 */
class CameraActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    lateinit var fotoapparat: Fotoapparat
    var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        loadingLayer.gone()

        fotoapparat = Fotoapparat(
                context = this,
                view = cameraView,                   // view which will draw the camera preview
                scaleType = ScaleType.CenterInside,    // (optional) we want the preview to fill the view
                lensPosition = back(),               // (optional) we want back camera
                logger = loggers(                    // (optional) we want to log camera events in 2 places at once
                        logcat(),                   // ... in logcat
                        fileLogger(this)            // ... and to file
                ),
                cameraErrorCallback = { error ->
                    Alert.showToast(this@CameraActivity, "Terjadi kesalahan")
                    Timber.e(error.message)
                }   // (optional) log fatal errors
        )
        bTakePicture.setOnClickListener {
            val photoResult = fotoapparat.takePicture()
            photoResult
                    .toBitmap()
                    .whenAvailable { bitmapPhoto ->
                        bitmapPhoto!!.bitmap.let {
                            loadingLayer.visible()
                            val resizedBitmap = (it resize 720)!! // no need for full size bitmap
                            val rotatedBitmap = (resizedBitmap rotate -bitmapPhoto.rotationDegrees)!!
//                        ivPreview.visible()
//                        ivPreview.setImageBitmap(rotatedBitmap)
//                        val snapshot = ivPreview.getBitmap(ivPreview.width, ivPreview.height)
//                        val snapshot = Util.loadBitmapFromView(ivPreview, ivPreview.width, ivPreview.height)
//
                            val newHeight = rotatedBitmap.width * CardID1BorderView.heightToWidthRatio
                            val newY = rotatedBitmap.height / 2 - newHeight / 2
                            val ktpBitmap = Bitmap.createBitmap(
                                    rotatedBitmap,
                                    0,
                                    newY.toInt(),
                                    rotatedBitmap.width,
                                    newHeight.toInt()
                            )
                            killActivity(ktpBitmap)
//                        ivPreview.setImageBitmap(ktpBitmap)
//                        ivPreview.rotation = (-bitmapPhoto.rotationDegrees).toFloat()
//                        ivPreview.visible()
                        }
                    }
        }
        bTorch.setOnClickListener {
            isChecked = !isChecked
            fotoapparat.updateConfiguration(
                    UpdateConfiguration(
                            flashMode = if (isChecked) torch() else off()
                    )
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Alert.showToast(
                this,
                "Maaf, Anda harus mengijinkan aplikasi mengakses kamera untuk melanjutkan"
        )
        killActivity(null)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // no need, because handled directly by @AfterPermissionGranted(RC_CAMERA)
    }

    @AfterPermissionGranted(RC_CAMERA)
    private fun tryToStartCamera() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Already have permission, do the thing
            GlobalScope.launch(Dispatchers.Main) {
                // add delay to make sure fotoapparat run
                // error: CameraActivity$onCreate: Failed to open camera with lens position: LensPosition.Back and id: 0
                // https://github.com/RedApparat/Fotoapparat/issues/311
                // https://github.com/RedApparat/Fotoapparat/issues/135
                // https://github.com/RedApparat/Fotoapparat/issues/191
                delay(200)
                fotoapparat.start()
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    this, getString(R.string.camera_rationale),
                    RC_CAMERA, Manifest.permission.CAMERA
            )
        }
    }

    override fun onStart() {
        super.onStart()
        tryToStartCamera()
    }

    override fun onStop() {
        super.onStop()
        try {
            fotoapparat.stop()
        } catch (exception: Exception) {
        }
    }

    private fun killActivity(bitmap: Bitmap?) {
        if (bitmap == null) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            val tempKtpFile = tempFile("ktp", "ktp.png")
            bitmap writeTo tempKtpFile
            intent.putExtra("fileAbsolutePath", tempKtpFile.absolutePath)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }
}