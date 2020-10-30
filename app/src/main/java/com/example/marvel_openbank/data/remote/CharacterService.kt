package com.example.marvel_openbank.data.remote

import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.data.entities.CharacterListResponse
import com.example.marvel_openbank.utils.MD5
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {
    @GET("v1/public/characters")
    suspend fun getAllCharacters(
        @Query("orderBy") orderBy: String,
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): Response<CharacterListResponse>

    @GET("v1/public/characters/{characterId}?{path}")
    suspend fun getCharacter(
        @Path("characterId") id: Int,
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): Response<Character>
}
