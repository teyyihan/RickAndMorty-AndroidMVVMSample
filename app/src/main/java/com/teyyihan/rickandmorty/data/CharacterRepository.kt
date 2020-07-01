package com.teyyihan.rickandmorty.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.teyyihan.rickandmorty.Consts.NETWORK_PAGE_SIZE
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

    fun getSearchResultStream(query: CharacterQueryModel?): LiveData<PagingData<CharacterModel>> {


        val pagingSourceFactory = { database.charactersDao().getCharacters() }

        return if (query == null) {                         // Normal listing
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                remoteMediator = CharacterRemoteMediator(
                    service,
                    database
                ),
                pagingSourceFactory = pagingSourceFactory
            ).liveData
        } else {                                            // Filtering
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = { CharacterNetworkPaging(service, query) }
            ).liveData
        }


    }


}
