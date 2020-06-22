package com.teyyihan.rickandmorty.model

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("next")
    var next: String?,
    @SerializedName("pages")
    var pages: Int?,
    @SerializedName("prev")
    var prev: String?
)