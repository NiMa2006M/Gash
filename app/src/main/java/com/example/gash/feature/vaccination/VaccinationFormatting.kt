package com.example.gash.feature.vaccination

import androidx.annotation.StringRes
import com.example.gash.R
import com.example.gash.domain.model.InjectionSite

@StringRes
fun InjectionSite.labelRes(): Int = when (this) {
    InjectionSite.INTRAMUSCULAR -> R.string.vaccination_site_intramuscular
    InjectionSite.SUBCUTANEOUS -> R.string.vaccination_site_subcutaneous
    InjectionSite.INTRAVENOUS -> R.string.vaccination_site_intravenous
}

fun formatDose(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()