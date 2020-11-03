package com.example.marvel_openbank.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ProgressBar
import com.example.marvel_openbank.data.entities.Character
import java.text.SimpleDateFormat
import java.util.*

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


fun getDateFormated(date: String) : String {
    val dateString = date.replace('T',' ')
    val cal = Calendar.getInstance()
    val sdfGet = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    cal.time = sdfGet.parse(dateString)
    val sdfSet = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    return sdfSet.format(cal.time)
}

//2014-04-29T14:18:17-0400
