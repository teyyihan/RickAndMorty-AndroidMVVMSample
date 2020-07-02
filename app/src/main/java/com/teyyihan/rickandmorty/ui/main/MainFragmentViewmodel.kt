package com.teyyihan.rickandmorty.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainFragmentViewmodel @ViewModelInject constructor(
    var repository: CharacterRepository
) : ViewModel() {


    init {
        // Initial
        searchCharacter(null)
    }

    private var currentQueryValue: CharacterQueryModel? = null

    var currentSearchResult: LiveData<PagingData<CharacterModel>>? = null

    fun searchCharacter(query: CharacterQueryModel?): LiveData<PagingData<CharacterModel>> {
        val lastResult = currentSearchResult

        // If the query is the same, just send the old result
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult = repository.getSearchResultStream(query)
            // Cached in viewmodel. When viewmodel dies, data will be lost
            .cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }

}