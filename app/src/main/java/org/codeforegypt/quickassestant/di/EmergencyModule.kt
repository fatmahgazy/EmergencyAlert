package org.codeforegypt.quickassestant.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.codeforegypt.quickassestant.api.EmergencyApi
import org.codeforegypt.quickassestant.data.db.UserDao
import org.codeforegypt.quickassestant.data.repository.EmergencyRepo
import org.codeforegypt.quickassestant.domain.repository.IEmergencyRepo
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ActivityRetainedComponent::class)
object EmergencyModule {

    @ActivityRetainedScoped
    @Provides
    fun provideRepoEmergency(emergencyApi: EmergencyApi , userDao: UserDao): IEmergencyRepo{
        return EmergencyRepo(emergencyApi, userDao)
    }
    @ActivityRetainedScoped
    @Provides
    fun provideEmergencyApi(retrofit: Retrofit): EmergencyApi{
        return retrofit.create(EmergencyApi::class.java)
    }

}