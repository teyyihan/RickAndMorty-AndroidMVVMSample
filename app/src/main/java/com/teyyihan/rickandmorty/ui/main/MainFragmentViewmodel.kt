package com.teyyihan.rickandmorty.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentViewmodel @ViewModelInject constructor(
    var repository: CharacterRepository
) : ViewModel() {


    private var currentQueryValue: CharacterQueryModel? = null

    private var currentSearchResult: Flow<PagingData<CharacterModel>>? = null

    fun searchRepo(query: CharacterQueryModel?): Flow<PagingData<CharacterModel>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<CharacterModel>> = repository.getSearchResultStream(query)
            .cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }

}