package org.codeforegypt.quickassestant.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped
import org.codeforegypt.quickassestant.api.ContactsApi
import org.codeforegypt.quickassestant.data.db.ContactDao
import org.codeforegypt.quickassestant.data.db.UserDao
import org.codeforegypt.quickassestant.data.db.UserDatabase
import org.codeforegypt.quickassestant.data.repository.ContactRepo
import org.codeforegypt.quickassestant.domain.repository.IContactRepo
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object ContactModule {

    @ActivityRetainedScoped
    @Provides
    fun provideArticleApi(retrofit: Retrofit): ContactsApi {
        return retrofit.create(ContactsApi::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideContactsRepository(contactsApi: ContactsApi, userDao: UserDao , contactDao: ContactDao): IContactRepo {
        return ContactRepo(contactsApi, userDao, contactDao)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideContactDao(db: UserDatabase) = db.getContactDao()
}
