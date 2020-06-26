package com.teyyihan.rickandmorty.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.model.CharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentViewmodel @ViewModelInject constructor(
    var repository: CharacterRepository
) : ViewModel() {


    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<CharacterModel>>? = null

    fun searchRepo(queryString: String?): Flow<PagingData<CharacterModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<CharacterModel>> = repository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }

}