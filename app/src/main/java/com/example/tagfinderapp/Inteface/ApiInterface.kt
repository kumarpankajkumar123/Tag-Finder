package com.example.tagfinderapp.Inteface

import com.example.tagfinderapp.Model.SearchResult
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Model.VideoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("youtube/v3/videos")
    suspend fun getVideoTag(@Query("part") part : String,      // snippet part to fetch tags
                            @Query("id")  videoId : String,     // YouTube video ID
                            @Query("key")  apiKey : String ) : Response<VideoModel>

    @GET("youtube/v3/videos")
    suspend fun getTodayVideo(@Query("part")  part : String,
                              @Query("chart") chart : String,
                              @Query("key") key : String,
                              @Query("regionCode") code :String,
                              @Query("maxResults") maxResult : Int):Response<TodayVideo>

    @GET("search")
    suspend fun getKeywords(@Query("q") q:String,
                            @Query("client") client : String,
                            @Query("ds") ds: String): Response<List<Any>>
}