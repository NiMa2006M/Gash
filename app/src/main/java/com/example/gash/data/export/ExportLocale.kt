package com.example.gash.data.export

import java.util.Locale

private val RTL_LANGUAGES = setOf("fa", "ar", "ur", "he")

fun Locale.isRightToLeft(): Boolean = language in RTL_LANGUAGES