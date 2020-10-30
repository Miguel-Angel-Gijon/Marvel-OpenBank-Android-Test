package com.example.marvel_openbank.data.remote

import com.example.marvel_openbank.utils.MD5
import javax.inject.Inject

class CharacterRemoteDataSource @Inject constructor(
    private val characterService: CharacterService
) : BaseDataSource() {

    suspend fun getCharacters() = getResult {
        val md5 = MD5()
        md5.getHashComplete()
        val hash: String = md5.hash ?: ""
        characterService.getAllCharacters("name",md5.timestamp, md5.API_KEY, hash)
    }

    suspend fun getCharacter(id: Int) = getResult {
        val md5 = MD5()
        md5.getHashComplete()
        val hash: String = md5.hash ?: ""
        characterService.getCharacter(id, md5.timestamp, md5.API_KEY, hash)
    }
}