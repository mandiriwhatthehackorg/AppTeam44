package id.co.bankmandiri.common.extension

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import id.co.bankmandiri.BuildConfig

/**
 * @author hendrawd on 24 Mar 2019
 */
object PreferenceKey {
    val EMAIL_ADDRESS = "email_address"
    val TASKS_UNIQUE_ID = "unique_id"
    val USER_DETAIL = "user_detail"
}

object DefaultValue {
    val EMAIL_ADDRESS = if (BuildConfig.DEBUG) "hendrawd88@gmail.com" else ""
    val TASKS_UNIQUE_ID = null
    val USER_DETAIL = null
}

fun Context.pref() = PreferenceManager.getDefaultSharedPreferences(this)!!

fun Context.saveEmailAddress(emailAddress: String) {
    pref().edit {
        putString(
            PreferenceKey.EMAIL_ADDRESS,
            emailAddress
        )
    }
}

fun Context.saveTasksUniqueId(uniqueId: String) {
    pref().edit {
        putString(
            PreferenceKey.TASKS_UNIQUE_ID,
            uniqueId
        )
    }
}

fun Context.saveUserDetail(userDetail: String) {
    pref().edit {
        putString(
            PreferenceKey.USER_DETAIL,
            userDetail
        )
    }
}

fun Context.loadEmailAddress(): String =
    pref().getString(PreferenceKey.EMAIL_ADDRESS, DefaultValue.EMAIL_ADDRESS)!!

fun Context.loadTasksUniqueId(): String? =
    pref().getString(PreferenceKey.TASKS_UNIQUE_ID, DefaultValue.TASKS_UNIQUE_ID)

fun Context.loadUserDetail(): String? =
    pref().getString(PreferenceKey.USER_DETAIL, DefaultValue.USER_DETAIL)

fun Context.removeTasksUniqueId() = pref().edit {
    remove(PreferenceKey.TASKS_UNIQUE_ID)
}
