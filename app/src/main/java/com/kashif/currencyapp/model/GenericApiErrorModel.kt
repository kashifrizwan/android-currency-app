package com.kashif.currencyapp.model

import com.google.gson.annotations.SerializedName

data class GenericApiErrorModel(
    @SerializedName("message")
    val message: String = ""
)
