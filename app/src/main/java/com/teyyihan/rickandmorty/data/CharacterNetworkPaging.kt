package com.teyyihan.rickandmorty.data

import androidx.paging.PagingSource
import com.teyyihan.rickandmorty.api.RickAndMortyAPI
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import retrofit2.HttpException
import java.io.IOException

class CharacterNetworkPaging(
        private val service: RickAndMortyAPI,
        private val query: CharacterQueryModel?
) : PagingSource<Int,CharacterModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {

        val position = params.key ?: 1
        return try {
            val response = service.getCharacters(position, query?.name, query?.status?.name,  query?.gender?.name)
            val repos = response.results
            LoadResult.Page(
                data = repos,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            if (exception.code() == 404) {
                return LoadResult.Page(emptyList(), position-1 ,null)
            }
            return LoadResult.Error(exception)
        }

    }

}