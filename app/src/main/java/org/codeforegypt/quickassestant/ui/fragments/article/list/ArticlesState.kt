package org.codeforegypt.quickassestant.ui.fragments.article.list

import org.codeforegypt.quickassestant.data.model.Article

data class ArticlesState(
    val articles: List<Article> = emptyList(),
    val filteredArticles: List<Article>? = null,
    val filter: String? = null
)
