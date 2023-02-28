package com.example.dailycook.ui.main

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailycook.SettingsRemote
import com.example.dailycook.model.MenuItem
import com.example.dailycook.model.MyListItem
import com.example.dailycook.model.PantryItem
import com.example.dailycook.model.SpinnerItem
import com.facebook.common.util.UriUtil
import java.util.*


class MainViewModel(context: Context) : ViewModel() {
    val remote: SettingsRemote = SettingsRemote(context)
    val pantryList: MutableList<PantryItem> = mutableListOf()
    var currentPantryName: String? = null
    private val _myList = MutableLiveData<List<String>>()
    val myList: LiveData<List<String>> get() = _myList
    var myIngredientsList: MutableList<String> = mutableListOf()
    var spinnerItemsList = mutableListOf<SpinnerItem>()
    var favoriteRecipesList = mutableListOf<MenuItem>()
    private val _previewPageFlow = MutableLiveData<List<String>>()
    var checkedChipsList: MutableList<String> = mutableListOf()

    fun getListOfPantries(resources: Resources, context: Context?): List<PantryItem>? {
        if (pantryList.isNotEmpty()) return pantryList
        remote.toolConfig()?.pantryConfig?.products?.forEach {
            val icon = resources.getIdentifier(it.icon, "drawable", context?.packageName)
            val uri = Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(java.lang.String.valueOf(icon))
                .build()
            pantryList.add(PantryItem(uri, it.name))
        }
        return pantryList
    }

    fun getMyPantryItems(): MutableList<MyListItem>? {
        val list: MutableList<MyListItem> = mutableListOf()
        remote.toolConfig()?.pantryConfig?.products?.forEach {
            val set = mutableListOf<String>()
            it.set.forEach { inngrdnt ->
                if (myIngredientsList.contains(inngrdnt)) {
                    set.add(inngrdnt)
                }
            }
            if (set.isNotEmpty()) {
                list.add(MyListItem(it.name, set))
            }
        }
        return list
    }

    fun updateFavoritesList(favorite: MenuItem?) {
        if (favoriteRecipesList.contains(favorite)) {
            favoriteRecipesList.remove(favorite)
        } else {
            favorite?.let { favoriteRecipesList.add(it) }
        }
    }

    fun getSpinnerItems(): MutableList<SpinnerItem> {
        remote.toolConfig()?.pantryConfig?.products?.forEach {
            it.set.forEach { item ->
                if (spinnerItemsList.isNotEmpty()) {
                    if (spinnerItemsList.none { spinnerItemsList -> spinnerItemsList.text == item.replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(
                                Locale.getDefault()
                            ) else char.toString()
                        } }) {
                        spinnerItemsList.add(SpinnerItem(item.replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(
                                Locale.getDefault()
                            ) else char.toString()
                        }, myIngredientsList.contains(item)))
                    }
                } else spinnerItemsList.add(SpinnerItem(item.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(
                        Locale.getDefault()
                    ) else char.toString()
                }, myIngredientsList.contains(item)))
            }
        }
        return spinnerItemsList
    }

    fun updateSpinnerItems() {
        myIngredientsList.forEach { ingrd ->
            spinnerItemsList.find { it.text == ingrd }?.added = true
        }
    }

    fun updatePantryListState() {
        pantryList.find { pantryItem -> pantryItem.title == currentPantryName }?.added = checkedChipsList.isNotEmpty()
    }

    fun updateMyListState() {
        val addedList = mutableListOf<String>()
        remote.toolConfig()?.pantryConfig?.products?.forEach {
            it.set.forEach { inngrdnt ->
                if (myList.value?.contains(inngrdnt) == true || myIngredientsList.contains(inngrdnt)) {
                    if (!addedList.contains(it.name)) addedList.add(it.name)
                }
            }
        }
        pantryList.forEach { it.added = false }

        addedList.forEach { addedName ->
            pantryList.find { it.title == addedName }?.added = true
        }
    }

    fun addToMyList(items: List<String>) {
        items.forEach {
            if (!myIngredientsList.contains(it)) {
                myIngredientsList.add(it)
            }
        }
        _myList.postValue(myIngredientsList)
    }

    fun deleteFromMyList(item: String) {
        if (myIngredientsList.contains(item)) {
            myIngredientsList.remove(item)
        }
        _myList.postValue(myIngredientsList)
    }

    fun addChip(chip: String) {
        checkedChipsList.add(chip)
        _previewPageFlow.postValue(checkedChipsList)
    }

    fun removeChip(chip: String) {
        checkedChipsList.remove(chip)
        _previewPageFlow.postValue(checkedChipsList)
    }
    // need to be changed
    fun isSignedIn() = false

}

class MainViewModelFactory(val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Context::class.java).newInstance(context)
    }

}