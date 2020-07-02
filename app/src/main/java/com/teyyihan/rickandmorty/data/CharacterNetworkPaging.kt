package com.teyyihan.rickandmorty.data

import androidx.paging.PagingSource
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import retrofit2.HttpException
import java.io.IOException

/**
 * This class is to fetch characters and displaying them without caching
 * ( Only network pagination )
 */
class CharacterNetworkPaging(
        private val service: RickAndMortyAPI,
        private val query: CharacterQueryModel?
) : PagingSource<Int,CharacterModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {

        val position = params.key ?: 1                                           // Get page count
        return try {
            // Make a request with a query if it exists
            val response = service.getCharacters(position, query?.name, query?.status,  query?.gender)
            val characters = response.results
            LoadResult.Page(
                data = characters,
                prevKey = if (position == 1) null else position - 1,             // If this is the first page, prev key must be null
                nextKey = if (characters.isEmpty()) null else position + 1       // If this is the last page , prev key must be null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            if (exception.code() == 404) {                                       // If code is 404 , that means there is no page with that query, return emptylist
                return LoadResult.Page(emptyList(), position-1 ,null)
            }
            return LoadResult.Error(exception)
        }

    }

}