package com.example.marvel_openbank.ui.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.marvel_openbank.R
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.databinding.CharacterDetailFragmentBinding
import com.example.marvel_openbank.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {

    private var binding: CharacterDetailFragmentBinding by autoCleared()
    private val viewModel: CharacterDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharacterDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("id")?.let { viewModel.start(it) }
        setupObservers()
        setupComponents()
    }

    private fun setupComponents() {
        context?.let {
            binding.progressBar.setColorHint(getColorVersions(R.color.white, context))
            binding.progressBarImage.setColorHint(getColorVersions(R.color.white, context))
        }
    }

    private fun setupObservers() {
        viewModel.character.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bindCharacter(it.data!!)
                    binding.progressBar.visibility = View.GONE
                    binding.characterCl.visibility = View.VISIBLE
                }

                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.characterCl.visibility = View.GONE
                }
                Resource.Status.SUCCESS_UPDATE -> {}
            }
        })
    }

    private fun bindCharacter(character: Character) {
        binding.name.text = character.name.toUpperCase(Locale.getDefault())
        binding.lastUpdate.text = context?.getString(R.string.last_date, getDateFormated(character.date))
        binding.description.text = character.description.let {
            if (it.isNotEmpty()) it else getString(
                R.string.not_description
            )
        }
        Glide.with(binding.root)
            .load(getURLImageDetail(character))
            .transform(RoundedCorners(20))
            .into(binding.image)
    }
}
