package com.example.dailycook.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailycook.databinding.PantryItemBinding
import com.example.dailycook.model.PantryItem

class MainFragmentAdapter(private val pantryItems: List<PantryItem>): RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder>() {

    private lateinit var binding: PantryItemBinding

    inner class MainFragmentViewHolder(private var binding: PantryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pantry: PantryItem) {
            binding.pantryImage.setImageURI(pantry.title, pantry.image)
            binding.pantryTv.text = pantry.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        binding = PantryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainFragmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        val pantry = pantryItems[position]
        holder.bind(pantry)
    }

    override fun getItemCount(): Int = pantryItems.size

}