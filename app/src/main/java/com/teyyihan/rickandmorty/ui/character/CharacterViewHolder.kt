package com.teyyihan.rickandmorty.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.teyyihan.rickandmorty.databinding.CharacterViewItemBinding
import com.teyyihan.rickandmorty.model.CharacterModel


class CharacterViewHolder(
    binding: CharacterViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    val name: TextView = binding.characterViewItemCharacterName
    val cardView : CardView =binding.root

    private var character : CharacterModel? = null


    fun bind(character: CharacterModel?) {
        if (character == null) {
        } else {
            showRepoData(character)
        }
    }

    private fun showRepoData(character: CharacterModel) {
        this.character = character
        name.text = character.name
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
