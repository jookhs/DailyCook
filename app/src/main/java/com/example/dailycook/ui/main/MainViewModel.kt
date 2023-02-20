package com.example.dailycook.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailycook.SettingsRemote
import com.example.dailycook.model.PantryItem

class MainViewModel(context: Context) : ViewModel() {
    val remote: SettingsRemote = SettingsRemote(context)
    private val pantryList: MutableList<PantryItem> = mutableListOf()

    fun getListOfPantries(): List<PantryItem> {
        if (pantryList.isNotEmpty()) return pantryList
        remote.toolConfig()?.pantryConfig?.products?.forEach {
            pantryList.add(PantryItem(it.icon, it.name))
        }
        return pantryList
    }
}

class MainViewModelFactory(val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Context::class.java).newInstance(context)
    }

}