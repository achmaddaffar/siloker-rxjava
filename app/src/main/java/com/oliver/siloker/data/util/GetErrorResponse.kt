package com.oliver.siloker.data.util

import com.google.gson.Gson
import com.oliver.siloker.data.network.model.response.BaseResponse
import okhttp3.ResponseBody

fun ResponseBody?.getErrorResponse(): BaseResponse<Any?>? {
    val gson = Gson()
    return gson.fromJson<BaseResponse<Any?>>(this?.string(), BaseResponse::class.java)
}