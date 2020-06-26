package com.teyyihan.rickandmorty.ui.main

import android.content.Context
import android.os.Bundle
import androidx.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.*
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.FragmentMainBinding
import com.teyyihan.rickandmorty.db.PreferencesRepository
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.ui.CharacterAdapter
import com.teyyihan.rickandmorty.ui.CharactersLoadStateAdapter
import com.teyyihan.rickandmorty.ui.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    private val  viewModel by viewModels<MainFragmentViewmodel>()
    private val  mainViewModel by activityViewModels<MainActivityViewModel>()
    private val adapter = CharacterAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : FragmentMainBinding

    @Inject
    @ApplicationContext
    lateinit var mContext: Context
    private var searchJob: Job? = null

    private fun search(query: String?) {
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
        enterTransition = MaterialFadeThrough().apply {
            duration = 1000
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding.root

        recyclerView = binding.list
        recyclerView.layoutManager =GridLayoutManager(mContext,2)


        setSearchViewResultListener()

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

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
            override fun onCharacterClicked(cardView: View, email: CharacterModel) {

                exitTransition = createMaterialElevationScale(false).apply {
                    duration = 300
                }
                reenterTransition = createMaterialElevationScale(true).apply {
                    duration = 300
                }

                val action = MainFragmentDirections.actionMainFragmentToCharacterFragment(email)

                val extras = FragmentNavigatorExtras(cardView to "character_transition_"+email._id.toString())
                findNavController().navigate(action, extras)

            }

        }

        initAdapter()
        search("query")
        retry_button.setOnClickListener { adapter.retry() }
    }

    private fun setSearchViewResultListener() {
        mainViewModel.queryTextLive.observe(viewLifecycleOwner, Observer {
            search(it)
        })
    }

    private fun initAdapter() {
        recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CharactersLoadStateAdapter { adapter.retry() },
                footer = CharactersLoadStateAdapter { adapter.retry() }
        )


        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            //progress_bar.isVisible = (loadState.refresh is LoadState.Loading)
            // Show the retry state if initial load or refresh fails.
            retry_button.isVisible = loadState.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                        context,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun createMaterialElevationScale(forward: Boolean): Transition {
        return MaterialSharedAxis(MaterialSharedAxis.Z, forward).apply {
            val scaleProvider = primaryAnimatorProvider as ScaleProvider
            scaleProvider.incomingStartScale = 0.85F
            scaleProvider.outgoingEndScale = 0.85F
            secondaryAnimatorProvider = FadeProvider()
        }
    }



}