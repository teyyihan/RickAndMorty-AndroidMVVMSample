package com.teyyihan.rickandmorty.ui.character

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.Fade
import com.google.android.material.transition.MaterialContainerTransform
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.FragmentCharacterBinding
import java.lang.Exception

class CharacterFragment : Fragment() {

    private lateinit var binding : FragmentCharacterBinding
    private val args: CharacterFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().setDuration(300)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)
        binding.root.transitionName = "1" /*getString(R.string.character_transition_container,args.character?._id.toString())*/
        return binding.root
    }

}