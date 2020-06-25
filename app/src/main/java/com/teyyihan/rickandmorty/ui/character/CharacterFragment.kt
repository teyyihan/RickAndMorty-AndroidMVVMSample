package com.teyyihan.rickandmorty.ui.character

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.Fade
import com.google.android.material.transition.MaterialContainerTransform
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.FragmentCharacterBinding

class CharacterFragment : Fragment() {

    private lateinit var binding : FragmentCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().setDuration(1000)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.transitionName = "trans"

    }



}