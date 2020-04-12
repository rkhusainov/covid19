package com.github.rkhusainov.covid19.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @param format паттерн в который нужно спарсить дату
 */
fun String.dateInFormat(format: String): String? {
    val oldFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val newFormat = SimpleDateFormat(format, Locale.getDefault())
    var parsedDate: String? = null
    try {
        parsedDate = newFormat.format(oldFormat.parse(this)!!)
    } catch (ignored: ParseException) {
        ignored.printStackTrace()
    }
    return parsedDate
}

/**
 * Паттерн: dd/MM/yyyy
 */
fun Date.formatToViewDateDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}
