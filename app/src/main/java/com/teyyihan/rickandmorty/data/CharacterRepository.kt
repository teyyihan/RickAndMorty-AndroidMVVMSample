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

        /**
         *  Truth source for displaying data from database.
         *  Notice that this reference doesn't be used on listing with query.
         *  Because listing with query doesn't cache data
         */
        val pagingSourceFactory = { database.charactersDao().getCharacters() }

        // Normal listing, without any query
        return if (query == null) {
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                remoteMediator = CharacterRemoteMediator(
                    service,
                    database
                ),
                pagingSourceFactory = pagingSourceFactory
            ).liveData
        }
        // Listing with a given query
        else {                                            // Filtering
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                pagingSourceFactory = { CharacterNetworkPaging(service, query) }
            ).liveData
        }


    }


}
