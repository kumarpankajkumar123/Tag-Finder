package com.example.tagfinderapp.Util

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

object UserDatabase {
    private const val SHARED_NAME = "UserInfo"
    private const val UNIQUE_KEY = "unique_id"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun generateOrCreatedId(): String {
        val id = sharedPreferences.getString(UNIQUE_KEY, null)
        if (id == null) {
            val uniqueId = UUID.randomUUID().toString()
            editor.putString(UNIQUE_KEY, uniqueId)
            editor.apply()
            return uniqueId
        }
        return id
    }

    // Add a new URL for a specific user
    fun addUrl(url: String, userId: String) {
        try {
            val userJsonData = sharedPreferences.getString(userId, null)
            val userData = if (userJsonData != null) JSONObject(userJsonData) else createEmptyUserData()
            val urls: JSONArray = userData.getJSONArray("urls")
            if (!contains(urls, url)) urls.put(url)

            editor.putString(userId, userData.toString())
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Add a new keyword for a specific user
    fun addKeyword(keyword: String, userId: String) {
        try {
            val userJsonData = sharedPreferences.getString(userId, null)
            val userData = if (userJsonData != null) JSONObject(userJsonData) else createEmptyUserData()
            val keywords: JSONArray = userData.getJSONArray("keywords")
            if (!contains(keywords, keyword)) keywords.put(keyword)

            editor.putString(userId, userData.toString())
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Add a list of thumbnail URLs for a specific video URL
    fun addThumbnailsForUrl(videoUrl: String, thumbnails: List<String>, userId: String) {
        try {
            val userJsonData = sharedPreferences.getString(userId, null)
            val userData = if (userJsonData != null) JSONObject(userJsonData) else createEmptyUserData()

            // Create or update the "thumbnails" JSON object
            val thumbnailsObject = if (userData.has("thumbnails")) userData.getJSONObject("thumbnails") else JSONObject()
            val existingThumbnails = if (thumbnailsObject.has(videoUrl)) JSONArray(thumbnailsObject.getString(videoUrl)) else JSONArray()

            // Add new thumbnails if they don't already exist
            for (thumbnail in thumbnails) {
                if (!contains(existingThumbnails, thumbnail)) existingThumbnails.put(thumbnail)
            }

            thumbnailsObject.put(videoUrl, existingThumbnails)
            userData.put("thumbnails", thumbnailsObject)

            editor.putString(userId, userData.toString())
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Retrieve thumbnail URLs for a specific video URL
    fun getThumbnailsForUrl(videoUrl: String, userId: String): List<String> {
        val thumbnailsList = mutableListOf<String>()
        try {
            val userJsonData = sharedPreferences.getString(userId, null)
            val userData = userJsonData?.let { JSONObject(it) }
            if (userData != null && userData.has("thumbnails")) {
                val thumbnailsObject = userData.getJSONObject("thumbnails")
                if (thumbnailsObject.has(videoUrl)) {
                    val thumbnailsArray = thumbnailsObject.getJSONArray(videoUrl)
                    for (i in 0 until thumbnailsArray.length()) {
                        thumbnailsList.add(thumbnailsArray.getString(i))
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return thumbnailsList
    }

    @Throws(JSONException::class)
    private fun contains(array: JSONArray, value: String): Boolean {
        for (i in 0 until array.length()) {
            if (array.getString(i) == value) {
                return true
            }
        }
        return false
    }
    fun getUserData(userId: String): JSONObject? {
        return try {
            sharedPreferences.getString(userId, null)?.let { JSONObject(it) }
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }
    fun getKeywordsList(jsonObject: JSONObject): List<String> {
        val keywordsList = mutableListOf<String>()
        try {
            val keywordsArray = jsonObject.optJSONArray("keywords") ?: return keywordsList
            for (i in 0 until keywordsArray.length()) {
                keywordsList.add(keywordsArray.getString(i))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return keywordsList
    }
    fun getUrlsList(userData: JSONObject): List<String> {
        val urlsList = mutableListOf<String>()
        try {
            val urlsArray = userData.optJSONArray("urls")
            if (urlsArray != null) {
                for (i in 0 until urlsArray.length()) {
                    urlsList.add(urlsArray.getString(i))
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return urlsList
    }

    // Helper to create an empty user data object
    private fun createEmptyUserData(): JSONObject {
        val userData = JSONObject()
        userData.put("urls", JSONArray())
        userData.put("keywords", JSONArray())
        userData.put("thumbnails", JSONObject())
        return userData
    }

    fun getHistoryData(userId: String): List<Pair<String, String>> {
        val historyList = mutableListOf<Pair<String, String>>()
        try {
            val userJsonData = sharedPreferences.getString(userId, null)
            val userData = userJsonData?.let { JSONObject(it) }
            if (userData != null && userData.has("thumbnails")) {
                val thumbnailsObject = userData.getJSONObject("thumbnails")
                val keys = thumbnailsObject.keys()
                while (keys.hasNext()) {
                    val videoUrl = keys.next()
                    val thumbnailsArray = thumbnailsObject.getJSONArray(videoUrl)
                    if (thumbnailsArray.length() > 0) {
                        val firstThumbnail = thumbnailsArray.getString(0)
                        historyList.add(Pair(videoUrl, firstThumbnail))
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return historyList
    }

}
