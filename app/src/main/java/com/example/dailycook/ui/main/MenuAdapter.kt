package com.example.dailycook.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.FavoriteItemBinding
import com.example.dailycook.databinding.MenuItemBinding
import com.example.dailycook.databinding.PantryItemBinding
import com.example.dailycook.model.MenuItem
import com.example.dailycook.model.MyListItem
import com.example.dailycook.model.PantryItem
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams

class MenuAdapter(private var recipes: List<MenuItem>?, private val clickListener: MainAdapterClickListener): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }


    private lateinit var binding: MenuItemBinding

    inner class MenuViewHolder(private var binding: MenuItemBinding, private val clickListener: MainAdapterClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(recipe: MenuItem?) {
            if (recipe?.viewType == VIEW_TYPE_TWO) {
                binding.menuImage.layoutParams = ViewGroup.LayoutParams(600, 650)
                binding.menuImageBlur.layoutParams = ViewGroup.LayoutParams(600, 650)
            } else {
                binding.menuImage.layoutParams = ViewGroup.LayoutParams(600, 750)
                binding.menuImageBlur.layoutParams = ViewGroup.LayoutParams(600, 750)
            }
            binding.menuImage.setImageURI(recipe?.image)
            binding.menuTv.text = recipe?.title
            binding.menuCard.setOnClickListener {
                clickListener.onMenuItemClicked(recipe)
            }
            binding.menuIcon.setImageResource(R.drawable.ic_heart_filled)
            binding.menuIcon.setOnClickListener {
                clickListener.onHeartClicked(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val recipe = recipes?.get(position)
        holder.bind(recipe)
    }

    fun setItems(recipe: MutableList<MenuItem>?) {
        recipes = recipe
    }

    override fun getItemCount(): Int = recipes?.size ?: 0

}