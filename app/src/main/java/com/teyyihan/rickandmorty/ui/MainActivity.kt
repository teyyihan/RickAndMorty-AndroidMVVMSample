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

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.ActivityMainBinding
import com.teyyihan.rickandmorty.db.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private lateinit var binding: ActivityMainBinding
    private val mainViewmodel by viewModels<MainActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.mainFragmentToolbar)
        binding.searchView.setOnQueryTextListener(searchViewListener)

        preferencesRepository.nightModeLive.observe(this, Observer {
                it?.let { itt-> delegate.localNightMode = itt }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_appbar_menu, menu)

        val item: MenuItem = menu!!.findItem(R.id.main_appbar_search_menu_item)
        binding.searchView.setMenuItem(item)

        return true
    }

    val searchViewListener = object : MaterialSearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            queryTextSubmit(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {

            return false
        }

    }

    fun queryTextSubmit(query: String?) {
        mainViewmodel.queryTextLive.value = query
    }

}
