package id.co.bankmandiri.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.co.bankmandiri.common.api.model.VideoCallLinkResponse

/**
 * @author hendrawd on 24 Mar 2019
 */
class VideoCallViewModel(application: Application) : AndroidViewModel(application) {
    val isRequestingVideoCallLink = MutableLiveData<Boolean>().apply { value = false }
    val videoCallLinkResponseObservable: MutableLiveData<VideoCallLinkResponse> =
        MutableLiveData()

    fun videoCallLink(emailAddress: String) {
        if (isRequestingVideoCallLink.value!!) {
            return
        }
        isRequestingVideoCallLink.postValue(true)

        // TODO get video link from server api
//        GlobalScope.launch {
//            val serverResponseString = CustomerEndPoint
//                .videoCallLink()
//                .postData("{\"email\": \"$emailAddress\"}")
//            try {
//                if (!serverResponseString.isNullOrEmpty()) {
//                    val serverResponse =
//                        serverResponseString.toKotlinObject<VideoCallLinkResponse>()
//                    videoCallLinkResponseObservable.postValue(serverResponse)
//                }
//            } catch (exception: Exception) {
//                videoCallLinkResponseObservable.postValue(null)
//            }
//
//            isRequestingVideoCallLink.postValue(false)
//        }
    }
}