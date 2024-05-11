package org.codeforegypt.quickassestant.api

import kotlinx.coroutines.Deferred
import org.codeforegypt.quickassestant.data.model.Article
import org.codeforegypt.quickassestant.data.model.Articles
import retrofit2.Response
import retrofit2.http.GET

interface ArticlesApi {

    @GET("data/articles")
    fun getArticles(): Deferred<Response<Articles>>
}