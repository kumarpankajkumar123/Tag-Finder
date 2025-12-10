package com.example.tagfinderapp.Repository

import android.util.Log
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.CompetitorModel
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.ApiHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val api: ApiInterface) {
    suspend fun getVideotags(part: String, id: String, key: String): ApiHandler<VideoModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVideoTag(part, id, key)
                if (response.isSuccessful && response.body() != null) {
                    ApiHandler.Success(response.body()!!)
                } else {
                    ApiHandler.Failure(response.message())
                }
            } catch (e: Exception) {
                ApiHandler.Failure(e.message ?: "")
            }
        }
    }

    suspend fun getTodayVideo(
        part: String,
        chart: String,
        key: String,
        code: String,
        maxResult: Int
    ): ApiHandler<TodayVideo> {
        return withContext(Dispatchers.IO) {
            try {
                val res = api.getTodayVideo(part, chart, key, code, maxResult)
                if (res.isSuccessful && res.body() != null) {
                    ApiHandler.Success(res.body()!!)
                } else {
                    ApiHandler.Failure("No data found")
                }
            } catch (e: Exception) {
                ApiHandler.Failure("Something went wrong")
            }
        }
    }
    suspend fun getCompetitors(
        part: String,
        key: String,
        maxResults: String,
        q: String,
        type: String
    ): ApiHandler<CompetitorModel?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCompetitors(part, key, maxResults, type, q)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        ApiHandler.Success(response.body())
                    } else {
                        ApiHandler.Failure("no data found")
                    }
                } else {
                    ApiHandler.Failure("Someting went wrong")
                }
            } catch (e: Exception) {
                ApiHandler.Failure("Something went wrong")
            }
        }
    }
}
