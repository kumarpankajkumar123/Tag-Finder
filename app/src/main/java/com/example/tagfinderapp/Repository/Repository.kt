package com.example.tagfinderapp.Repository

import android.util.Log
import com.example.tagfinderapp.Inteface.ApiInterface
import com.example.tagfinderapp.Model.SearchResult
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Model.VideoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val api: ApiInterface) {

    suspend fun getVideotags(part: String, id: String, key: String): Result<VideoModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVideoTag(part, id, key)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getTodayVideo(part:String,chart:String,key:String,code :String,maxResult : Int): Result<TodayVideo> {
       return withContext(Dispatchers.IO){
           try {
               val res = api.getTodayVideo(part,chart,key,code,maxResult)
               if(res.isSuccessful && res.body() != null){
                   Result.success(res.body()!!)
               }
               else{
                   Result.failure<TodayVideo>(Exception("Error: ${res.code()}"))
               }
           } catch (e:Exception){
               Result.failure<TodayVideo>(e)
           }
       }
    }

    // Process JSON response
    private fun processJsonResponse(json: List<Any>) {
        // Extract the first item (string)
        val query = json.getOrNull(0) as? String ?: "No query found"

        // Extract the second item (list of suggestions)
        val suggestions = json.getOrNull(1) as? List<*> ?: emptyList<String>()

        // Extract the fourth item (object with suggest subtypes)
        val suggestSubtypes = (json.getOrNull(3) as? Map<*, *>)?.get("google:suggestsubtypes") as? List<List<Int>>

        // Log the extracted data
        Log.d("JSON Processing", "Query: $query")
        Log.d("JSON Processing", "Suggestions: $suggestions")
        Log.d("JSON Processing", "Suggest Subtypes: $suggestSubtypes")
    }

    suspend fun generateKeywords(key: String): Result<List<Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val res = api.getKeywords(key, "firefox", "yt")
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body != null) {
                        // Process the JSON response here
                        processJsonResponse(body)
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Response body is null"))
                    }
                } else {
                    Result.failure(Exception("Error: ${res.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
