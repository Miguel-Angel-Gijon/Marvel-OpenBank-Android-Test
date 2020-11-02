package com.example.marvel_openbank.ui.characters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.example.marvel_openbank.data.entities.Character
import com.example.marvel_openbank.databinding.ItemCharacterBinding
import com.example.marvel_openbank.utils.SIZE_IMAGE_LIST
import com.example.marvel_openbank.utils.getURLImage
import com.example.marvel_openbank.utils.getURLImageList

class CharactersAdapter(private val listener: CharacterItemListener) :
    RecyclerView.Adapter<CharacterViewHolder>() {

    interface CharacterItemListener {
        fun onClickedCharacter(characterId: Int)
    }

    private val items = ArrayList<Character>()
    private val itemsAux = ArrayList<Character>()

    fun setItems(items: ArrayList<Character>) {
        this.items.clear()
        this.items.addAll(items.sortedWith(compareBy { it.name }))
        if (itemsAux.size < items.size) {
            itemsAux.clear()
            itemsAux.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun updateItems(itemsNew: List<Character>) {
        items.addAll(itemsNew)
        setItems(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding: ItemCharacterBinding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
        holder.bind(items[position])

    fun filteritems(newText: String?) {
        newText?.let {
            val itemsFilters: ArrayList<Character> = arrayListOf()
            itemsFilters.addAll(itemsAux.filter { character ->
                character.name.contains(
                    newText,
                    true
                )
            })
            setItems(itemsFilters)
        }
    }
}

class CharacterViewHolder(
    private val itemBinding: ItemCharacterBinding,
    private val listener: CharactersAdapter.CharacterItemListener
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var character: Character

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: Character) {
        this.character = item
        itemBinding.name.text = item.name
        Glide.with(itemBinding.root)
            .load(getURLImageList(item))
            .transform(CircleCrop())
            .into(itemBinding.image)
    }

    override fun onClick(v: View?) {
        listener.onClickedCharacter(character.id)
    }
}

