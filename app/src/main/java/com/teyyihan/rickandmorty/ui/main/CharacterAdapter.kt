package com.teyyihan.rickandmorty.ui.main

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.teyyihan.rickandmorty.databinding.CharacterViewItemBinding
import com.teyyihan.rickandmorty.model.CharacterModel

class CharacterAdapter(val glide: RequestManager) : PagingDataAdapter<CharacterModel, CharacterViewHolder>(
    UIMODEL_COMPARATOR
) {


    lateinit var characterClickListener: CharacterAdapterListener

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
        holder.binding.root.transitionName = "character_transition_container_"+characterModel?._id.toString()

        holder.cardView.setOnClickListener {
            if (characterModel != null) {
                characterClickListener.onCharacterClicked(holder.binding,characterModel)
            }
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