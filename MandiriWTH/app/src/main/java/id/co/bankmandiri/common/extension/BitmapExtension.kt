package id.co.bankmandiri.common.extension

import android.graphics.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.min

/**
 * @author hendrawd on 27 Jan 2019
 */
infix fun Bitmap.border(borderSize: Float): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val radius = min(height / 2, width / 2).toFloat()
    val output =
        Bitmap.createBitmap(width + borderOffset, height + borderOffset, Bitmap.Config.ARGB_8888)
    val paint = Paint()
    val borderX = width / 2 + borderSize
    val borderY = height / 2 + borderSize
    val canvas = Canvas(output)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.style = Paint.Style.FILL
    canvas.drawCircle(borderX, borderY, radius, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)
    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = Color.WHITE
    paint.strokeWidth = borderSize
    canvas.drawCircle(borderX, borderY, radius, paint)
    return output
}

infix fun Bitmap.rotate(degrees: Number): Bitmap? {
    return Bitmap.createBitmap(
        this,
        0,
        0,
        width,
        height,
        Matrix().apply { postRotate(degrees.toFloat()) },
        true
    )
}

infix fun Bitmap.writeTo(file: File) {
    try {
        FileOutputStream(file).use { out ->
            compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

infix fun Bitmap.resize(maximumSize: Int): Bitmap? {
    return resize(maximumSize, maximumSize)
}

fun Bitmap.resize(desiredWidth: Int, desiredHeight: Int): Bitmap? {
    var scaledBitmap: Bitmap? = null
    try {
        if (!(width <= desiredWidth && height <= desiredHeight)) {
            // Scale image
            scaledBitmap = ScalingUtilities.createScaledBitmap(
                this,
                desiredWidth,
                desiredHeight,
                ScalingUtilities.ScalingLogic.FIT
            )
        } else {
            return this
        }
    } catch (e: Throwable) {
    }
    return scaledBitmap
}