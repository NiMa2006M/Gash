package com.example.gash.feature.filetransfer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.gash.R

enum class ExportOption(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val iconRes: Int,
    val isIconMonochrome: Boolean
) {
    ANIMALS_WEIGHTS(
        titleRes = R.string.file_transfer_option_animals_title,
        descriptionRes = R.string.file_transfer_option_animals_desc,
        iconRes = R.drawable.icon_weight,
        isIconMonochrome = true
    ),
    VACCINATION(
        titleRes = R.string.file_transfer_option_vaccination_title,
        descriptionRes = R.string.file_transfer_option_vaccination_desc,
        iconRes = R.drawable.gash_home_icon_vaccination,
        isIconMonochrome = false
    )
}