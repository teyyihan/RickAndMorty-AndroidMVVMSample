package com.teyyihan.rickandmorty.ui.character

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.teyyihan.rickandmorty.model.CharacterModel

class CharacterAdapter : PagingDataAdapter<CharacterModel, CharacterViewHolder>(UIMODEL_COMPARATOR) {


    lateinit var characterClickListener: CharacterAdapterListener

    interface CharacterAdapterListener {
        fun onCharacterClicked(cardView: View, characterModel: CharacterModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(parent)

    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterModel = getItem(position)

        characterModel.let {
         holder.bind(characterModel)
        }
        holder.cardView.transitionName = "character_transition_container_"+characterModel?._id.toString()
        holder.cardView.setOnClickListener {
            if (characterModel != null) {
                characterClickListener.onCharacterClicked(holder.cardView,characterModel)
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