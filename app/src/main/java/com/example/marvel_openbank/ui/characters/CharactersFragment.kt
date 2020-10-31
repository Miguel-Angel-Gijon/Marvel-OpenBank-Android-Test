package com.example.marvel_openbank.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel_openbank.R
import com.example.marvel_openbank.databinding.CharactersFragmentBinding
import com.example.marvel_openbank.utils.Resource.Status.*
import com.example.marvel_openbank.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment(), CharactersAdapter.CharacterItemListener {

    private var binding: CharactersFragmentBinding by autoCleared()
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var adapterCharacters: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
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
                        progressBarMoreItems.visibility = GONE
                        if (!it.data.isNullOrEmpty()) adapterCharacters.setItems(ArrayList(it.data))
                    }
                    ERROR ->
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

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
}
