package com.example.dailycook

import com.example.dailycook.model.PantryItem

interface MainAdapterClickListener {
    fun onImageClicked(pantryItem: PantryItem?) {}
    fun onItemClicked(item: String) {}
}