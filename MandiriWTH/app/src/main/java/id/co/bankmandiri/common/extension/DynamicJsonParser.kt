package id.co.bankmandiri.common.extension

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/**
 * Laravel form error using dynamic key-value json,
 * must create custom parser/custom gson deserializer to parse it
 * Based on https://stackoverflow.com/a/22855546/3940133 untested
 *
 * @author hendrawd on 03 Apr 2019
 */
fun JSONObject.parseJson() {
    val it = keys()
    while (it.hasNext()) {
        val key = it.next()
        try {
            when {
                get(key) is JSONArray -> {
                    val jsonArray = getJSONArray(key)
                    val size = jsonArray.length()
                    for (i in 0 until size) {
                        jsonArray.getJSONObject(i).parseJson()
                    }
                }
                get(key) is JSONObject -> getJSONObject(key).parseJson()
                else -> Log.e("ParseJsonFirst", key + ":" + getString(key))
            }
        } catch (e: Throwable) {
            try {
                Log.e("ParseJsonSecond", key + ":" + getString(key))
            } catch (ee: Exception) {
            }
            e.printStackTrace()
        }
    }
}

fun String.stripOffJunk(): String {
    var returnValue = this
    returnValue = returnValue.removePrefix("[\"")
    returnValue = returnValue.removeSuffix("\"]")
    return returnValue
}

fun JSONObject.getFirstErrorOrNull(): String? {
    val it = keys()
    while (it.hasNext()) {
        val key = it.next()
        try {
            when (get(key)) {
                is JSONArray -> {
                    val jsonArray = getJSONArray(key)
                    val size = jsonArray.length()
                    for (i in 0 until size) {
                        return jsonArray.getJSONObject(i).getFirstErrorOrNull()
                    }
                }
                is JSONObject -> return getJSONObject(key).getFirstErrorOrNull()
                else -> return getString(key)
            }
        } catch (e: Throwable) {
            try {
                return getString(key)
            } catch (ee: Exception) {
            }
            e.printStackTrace()
        }
    }
    return null
}