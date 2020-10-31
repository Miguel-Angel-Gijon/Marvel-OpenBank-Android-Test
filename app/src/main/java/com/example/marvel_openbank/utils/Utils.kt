package com.example.marvel_openbank.utils

import com.example.marvel_openbank.data.entities.Character

fun getURLImageList(character: Character): String = getURLImage(character, SIZE_IMAGE_LIST)
fun getURLImageDetail(character: Character): String = getURLImage(character, SIZE_IMAGE_CHARACTER_DETAIL)
fun getURLImage(character: Character, size : String): String = "${character.path}/$size.${character.extension}"