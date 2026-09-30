package com.example.gash.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.gash.R

enum class HomeOperation(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int? = null
) {
    ULTRASOUND(
        labelRes = R.string.operation_ultrasound,
        iconRes = R.drawable.gash_home_icon_ultrasound
    ),

    VACCINATION(
        labelRes = R.string.operation_vaccination,
        iconRes = R.drawable.gash_home_icon_vaccination
    ),

    CIDR(
        labelRes = R.string.operation_cidr,
        iconRes = R.drawable.gash_home_icon_cidr
    ),

    MILKING(
        labelRes = R.string.operation_milking,
        iconRes = R.drawable.gash_home_icon_milking
    ),

    WEANING(
        labelRes = R.string.operation_weaning
    ),

    INSEMINATION(
        labelRes = R.string.operation_insemination
    ),

    DRYING(
        labelRes = R.string.operation_drying
    ),

    HOOF_TRIMMING(
        labelRes = R.string.operation_hoof_trimming
    ),

    DISEASE(
        labelRes = R.string.operation_disease
    )
}