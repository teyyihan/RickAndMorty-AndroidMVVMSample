package com.teyyihan.rickandmorty.di

import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.db.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ViewmodelModule {


    @Provides
    fun provideCharacterRepo(service: RickAndMortyAPI, database : MainDatabase) : CharacterRepository {
        return CharacterRepository(service , database)
    }

}