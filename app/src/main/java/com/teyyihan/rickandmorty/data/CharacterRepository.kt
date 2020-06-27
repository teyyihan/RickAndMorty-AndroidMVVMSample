/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teyyihan.rickandmorty.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teyyihan.rickandmorty.Consts
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.db.MainDatabase
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that works with local and remote data sources.
 */
class CharacterRepository(
    private val service: RickAndMortyAPI,
    private val database: MainDatabase
) {

    fun getSearchResultStream(query: CharacterQueryModel?): Flow<PagingData<CharacterModel>> {

        // appending '%' so we can allow other characters to be before and after the query string
        //val dbQuery = "%${query?.replace(' ', '%')}%"
        val pagingSourceFactory = { database.charactersDao().getCharacters() }

        val pager = Pager(
                config = PagingConfig(pageSize = Consts.NETWORK_PAGE_SIZE),
                remoteMediator = CharacterRemoteMediator(
                        query,
                        service,
                        database
                ),
                pagingSourceFactory = pagingSourceFactory
        ).flow


        return pager
    }


}
