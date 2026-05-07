package com.example.pgapp.di

import com.example.pgapp.data.remote.FirebaseService
import com.example.pgapp.data.repository.AuthRepository
import com.example.pgapp.data.repository.PgRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseService(): FirebaseService {
        return FirebaseService()
    }

    @Provides
    fun providePgRepository(firebase: FirebaseService): PgRepository {
        return PgRepository(firebase)
    }

    @Provides
    fun provideAuthRepository(firebase: FirebaseService): AuthRepository {
        return AuthRepository(firebase)
    }
}