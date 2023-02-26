package com.example.dailycook

import com.example.dailycook.model.MenuItem
import com.example.dailycook.model.PantryItem

interface MainAdapterClickListener {
    fun onImageClicked(pantryItem: PantryItem?) {}
    fun onItemClicked(item: String) {}
    fun onHeartClicked(item: MenuItem?) {}
    fun onMenuItemClicked(item: MenuItem?) {}
}