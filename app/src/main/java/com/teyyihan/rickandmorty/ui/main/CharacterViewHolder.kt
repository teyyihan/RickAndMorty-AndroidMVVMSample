package com.teyyihan.rickandmorty.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.teyyihan.rickandmorty.databinding.CharacterViewItemBinding
import com.teyyihan.rickandmorty.model.CharacterModel
import javax.inject.Inject


class CharacterViewHolder @Inject constructor(
    val binding: CharacterViewItemBinding,
    val glide: RequestManager
) : RecyclerView.ViewHolder(binding.root) {

    val name: TextView = binding.characterViewItemCharacterName
    val cardView : CardView =binding.root
    private var character : CharacterModel? = null


    // If character is not null, display it
    fun bind(character: CharacterModel?) {
        if (character != null) {
            showRepoData(character)
        }
    }

    private fun showRepoData(character: CharacterModel) {
        this.character = character
        name.text = character.name

        glide.load(character._image).into(binding.characterViewItemCharacterImage)

    }

    companion object {
        fun create(
            parent: ViewGroup,
            glide: RequestManager
        ): CharacterViewHolder {
            val view = CharacterViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CharacterViewHolder(view,glide)
        }
    }
}
