package com.teyyihan.rickandmorty.di

import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.teyyihan.rickandmorty.Consts
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.db.MainDatabase
import com.teyyihan.rickandmorty.db.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext appContext: Context) : RequestManager {
        return Glide.with(appContext)
    }

    @Singleton
    @Provides
    fun providePreferencesRepository(sharedPreferences: SharedPreferences): PreferencesRepository {
        return PreferencesRepository(sharedPreferences)
    }

    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences(Consts.DEFAULT_PREFERENCES,Context.MODE_PRIVATE)
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideHttpLoginInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    fun provideHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }



}