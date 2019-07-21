package id.co.bankmandiri.common.api

import id.co.bankmandiri.BuildConfig.DEBUG
import id.co.bankmandiri.common.api.model.BaseResponse
import id.co.bankmandiri.common.extension.getFirstErrorOrNull
import id.co.bankmandiri.common.extension.stripOffJunk
import id.co.bankmandiri.common.extension.toJsonString
import okhttp3.*
import okhttp3.MediaType.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR

/**
 * TODO authentication mechanism
 *
 * @author hendrawd on 19/07/18
 */

private object ERROR {
    val MESSAGGE_422 = "Request tidak dapat diproses"
    val MESSAGGE_429 = "Terlalu banyak request"
    val MESSAGGE_500 = "Kesalahan internal server"
}

val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
private val OK_HTTP_LOGGING_INTERCEPTOR by lazy {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level =
        if (DEBUG) {
            BODY
        } else {
            NONE
        }
    loggingInterceptor
}
val OK_HTTP_CLIENT =
    OkHttpClient.Builder()
        .addInterceptor(ApiKeyAdderInterceptor())
        .addInterceptor(OK_HTTP_LOGGING_INTERCEPTOR)
        .build()

fun String.getDataAsString(tag: String? = null): String? {
    try {
        val request = Request.Builder()
            .url(this)
            .tag(tag ?: this)
            .build()
        return OK_HTTP_CLIENT.newCall(request).execute().handle()
    } catch (ioe: IOException) {
        ioe.printStackTrace()
    }
    return null
}

fun String.getDataAsJSONObject(tag: String): JSONObject? {
    val jsonString = getDataAsString(tag)
    if (jsonString != null) {
        try {
            return JSONObject(jsonString)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    return null
}

fun String.getData(callback: Callback) {
    val request = Request.Builder()
        .url(this)
        .build()
    OK_HTTP_CLIENT.newCall(request).enqueue(callback)
}

fun String.postData(data: Any, callback: Callback? = null): String? {
    val body = RequestBody.create(
        MEDIA_TYPE_JSON,
        if (data is String) {
            data
        } else {
            data.toJsonString()
        }
    )
    val request = Request.Builder()
        .url(this)
        .post(body)
        .build()
    if (callback == null) {
        try {
            return OK_HTTP_CLIENT.newCall(request).execute().handle()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    } else {
        OK_HTTP_CLIENT.newCall(request).enqueue(callback)
    }
    return null
}

fun String.postDataMultipart(multipartBody: MultipartBody, callback: Callback? = null): String? {
    val request = Request.Builder()
        .url(this)
        .post(multipartBody)
        .build()
    if (callback == null) {
        try {
            return OK_HTTP_CLIENT.newCall(request).execute().handle()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    } else {
        OK_HTTP_CLIENT.newCall(request).enqueue(callback)
    }
    return null
}

fun String.cancelRequestByTag() {
    for (call in OK_HTTP_CLIENT.dispatcher.queuedCalls()) {
        val tag = call.request().tag()
        if (tag != null && tag == this)
            call.cancel()
    }

    for (call in OK_HTTP_CLIENT.dispatcher.runningCalls()) {
        val tag = call.request().tag()
        if (tag != null && tag == this)
            call.cancel()
    }
}

private fun Response.handle(): String? {
    return when (code) {
        422 -> {
            val errorMessage =
                JSONObject(body?.string()).getFirstErrorOrNull()?.stripOffJunk()
            createErrorResponse(errorMessage ?: ERROR.MESSAGGE_422)
        }
//                HTTP_UNAUTHORIZED -> LoginManager.askForReLogin()
        429 -> createErrorResponse(ERROR.MESSAGGE_429)
        HTTP_INTERNAL_ERROR -> createErrorResponse(ERROR.MESSAGGE_500)
        else -> {
            // other response code also return error message to be shown to user
            body?.string()
        }
    }
}

private fun createErrorResponse(errorMessage: String): String {
    return BaseResponse(false, errorMessage).toJsonString()
}