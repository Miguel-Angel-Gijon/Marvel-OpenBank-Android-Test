package com.example.marvel_openbank.data.repository

import com.example.marvel_openbank.data.local.CharacterDao
import com.example.marvel_openbank.data.remote.CharacterRemoteDataSource
import com.example.marvel_openbank.utils.TransformersDao
import com.example.marvel_openbank.utils.performGetOperation
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
) {

    private val transformersDao: TransformersDao = TransformersDao()
    private var offset: Int = 0

    fun getCharacter(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getCharacter(id) },
        networkCall = { remoteDataSource.getCharacter(id) },
        saveCallResult = {
            localDataSource.insert(transformersDao.transformCharacter(it.data.results[0]))
        }
    )

    fun getCharacters(recall: Boolean = false) = performGetOperation(
        databaseQuery = { localDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getCharacters(offset) },
        saveCallResult = {
            offset = it.data.count
            localDataSource.insertAll(transformersDao.transformList(it.data))
        },
        reCall = recall
    )
}