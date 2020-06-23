package com.teyyihan.rickandmorty.di

import android.content.Context
import com.teyyihan.rickandmorty.Consts
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.db.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideCharacterRepo(service: RickAndMortyAPI, database : MainDatabase) : CharacterRepository {
        return CharacterRepository(service , database)
    }

    @Provides
    fun provideRickAndMortyAPI() : RickAndMortyAPI {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
        return Retrofit.Builder()
            .baseUrl(Consts.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyAPI::class.java)
    }

    @Provides
    fun provideMainDatabase(@ApplicationContext context: Context): MainDatabase {
        return MainDatabase.getInstance(context)
    }

}