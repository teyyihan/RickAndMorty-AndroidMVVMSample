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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.CharacterViewItemBinding
import com.teyyihan.rickandmorty.model.CharacterModel

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class CharacterViewHolder(
    binding: CharacterViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    val name: TextView = binding.repoName
    val view : ConstraintLayout =binding.root

    private var repo: CharacterModel? = null


    fun bind(repo: CharacterModel?) {
        if (repo == null) {
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: CharacterModel) {
        this.repo = repo
        name.text = repo.name

    }

    companion object {
        fun create(
            parent: ViewGroup
        ): CharacterViewHolder {
            val view = CharacterViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CharacterViewHolder(view)
        }
    }
}
