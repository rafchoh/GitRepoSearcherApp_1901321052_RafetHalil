package com.rafetrar.apps.kotlinapplication.api.git

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IGitAPI {

    companion object {

        const val  GIT_BASE_URL = "https://api.github.com/"
    }

    @Headers(
        "Accept: application/vnd.github.v3+json"
    )
    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): GitAPIResponse
}