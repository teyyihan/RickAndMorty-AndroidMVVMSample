package com.teyyihan.rickandmorty.ui.main

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.teyyihan.rickandmorty.databinding.CharacterViewItemBinding
import com.teyyihan.rickandmorty.model.CharacterModel

class CharacterAdapter(val glide: RequestManager) : PagingDataAdapter<CharacterModel, CharacterViewHolder>(
    CHARACTER_COMPARATOR
) {

    lateinit var characterClickListener: CharacterAdapterListener

    /**
     *  Interface - Listener mechanism to pass value and view to MainFragment
     *  and navigate via Navigation Component there.
     */
    interface CharacterAdapterListener {
        fun onCharacterClicked(characterBinding: CharacterViewItemBinding, characterModel: CharacterModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(
            parent,
            glide
        )

    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterModel = getItem(position)

        characterModel.let {
         holder.bind(characterModel)
        }
        /**
         *  Set transition name for Material Container Transform
         *  If you don't, you will get IllegalStateException at runtime.
         */
        holder.binding.root.transitionName = "character_transition_container_"+characterModel?._id.toString()

        // Pass view and data
        holder.cardView.setOnClickListener {
            if (characterModel != null) {
                characterClickListener.onCharacterClicked(holder.binding,characterModel)
            }
        }

    }

    companion object {
        /**
         *  DiffUtil Callback to find changes and default smooth animation
         */
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<CharacterModel>() {
            override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
                return oldItem._id?.equals(newItem._id)!!
            }

            override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean =
                    oldItem == newItem
        }
    }
}