package org.codeforegypt.quickassestant.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.codeforegypt.quickassestant.data.model.Article

@Dao
interface ArticleDao {

    @Insert
    fun insertArticle(article: Article)

    @Query("SELECT * FROM Article")
    fun getAllArticles(): List<Article>


}