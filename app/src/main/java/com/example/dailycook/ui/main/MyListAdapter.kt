package com.example.dailycook.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.databinding.MyListItemBinding
import com.example.dailycook.model.MyListItem
import com.google.android.material.chip.Chip
import java.util.*

class MyListAdapter(private val context: Context, private val clickListener: MainAdapterClickListener): RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {

    private lateinit var binding: MyListItemBinding
    private var pantryItems: MutableList<MyListItem>? = null

    inner class MyListViewHolder(private var binding: MyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(item: MyListItem?) {
            binding.title.text = item?.name
            binding.myListChips.removeAllViews()
            item?.items?.forEach {
                val chip = Chip(context)
                val text = it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
                chip.text = text
                chip.isCloseIconVisible = true
                chip.isClickable = true
                chip.isCheckable = false
                binding.myListChips.addView(chip)
                binding.card.visibility = View.VISIBLE
                chip.setOnCloseIconClickListener { ch ->
                    binding.myListChips.removeView(chip)
                    if (binding.myListChips.getChildAt(0) == null) {
                        pantryItems?.remove(item)
                        binding.card.visibility = View.GONE
                    }
                    clickListener.onItemClicked(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        binding = MyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val pantry = pantryItems?.get(position)
        holder.bind(pantry)
    }

    fun setItems(pantry: MutableList<MyListItem>?) {
        pantryItems = pantry
    }

    override fun getItemCount(): Int = pantryItems?.size ?: 0

}