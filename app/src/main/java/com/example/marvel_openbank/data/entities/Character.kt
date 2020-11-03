package com.example.marvel_openbank.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    val id: Int,
    val description: String,
    val name: String,
    val path: String,
    val extension: String,
    val date : String
)

data class CharacterCallResponse(
    val data: CharacterDataResponse
)

data class CharacterDataResponse(
    val results: List<CharacterResponse>
)

class CharacterResponse(
    val id: Int,
    val description: String,
    val name: String,
    val thumbnail: ImageCharacter?,
    val modified : String
)

