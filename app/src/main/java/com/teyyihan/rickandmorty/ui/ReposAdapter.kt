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

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.teyyihan.rickandmorty.model.CharacterModel

/**
 * Adapter for the list of repositories.
 */
class ReposAdapter : PagingDataAdapter<CharacterModel, ViewHolder>(UIMODEL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("teoooooo geldi oncreate")
        return RepoViewHolder.create(parent)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("teoooooo geldi onbind")

        val uiModel = getItem(position)
        uiModel.let {

         (holder as RepoViewHolder).bind(uiModel)

        }
    }

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<CharacterModel>() {
            override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
                return oldItem._id?.equals(newItem._id)!!
            }

            override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean =
                    oldItem == newItem
        }
    }
}