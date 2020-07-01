package com.teyyihan.rickandmorty.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.BottomSheetLayoutBinding
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ClassCastException
import java.lang.Exception
import javax.inject.Inject

class QueryBottomSheet : BottomSheetDialogFragment() {

    var mListener: BottomSheetListener? = null
    private var nameQuery : String? = null
    private var statusQuery : String? = null
    private var genderQuery : String? = null

    interface BottomSheetListener{
        fun queryButtonClicked(query : CharacterQueryModel?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetLayoutBinding.inflate(inflater,container,false)

        context?.let {
            ArrayAdapter.createFromResource(it, R.array.status_spinner, android.R.layout.simple_spinner_item).also { adapter ->

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.queryBottomSheetStatusSpinner.adapter = adapter
            }

            ArrayAdapter.createFromResource(it, R.array.gender_spinner, android.R.layout.simple_spinner_item).also { adapter ->

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.queryBottomSheetGenderSpinner.adapter = adapter
            }
        }

        binding.queryBottomSheetQueryButton.setOnClickListener {

            statusQuery = findStatus(binding.queryBottomSheetStatusSpinner.selectedItemId)
            genderQuery = findGender(binding.queryBottomSheetGenderSpinner.selectedItemId)

            mListener?.queryButtonClicked(
                CharacterQueryModel(
                    binding.queryBottomSheetEditText.text.toString(),
                    statusQuery,
                    genderQuery
                )
            )
            dismiss()
        }

        return binding.root
    }

    private fun findGender(selectedItemId: Long): String? {
        return when(selectedItemId){
            0L -> {
                null
            }
            1L ->{
                "female"
            }
            2L ->{
                "male"
            }
            3L ->{
                "genderless"
            }
            4L ->{
                "unknown"
            }
            else -> null
        }
    }

    private fun findStatus(selectedItemId: Long): String? {
        return when(selectedItemId){
            0L -> {
                null
            }
            1L ->{
                "alive"
            }
            2L ->{
                "dead"
            }
            3L ->{
                "unknown"
            }
            else -> null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as BottomSheetListener
    }

}