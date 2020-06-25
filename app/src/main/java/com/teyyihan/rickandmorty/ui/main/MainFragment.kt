package com.teyyihan.rickandmorty.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.teyyihan.rickandmorty.R
import com.teyyihan.rickandmorty.databinding.FragmentMainBinding
import com.teyyihan.rickandmorty.db.PreferencesRepository
import com.teyyihan.rickandmorty.ui.CharacterAdapter
import com.teyyihan.rickandmorty.ui.CharactersLoadStateAdapter
import com.teyyihan.rickandmorty.ui.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding.root

        recyclerView = binding.list
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        setMainViewmodelListener()

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

    private fun setMainViewmodelListener() {
        mainViewModel.queryTextLive.observe(viewLifecycleOwner, Observer {
            search(it)
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        initAdapter()
        search("query")
        retry_button.setOnClickListener { adapter.retry() }
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


}