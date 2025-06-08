package com.oliver.siloker.domain.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtil {

    fun formatIsoToString(
        isoString: String,
        pattern: String = "EEEE, MMMM d, yyyy 'at' hh:mm a",
        locale: Locale = Locale.getDefault()
    ): String {
        if (isoString.isEmpty()) return "-"
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        val parsed = LocalDateTime.parse(isoString)
        return parsed.format(formatter)
    }
}