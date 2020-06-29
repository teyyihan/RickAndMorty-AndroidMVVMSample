package com.teyyihan.rickandmorty.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialFadeThrough
import com.teyyihan.rickandmorty.Consts
import com.teyyihan.rickandmorty.databinding.CharacterViewItemBinding
import com.teyyihan.rickandmorty.databinding.FragmentMainBinding
import com.teyyihan.rickandmorty.db.PreferencesRepository
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.CharacterQueryModel
import com.teyyihan.rickandmorty.ui.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    @Inject
    @ApplicationContext
    lateinit var appContext: Context
    @Inject
    lateinit var glide : RequestManager
    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    private val  viewModel by viewModels<MainFragmentViewmodel>()
    private val  mainViewModel by activityViewModels<MainActivityViewModel>()
    private lateinit var adapter : CharacterAdapter
    private lateinit var binding : FragmentMainBinding



    private var searchJob: Job? = null

    private fun search(query: CharacterQueryModel?) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CharacterAdapter(glide)
        enterTransition = MaterialFadeThrough().apply {
            duration = 300
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding.root

        binding.list.layoutManager = GridLayoutManager(appContext, Consts.GRID_COUNT)



        // add dividers between RecyclerView's row items
//        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//        recyclerView.addItemDecoration(decoration)

//       preferencesRepository
//           .isDarkThemeLive.observe(viewLifecycleOwner, Observer { isDarkTheme ->
//                isDarkTheme?.let { darkThemeSwitch.isChecked = it
//                }
//            })
//
//        binding.retryButton.setOnClickListener {
//            preferencesRepository.isDarkTheme = true
//        }
//
//        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
//            preferenceRepository.isDarkTheme = checked
//        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        adapter.characterClickListener = object : CharacterAdapter.CharacterAdapterListener{
            override fun onCharacterClicked(characterBinding: CharacterViewItemBinding, characterModel: CharacterModel) {

                val action = MainFragmentDirections.actionMainFragmentToCharacterFragment(characterModel)
                val extras = FragmentNavigatorExtras((characterBinding.root to Consts.CHARACTER_CONTAINER_TRANSITION))
                findNavController().navigate(action, extras)

            }

        }

        initAdapter()
        search(/*CharacterQueryModel(name = "Rick")*/null)
        binding.retryButton.setOnClickListener { adapter.retry() }
    }


    private fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CharactersLoadStateAdapter { adapter.retry() },
                footer = CharactersLoadStateAdapter { adapter.retry() }
        )


        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.list.isVisible = loadState.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(context, "Wooops", Toast.LENGTH_LONG).show()
            }
        }

    }

}