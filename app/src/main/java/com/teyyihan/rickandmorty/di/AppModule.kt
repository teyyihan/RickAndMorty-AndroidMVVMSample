package com.teyyihan.rickandmorty.di

import android.content.Context
import android.content.SharedPreferences
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
    fun provideRickAndMortyAPI(client : OkHttpClient, gsonConverterFactory: GsonConverterFactory) : RickAndMortyAPI {

        return Retrofit.Builder()
            .baseUrl(Consts.BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(RickAndMortyAPI::class.java)
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