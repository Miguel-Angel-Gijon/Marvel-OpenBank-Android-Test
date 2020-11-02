package com.example.marvel_openbank.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ProgressBar
import com.example.marvel_openbank.R
import com.example.marvel_openbank.data.entities.Character

fun getURLImageList(character: Character): String = getURLImage(character, SIZE_IMAGE_LIST)
fun getURLImageDetail(character: Character): String =
    getURLImage(character, SIZE_IMAGE_CHARACTER_DETAIL)

fun getURLImage(character: Character, size: String): String =
    "${character.path}/$size.${character.extension}"

fun getColorVersions(color: Int, context: Context?): Int =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        context?.getColor(color) ?: Color.BLACK

    } else {
        Resources.getSystem().getColor(color)
    }


fun ProgressBar.setColorHint(colorHint: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        context.let {
            indeterminateDrawable.mutate().colorFilter =
                BlendModeColorFilter(colorHint, BlendMode.SRC_ATOP)
        }
    } else {
        indeterminateDrawable.mutate().setColorFilter(
            colorHint, PorterDuff.Mode.SRC_ATOP
        )
    }
}