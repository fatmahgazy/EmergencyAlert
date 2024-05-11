package org.codeforegypt.quickassestant.domain.repository

import org.codeforegypt.quickassestant.data.model.Article

interface IArticleRepo {

    suspend fun getArticles(): List<Article>
}