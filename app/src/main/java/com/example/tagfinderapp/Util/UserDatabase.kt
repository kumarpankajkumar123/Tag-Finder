package com.example.tagfinderapp.Util

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

object UserDatabase {
    private const val SHARED_NAME = "UserInfo"
    private const val VIDEO_LIST = "video_list"
    private const val KEYWORDS_LIST = "keywords_list"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveVideo(videoId: String, thumbnailUrl: String,description:String) {
        val jsonArray = getVideoList()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            if (obj.getString("videoId") == videoId) {
                // Already exists → Do not add again
                return
            }
        }

        val jsonObj = JSONObject().apply {
            put("videoId", videoId)
            put("thumbnail", thumbnailUrl)
            put("description",description)
        }

        jsonArray.put(jsonObj)
        editor.putString(VIDEO_LIST, jsonArray.toString()).apply()
    }

    fun getVideoList(): JSONArray {
        val jsonString = sharedPreferences.getString(VIDEO_LIST, "[]")
        return try {
            JSONArray(jsonString)
        } catch (e: JSONException) {
            JSONArray()
        }
    }

    fun addKeyword(keyword: String) {
        // purane keywords list nikaalo
        val currentList = getKeywords().toMutableList()

        // optional: duplicate avoid karna ho
        if (!currentList.contains(keyword)) {
            currentList.add(keyword)
        }

        // list ko JSON array me convert karo
        val jsonArray = JSONArray()
        currentList.forEach { jsonArray.put(it) }

        // SharedPreferences me save karo
        editor.putString(KEYWORDS_LIST, jsonArray.toString()).apply()
    }

    fun clearKeywords() {
        editor.remove(KEYWORDS_LIST).apply()
    }

    fun getKeywords(): List<String> {
        val jsonString = sharedPreferences.getString(KEYWORDS_LIST, "[]")
        val jsonArray = JSONArray(jsonString)

        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }




}
