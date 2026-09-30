package com.example.gash.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.gash.R

enum class HomePage(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int
) {
    WEIGHT(R.string.home_shortcut_weight, R.drawable.icon_weight),
    OPERATIONS(R.string.home_shortcut_operations_census, R.drawable.icon_operations_census),
    HERD_MANAGEMENT(R.string.home_shortcut_herd_management, R.drawable.icon_herd_management)
}