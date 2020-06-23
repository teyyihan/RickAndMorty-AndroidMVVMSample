/*
 * Copyright (C) 2020 The Android Open Source Project
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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.db.MainDatabase
import com.teyyihan.rickandmorty.model.CharacterModel
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import java.lang.Exception


@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val query: String,
    private val service: RickAndMortyAPI,
    private val mainDatabase: MainDatabase
) : RemoteMediator<Int, CharacterModel>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterModel>): MediatorResult {


        val page : Int? = when (loadType) {
            LoadType.REFRESH -> {
                state.anchorPosition?.let { position ->
                    state.closestItemToPosition(position)?.nextKey?.minus(1) ?: 1
                } ?: 1
            }
            LoadType.PREPEND -> {

                state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.prevKey

            }
            LoadType.APPEND -> {

                state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.nextKey

            }

        }

        if (page != null) {
            try {
                val apiResponse = service.getCharacters(page)

                val characters = apiResponse.results
                val endOfPaginationReached = characters.isEmpty()

                characters.forEach {
                    it.run {
                        this.prevKey = if (page == 1) null else page - 1
                        this.nextKey = if (endOfPaginationReached) null else page + 1
                    }

                }

                mainDatabase.withTransaction {
                    mainDatabase.charactersDao().insertAll(characters)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (exception: IOException) {
                return MediatorResult.Error(exception)
            } catch (exception: HttpException) {
                return MediatorResult.Error(exception)
            }
        } else {
            return MediatorResult.Success(false)
        }

    }


}