package com.example.gash.core.time

import android.icu.text.DateFormat
import android.icu.util.ULocale
import java.util.Date
import java.util.Locale

private fun uLocaleFor(locale: Locale): ULocale =
    if (locale.language == "fa") ULocale("fa_IR@calendar=persian") else ULocale.forLocale(locale)

fun formatDateTime(millis: Long, locale: Locale): String =
    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, uLocaleFor(locale))
        .format(Date(millis))

fun formatTime(millis: Long, locale: Locale): String =
    DateFormat.getTimeInstance(DateFormat.SHORT, uLocaleFor(locale)).format(Date(millis))