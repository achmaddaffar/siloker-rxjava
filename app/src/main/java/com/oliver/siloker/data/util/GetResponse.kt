package com.oliver.siloker.data.util

import android.util.Log
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.rx.BuildConfig
import retrofit2.Response

suspend inline fun <reified T> getResponse(
    crossinline block: suspend () -> Response<T>
): Result<T, NetworkError> {
    try {
        val res = block()

        when (res.code()) {
            400 -> return Result.Error(NetworkError.BAD_REQUEST)
            401 -> return Result.Error(NetworkError.UNAUTHORIZED)
            402 -> return Result.Error(NetworkError.PAYMENT_REQUIRED)
            403 -> return Result.Error(NetworkError.FORBIDDEN)
            404 -> return Result.Error(NetworkError.NOT_FOUND)
            405 -> return Result.Error(NetworkError.METHOD_NOT_ANSWERED)
            406 -> return Result.Error(NetworkError.NOT_ACCEPTABLE)
            407 -> return Result.Error(NetworkError.PROXY_AUTHENTICATION_REQUIRED)
            408 -> return Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> return Result.Error(NetworkError.CONFLICT)
            410 -> return Result.Error(NetworkError.GONE)
            411 -> return Result.Error(NetworkError.LENGTH_REQUIRED)
            412 -> return Result.Error(NetworkError.PRECONDITION_FAILED)
            413 -> return Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            414 -> return Result.Error(NetworkError.URI_TOO_LONG)
            415 -> return Result.Error(NetworkError.UNSUPPORTED_MEDIA_TYPE)
            416 -> return Result.Error(NetworkError.RANGE_NOT_SATISFIABLE)
            417 -> return Result.Error(NetworkError.EXPECTATION_FAILED)
            421 -> return Result.Error(NetworkError.MISDIRECTED_REQUEST)
            429 -> return Result.Error(NetworkError.TOO_MANY_REQUEST)
            500 -> return Result.Error(NetworkError.UNKNOWN)
        }

        return Result.Success(res.body() ?: throw NullPointerException())
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) Log.e("okht", e.message.toString())
        return Result.Error(NetworkError.UNKNOWN)
    }
}

fun <T> getResponseRaw(response: Response<T>): Result<T, NetworkError> {
    return if (response.isSuccessful) {
        val body = response.body()
        if (body != null) Result.Success(body)
        else Result.Error(NetworkError.UNKNOWN)
    } else {
        when (response.code()) {
            400 -> Result.Error(NetworkError.BAD_REQUEST)
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            402 -> Result.Error(NetworkError.PAYMENT_REQUIRED)
            403 -> Result.Error(NetworkError.FORBIDDEN)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            405 -> Result.Error(NetworkError.METHOD_NOT_ANSWERED)
            406 -> Result.Error(NetworkError.NOT_ACCEPTABLE)
            407 -> Result.Error(NetworkError.PROXY_AUTHENTICATION_REQUIRED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            410 -> Result.Error(NetworkError.GONE)
            411 -> Result.Error(NetworkError.LENGTH_REQUIRED)
            412 -> Result.Error(NetworkError.PRECONDITION_FAILED)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            414 -> Result.Error(NetworkError.URI_TOO_LONG)
            415 -> Result.Error(NetworkError.UNSUPPORTED_MEDIA_TYPE)
            416 -> Result.Error(NetworkError.RANGE_NOT_SATISFIABLE)
            417 -> Result.Error(NetworkError.EXPECTATION_FAILED)
            421 -> Result.Error(NetworkError.MISDIRECTED_REQUEST)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUEST)
            500 -> Result.Error(NetworkError.UNKNOWN)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}
