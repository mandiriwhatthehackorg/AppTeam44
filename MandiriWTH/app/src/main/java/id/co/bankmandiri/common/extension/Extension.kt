package id.co.bankmandiri.common.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import id.co.bankmandiri.App
import id.co.bankmandiri.common.view.Alert
import java.io.File
import java.util.regex.Pattern

const val REGEX_DIGIT = "\\d+"

@Suppress("DEPRECATION")
fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String.openUrl(context: Context) {
    try {
        val url = if (!startsWith("http://") && !startsWith("https://")) {
            "http://$this"
        } else {
            this
        }
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (exception: Exception) {
        Alert.showToast(context, "Can't open url!")
    }
}

fun String.isPackageInstalled(packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(this, PackageManager.GET_ACTIVITIES)
        true
    } catch (exception: PackageManager.NameNotFoundException) {
        false
    }
}

fun String.isYoutubeLink(): Boolean {
    return contains("youtube.com") || contains("youtu.be")
}

fun String.youtubeLinkToYoutubeKey(): String? {
    if (contains("youtube.com")) {
        if (contains("watch?v=")) {
            return substringAfterLast("watch?v=")
        } else if (contains("embed/")) {
            return substringAfterLast("embed/")
        }
    } else if (contains("youtu.be")) {
        return substringAfterLast("youtu.be/")
    }
    return null
}

/**
 * Get youtube thumbnail preview of the video
 *
 * @param videoId key of youtube video
 * @return thumbnail url
 *
 *
 * It is possible to change the resolution of the thumbnail, but beware of unavailable thumb
 * we can use:
 * default.jpg
 * hqdefault.jpg
 * mqdefault.jpg
 * sddefault.jpg
 * maxresdefault.jpg
 */
fun String.youtubeKeyToThumbnailUrl(): String {
    return "https://img.youtube.com/vi/$this/default.jpg"
}

fun String.toActionViewIntent(): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse(this))
}

fun Context.openGooglePlayOfThisApplication() {
    try {
        startActivity("market://details?id=$packageName".toActionViewIntent())
    } catch (activityNotFoundException: ActivityNotFoundException) {
        startActivity("https://play.google.com/store/apps/details?id=$packageName".toActionViewIntent())
    }
}

// extension property
val String.hasNumber: Boolean
    get() = this.contains(REGEX_DIGIT.toRegex())

fun String.parseColor(): Int {
    return Color.parseColor(this)
}

fun String.getPositiveDigits(): List<String> {
    val positiveDigits = mutableListOf<String>()
    val pattern = Pattern.compile(REGEX_DIGIT)
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        positiveDigits.add(matcher.group())
    }
    return positiveDigits
}

fun Any.toJsonString(): String {
    return Gson().toJson(this).toString()
}

inline fun <reified T> String.toKotlinObject(): T {
    return Gson().fromJson(this, T::class.java)
}

inline fun <reified T> String.toKotlinMutableList(): MutableList<T> {
    return toKotlinObject<Array<T>>().toMutableList()
}

inline fun <reified T> isNot(any: Any, type: T) = any !is T

fun MutableLiveData<Boolean>.observeLoading(
        lifecycleOwner: LifecycleOwner,
        loadingLayout: View,
        vararg otherLoadingLiveData: LiveData<Boolean>
) {
    observe(
            lifecycleOwner,
            Observer {
                loadingLayout.apply {
                    if (it == null) {
                        gone()
                    } else {
                        if (it) {
                            visible()
                        } else {
                            for (otherStillLoading in otherLoadingLiveData) {
                                if (otherStillLoading.value!!) {
                                    return@Observer
                                }
                            }
                            gone()
                        }
                    }
                }
            }
    )
}

fun tempFile(directoryName: String, fileName: String): File {
    // Store to tmp file inside the application files directory
    val tempDirectory = File(
            App.INSTANCE.filesDir,
            directoryName
    )
    if (!tempDirectory.exists()) {
        tempDirectory.mkdir()
    }
    return File(tempDirectory.absolutePath, fileName)
}