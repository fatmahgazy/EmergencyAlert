package org.codeforegypt.quickassestant.data.repository

import org.codeforegypt.quickassestant.api.ArticlesApi
import org.codeforegypt.quickassestant.data.db.ArticleDao
import org.codeforegypt.quickassestant.data.model.Article
import org.codeforegypt.quickassestant.domain.repository.IArticleRepo
import javax.inject.Inject

class ArticleRepo @Inject constructor(
    private val articleApi: ArticlesApi,
    private val articleDao: ArticleDao
): IArticleRepo {
    override suspend fun getArticles(): List<Article>{
        val articlesResponse = articleApi.getArticles().await()
        return if (articlesResponse.isSuccessful && articlesResponse.body() != null)
            articlesResponse.body()!!.articles
        else
            emptyList<Article>()
    }
}