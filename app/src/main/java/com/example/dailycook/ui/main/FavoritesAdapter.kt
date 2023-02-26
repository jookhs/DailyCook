package com.example.dailycook.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.FavoriteItemBinding
import com.example.dailycook.databinding.PantryItemBinding
import com.example.dailycook.model.MenuItem
import com.example.dailycook.model.MyListItem
import com.example.dailycook.model.PantryItem
import com.facebook.drawee.generic.RoundingParams

class FavoritesAdapter(private var recipes: List<MenuItem>?, private val clickListener: MainAdapterClickListener): RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private lateinit var binding: FavoriteItemBinding

    inner class FavoritesViewHolder(private var binding: FavoriteItemBinding, private val clickListener: MainAdapterClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(recipe: MenuItem?) {
            binding.favImage.setImageURI(recipe?.image)
            binding.favTv.text = recipe?.title
            binding.favCard.setOnClickListener {
                clickListener.onMenuItemClicked(recipe)
            }
            binding.favIcon.setImageResource(R.drawable.ic_heart_filled)
            binding.favIcon.setOnClickListener {
                clickListener.onHeartClicked(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val recipe = recipes?.get(position)
        holder.bind(recipe)
    }

    fun setItems(recipe: MutableList<MenuItem>?) {
        recipes = recipe
    }

    override fun getItemCount(): Int = recipes?.size ?: 0

}