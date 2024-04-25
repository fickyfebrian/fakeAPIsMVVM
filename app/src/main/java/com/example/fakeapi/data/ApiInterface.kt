package com.example.fakeapi.data

import com.example.fakeapi.models.Posts
import retrofit2.http.GET

interface ApiInterface {
    @GET("/posts")
    suspend fun getPost(): List<Posts>
}