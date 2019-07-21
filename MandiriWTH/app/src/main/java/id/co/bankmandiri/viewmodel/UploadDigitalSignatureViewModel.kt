package id.co.bankmandiri.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.co.bankmandiri.common.api.model.BaseResponse
import id.co.bankmandiri.common.view.Alert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author hendrawd on 24 Mar 2019
 */
class UploadDigitalSignatureViewModel(application: Application) : AndroidViewModel(application) {
    val hasOngoingRequest = MutableLiveData<Boolean>().apply { value = false }
    val uploadDigitalSignatureResponseObservable: MutableLiveData<BaseResponse> =
        MutableLiveData()

    fun uploadDigitalSignature(emailAddress: String, file: File) {
        val action = "pembaharuan data"
        if (hasOngoingRequest.value!!) {
            "Sedang melakukan $action, silakan tunggu...".lt()
            return
        }
        hasOngoingRequest.postValue(true)

        GlobalScope.launch {
//            val dataMap = mapOf(
//                "email" to emailAddress,
//                "step" to RegisterStep.UPLOAD_DIGITAL_SIGNATURE,
//                "sign" to file
//            )
//            val serverResponseString = CustomerEndPoint
//                .uploadDigitalSignature()
//                .postDataMultipart(dataMap.toMultipartBody())
//            try {
//                if (!serverResponseString.isNullOrEmpty()) {
//                    val serverResponse =
//                        serverResponseString.toKotlinObject<BaseResponse>()
//                    if (!serverResponse.status) {
//                        if (serverResponse.message == null) {
//                            "$action gagal".lt()
//                        } else {
//                            serverResponse.message.lt()
//                        }
//                    } else if (serverResponse.status) {
//                        "$action berhasil".lt()
//                        uploadDigitalSignatureResponseObservable.postValue(serverResponse)
//                    }
//                }
//            } catch (exception: Exception) {
//                "$action gagal - ${exception.message}".lt()
//            }
//
//            hasOngoingRequest.postValue(false)
        }
    }

    private fun String.lt() {
        CoroutineScope(Dispatchers.Main).launch {
            Alert.showToast(getApplication(), this@lt)
        }
    }
}