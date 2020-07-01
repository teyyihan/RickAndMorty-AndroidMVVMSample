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
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentViewmodel @ViewModelInject constructor(
    var repository: CharacterRepository
) : ViewModel() {


    init {
        searchRepo(null)
    }

    private var currentQueryValue: CharacterQueryModel? = null

    var currentSearchResult: LiveData<PagingData<CharacterModel>>? = null

    fun searchRepo(query: CharacterQueryModel?): LiveData<PagingData<CharacterModel>> {
        val lastResult = currentSearchResult
        println("teoooo zaten öyleydi  currentQueryValue  "+currentQueryValue +"   query   "+query)
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult = repository.getSearchResultStream(query)
            .cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }

}