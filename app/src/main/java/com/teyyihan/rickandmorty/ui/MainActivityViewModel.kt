/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.teyyihan.rickandmorty.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teyyihan.rickandmorty.data.CharacterRepository
import com.teyyihan.rickandmorty.model.CharacterModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [CharacterRepository] to get the data.
 */
@ExperimentalCoroutinesApi
class MainActivityViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository)
    : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<CharacterModel>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<CharacterModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<CharacterModel>> = repository.getSearchResultStream(queryString)
                //.map { pagingData -> pagingData.map { CharacterModel(it) } }
                //.cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}