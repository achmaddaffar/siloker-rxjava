package com.oliver.siloker.presentation.util

import android.content.Context
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.rx.R

object ErrorMessageUtil {

    fun NetworkError?.parseNetworkError(context: Context): String {
        return context.getString(
            when (this) {
                NetworkError.SERVER_ERROR -> R.string.error_server
                NetworkError.BAD_REQUEST -> R.string.error_bad_request
                NetworkError.NO_INTERNET -> R.string.error_no_internet
                NetworkError.UNAUTHORIZED -> R.string.error_unauthorized
                NetworkError.PAYMENT_REQUIRED -> R.string.error_payment_required
                NetworkError.FORBIDDEN -> R.string.error_forbidden
                NetworkError.NOT_FOUND -> R.string.error_not_found
                NetworkError.METHOD_NOT_ANSWERED -> R.string.error_method_not_answered
                NetworkError.NOT_ACCEPTABLE -> R.string.error_not_acceptable
                NetworkError.PROXY_AUTHENTICATION_REQUIRED -> R.string.error_proxy_authentication_required
                NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
                NetworkError.CONFLICT -> R.string.error_conflict
                NetworkError.GONE -> R.string.error_gone
                NetworkError.LENGTH_REQUIRED -> R.string.error_length_required
                NetworkError.PRECONDITION_FAILED -> R.string.error_precondition_failed
                NetworkError.PAYLOAD_TOO_LARGE -> R.string.error_payload_too_large
                NetworkError.URI_TOO_LONG -> R.string.error_uri_too_long
                NetworkError.UNSUPPORTED_MEDIA_TYPE -> R.string.error_unsupported_media_type
                NetworkError.RANGE_NOT_SATISFIABLE -> R.string.error_range_not_satisfiable
                NetworkError.EXPECTATION_FAILED -> R.string.error_expectation_failed
                NetworkError.MISDIRECTED_REQUEST -> R.string.error_misdirected_request
                NetworkError.TOO_MANY_REQUEST -> R.string.error_too_many_requests
                NetworkError.UNKNOWN -> R.string.error_unknown
                null -> R.string.error_unknown
            }
        )
    }
}