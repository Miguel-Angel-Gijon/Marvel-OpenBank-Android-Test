package com.example.marvel_openbank.ui.characters

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel_openbank.R
import com.example.marvel_openbank.databinding.CharactersFragmentBinding
import com.example.marvel_openbank.ui.MainActivity
import com.example.marvel_openbank.utils.Resource.Status.*
import com.example.marvel_openbank.utils.autoCleared
import com.example.marvel_openbank.utils.getColorVersions
import com.example.marvel_openbank.utils.setColorHint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersFragment : Fragment(), CharactersAdapter.CharacterItemListener {

    private lateinit var menu: Menu
    private var binding: CharactersFragmentBinding by autoCleared()
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var adapterCharacters: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.fragment_characters_title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupComponents() {
        with(binding) {
            context?.let {
                progressBar.setColorHint(getColorVersions(R.color.colorPrimary, context))
                progressBarMoreItems.setColorHint(getColorVersions(R.color.white, context))
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapterCharacters.filteritems(newText)
                    return true
                }

            })
        }
    }

    private fun setupRecyclerView() {
        adapterCharacters = CharactersAdapter(this)
        with(binding.charactersRv) {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapterCharacters
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        binding.progressBarMoreItems.visibility = VISIBLE
                        viewModel.loadCharacters()
                    }
                }
            })
        }

    }

    private fun setupObservers() {
        viewModel.characters.observe(viewLifecycleOwner, Observer {
            with(binding) {
                when (it.status) {
                    SUCCESS -> {
                        progressBar.visibility = GONE
                        if (!it.data.isNullOrEmpty()) adapterCharacters.setItems(ArrayList(it.data))
                    }
                    ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = GONE
                        progressBarMoreItems.visibility = GONE
                    }
                    SUCCESS_UPDATE -> {
                        progressBarMoreItems.visibility = GONE
                        if (!it.data.isNullOrEmpty())
                            adapterCharacters.updateItems(it.data)
                        closeSearch()
                    }
                    LOADING ->
                        progressBar.visibility = VISIBLE
                }
            }
        })
    }

    override fun onClickedCharacter(characterId: Int) {
        findNavController().navigate(
            R.id.action_charactersFragment_to_characterDetailFragment,
            bundleOf("id" to characterId)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_navbar, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.search -> {
                openSearchBar()
                true
            }
            R.id.close_search -> {
                closeSearch()
                true
            }
            else -> NavigationUI.onNavDestinationSelected(
                item,
                requireView().findNavController()
            ) || super.onOptionsItemSelected(item)
        }

    private fun closeSearch() {
        menu.findItem(R.id.search).isVisible = true
        menu.findItem(R.id.close_search).isVisible = false
        binding.searchViewContainer.visibility = GONE
        binding.searchView.setQuery("", true)
    }

    private fun openSearchBar() {
        menu.findItem(R.id.search).isVisible = false
        menu.findItem(R.id.close_search).isVisible = true
        binding.searchViewContainer.visibility = VISIBLE
        binding.searchView.setQuery("", true)
    }

    override fun onResume() {
        binding.searchViewContainer.visibility = GONE
        if (adapterCharacters.itemCount == 0)
            presentation()
        else
            binding.presentationLayout.visibility = GONE
        super.onResume()
    }

    private fun presentation() {
        (activity as MainActivity).binding.toolbar.visibility = GONE
        with(binding.presentationLayout) {
            setOnClickListener { }
            val animation1 = AlphaAnimation(1.0f, 0.0f)
            animation1.duration = 1000
            animation1.startOffset = 2000
            animation1.fillAfter = true
            startAnimation(animation1)
            viewModel.viewModelScope.launch {
                delay(3000)
                visibility = GONE
                isClickable = false
                (activity as MainActivity).binding.toolbar.visibility = VISIBLE
            }
        }
    }
}