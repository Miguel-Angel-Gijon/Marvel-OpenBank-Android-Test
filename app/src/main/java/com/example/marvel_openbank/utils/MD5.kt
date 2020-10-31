package com.example.marvel_openbank.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.Timestamp

class MD5() {


    lateinit var timestamp: String
    var hash: String? = null

    lateinit var apikey: String


    private fun getMD5EncryptedString(encTarget: String): String? {
        var mdEnc: MessageDigest? = null
        try {
            mdEnc = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            println("Exception while encrypting to md5")
            e.printStackTrace()
        } // Encryption algorithm
        mdEnc?.update(encTarget.toByteArray(), 0, encTarget.length)
        var md5: String = BigInteger(1, mdEnc?.digest()).toString(16)
        while (md5.length < 32) {
            md5 = "0$md5"
        }
        return md5
    }

    fun getHashComplete() {
        timestamp = Timestamp(System.currentTimeMillis()).time.toString()
        hash = getMD5EncryptedString("$timestamp$PRIVATE_KEY$API_KEY")
    }
}