package org.codeforegypt.quickassestant.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped
import org.codeforegypt.quickassestant.api.ArticlesApi
import org.codeforegypt.quickassestant.data.db.ArticleDao
import org.codeforegypt.quickassestant.data.db.UserDatabase
import org.codeforegypt.quickassestant.data.repository.ArticleRepo
import org.codeforegypt.quickassestant.domain.repository.IArticleRepo
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ActivityRetainedComponent::class)
object ArticleModule {

    @ActivityRetainedScoped
    @Provides
    fun provideArticleApi(retrofit: Retrofit): ArticlesApi{
        return retrofit.create(ArticlesApi::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideArticleRepo(articleApi: ArticlesApi , articleDao: ArticleDao): IArticleRepo{
        return ArticleRepo(articleApi, articleDao)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideArticleDao(db: UserDatabase) = db.getArticleDao()


}