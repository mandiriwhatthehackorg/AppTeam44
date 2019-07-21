package id.co.bankmandiri.common.view

import android.app.Activity
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Helper class to show custom alert
 *
 * @author hendrawd on 10/19/16
 */
object Alert {
    private var toast: Toast? = null

    fun showToast(context: Context, message: String) {
        try {
            // make sure that the code will run on ui thread
            CoroutineScope(Dispatchers.Main).launch {
                if (toast != null) {
                    toast!!.cancel()
                }
                toast = Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG)
                // customize your toast here
                //        LinearLayout toastLayout = (LinearLayout) toast.getView();
                //        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                //        toastTV.setTextSize(20);
                //        toastTV.setGravity(Gravity.CENTER);
                //        toast.setGravity(Gravity.CENTER, 0, 0);
                toast!!.show()
            }
        } catch (e: Exception) {
            Timber.e(e, "Show Toast failed")
        }

    }

    fun showAlertDialog(context: Context, message: String) {
        if (context !is Activity)
            throw IllegalStateException("Context must be an Activity")
        try {
            android.app.AlertDialog.Builder(context)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show()
        } catch (e: Exception) {
            // in case context is not present / destroyed, etc
            Timber.e(e, "Show AlertDialog failed")
        }

    }

    fun showAlertDialogCompat(context: Context, message: String) {
        if (context !is Activity)
            throw IllegalStateException("Context must be an Activity")
        try {
            androidx.appcompat.app.AlertDialog.Builder(context)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show()
        } catch (e: Exception) {
            // in case context is not present / destroyed, etc
            Timber.e(e, "Show AlertDialogCompat failed")
        }

    }
}
