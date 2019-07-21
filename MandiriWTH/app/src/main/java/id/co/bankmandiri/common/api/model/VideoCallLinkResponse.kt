package id.co.bankmandiri.common.api.model

import com.google.gson.annotations.SerializedName

data class VideoCallLinkResponse(
    @SerializedName("room_link")
    val roomLink: String,
    @SerializedName("room_name")
    val roomName: String,
    @SerializedName("access_token")
    val accessToken: String
)