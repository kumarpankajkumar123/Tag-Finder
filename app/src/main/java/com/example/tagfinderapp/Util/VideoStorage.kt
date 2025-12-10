package com.example.tagfinderapp.Util

import android.content.Context
import com.example.tagfinderapp.Model.VideoModel
import com.google.gson.Gson

object VideoStorage {

    private const val PREFS_NAME = "app_prefs"
    private const val KEY_TODAY_VIDEO_JSON = "today_video_json"
    private val gson = Gson()

    // JSON + object dono save karne ka simple tarika
    fun saveTodayVideo(context: Context, videoModel: VideoModel) {
        val jsonString = gson.toJson(videoModel)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_TODAY_VIDEO_JSON, jsonString)
            .apply()
    }

    // Sirf JSON chahiye ho to
    fun getTodayVideoJson(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TODAY_VIDEO_JSON, null)
    }

    // Direct VideoModel object chahiye
    fun getTodayVideo(context: Context): VideoModel? {
        val json = getTodayVideoJson(context) ?: return null
        return gson.fromJson(json, VideoModel::class.java)
    }

    // Agar clear karna ho
    fun clearTodayVideo(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_TODAY_VIDEO_JSON)
            .apply()
    }
}
