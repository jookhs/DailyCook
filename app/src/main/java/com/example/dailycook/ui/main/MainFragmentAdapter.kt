package com.example.dailycook.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.PantryItemBinding
import com.example.dailycook.model.PantryItem
import com.facebook.drawee.generic.RoundingParams

class MainFragmentAdapter(private val pantryItems: List<PantryItem>?, private val clickListener: MainAdapterClickListener): RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder>() {

    private lateinit var binding: PantryItemBinding

    inner class MainFragmentViewHolder(private var binding: PantryItemBinding, private val clickListener: MainAdapterClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(pantry: PantryItem?) {
            binding.pantryImage.setImageURI(pantry?.image)
            binding.pantryTv.text = pantry?.title
            binding.pantryImage.setOnClickListener {
                clickListener.onImageClicked(pantry)
            }
            binding.pantryImageBorder.visibility = if (pantry?.added == true) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        binding = PantryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainFragmentViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        val pantry = pantryItems?.get(position)
        holder.bind(pantry)
    }

    override fun getItemCount(): Int = pantryItems?.size ?: 0

}