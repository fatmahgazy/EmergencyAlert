package org.codeforegypt.quickassestant.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped
import org.codeforegypt.quickassestant.api.ProfileApi
import org.codeforegypt.quickassestant.data.db.UserDao
import org.codeforegypt.quickassestant.data.repository.ProfileRepo
import org.codeforegypt.quickassestant.domain.repository.IProfileRepo
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object ProfileModule {

    @ActivityRetainedScoped
    @Provides
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideProfileRepo(userDao: UserDao, profileApi: ProfileApi): IProfileRepo{
        return ProfileRepo(userDao, profileApi)
    }


}