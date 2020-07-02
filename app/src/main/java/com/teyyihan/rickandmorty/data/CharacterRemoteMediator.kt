package com.teyyihan.rickandmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.db.MainDatabase
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import java.lang.Exception


/**
 *  This class is to fetch data and save them into Room database.
 *  Afterwards these datas will be displayed into recyclerview in MainFragment
 *  ( Single Source Of Truth )
 */
@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val service: RickAndMortyAPI,
    private val mainDatabase: MainDatabase
) : RemoteMediator<Int, CharacterModel>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterModel>): MediatorResult {

        /**
         *  Calculating next (append), previous (prepend) and initial(refresh) keys
         */
        val page: Int? = when (loadType) {

            LoadType.REFRESH -> {
                // initial key
                state.anchorPosition?.let { position ->
                    state.closestItemToPosition(position)?.nextKey?.minus(1) ?: 1
                } ?: 1
            }
            LoadType.PREPEND -> {
                // Prev key
                state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.prevKey

            }
            LoadType.APPEND -> {
                // Next key
                state.pages.lastOrNull{ it.data.isNotEmpty() }?.data?.lastOrNull()?.nextKey

            }

        }


        if (page != null) {
            try {
                val apiResponse = service.getCharacters(page,  null,  null,  null)
                val characters = apiResponse.results
                val endOfPaginationReached = characters.isEmpty()

                /**
                 *  Set prev and next keys into CharacterModels. So that we can access them with O(1) time complexity.
                 *  Paging 3.0 Codelab stores them into another Room entity.
                 */
                characters.forEach {
                    it.run {
                        this.prevKey = if (page == 1) null else page - 1
                        this.nextKey = if (endOfPaginationReached) null else page + 1
                    }
                }

                // Insert Characters
                mainDatabase.withTransaction {
                    mainDatabase.charactersDao().insertAll(characters)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (exception: IOException) {
                return MediatorResult.Error(exception)
            } catch (exception: HttpException) {
                if (exception.code() == 404) {
                    return MediatorResult.Success(endOfPaginationReached = true)            // If code is 404 , that means there is no page with that query, return endofPagination
                }
                return MediatorResult.Error(exception)
            }
        } else {
            return MediatorResult.Success(false)
        }

    }


}