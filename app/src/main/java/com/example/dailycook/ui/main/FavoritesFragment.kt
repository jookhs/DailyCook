package com.example.dailycook.ui.main

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentFavoritesBinding
import com.example.dailycook.databinding.FragmentMyListBinding
import com.example.dailycook.model.MenuItem
import com.example.dailycook.model.SpinnerItem
import java.util.*

class FavoritesFragment: Fragment(R.layout.fragment_favorites), MainAdapterClickListener {
    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireContext())
    }
    private lateinit var favoritesAdapter: FavoritesAdapter
    private var binding: FragmentFavoritesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritesBinding.bind(view)

        if (savedInstanceState != null) {
            viewModel.currentPantryName = savedInstanceState.getString("currentPantryName")
            viewModel.myIngredientsList = savedInstanceState.getStringArrayList("ingredientsList")?.toMutableList() ?: emptyList<String>().toMutableList()
            viewModel.addToMyList(viewModel.myIngredientsList)
        }
        favoritesAdapter = FavoritesAdapter(viewModel.favoriteRecipesList, this)
        binding?.favoritesRcView?.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = favoritesAdapter
        }
        binding?.btnBackFavorite?.setOnClickListener {
            onBackPressed()
        }
        favoritesAdapter.setItems(viewModel.favoriteRecipesList)
        updateUI()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (onBackPressed()) activity?.onBackPressed()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPantryName", viewModel.currentPantryName)
        outState.putStringArrayList("ingredientsList", viewModel.myIngredientsList as? ArrayList<String>)
    }

    private fun updateUI() {
        binding?.notRegistered?.visibility = if (favoritesAdapter.itemCount == 0) View.VISIBLE else View.GONE
        binding?.signUp?.visibility = if (favoritesAdapter.itemCount == 0) View.VISIBLE else View.GONE
        binding?.signUp?.text = viewModel.remote.toolConfig()?.favoritesConfig?.actionButton
        binding?.favoritesTitle?.text = viewModel.remote.toolConfig()?.favoritesConfig?.title
        binding?.notRegisteredText?.text = viewModel.remote.toolConfig()?.favoritesConfig?.titleSecond
        // if registered need to show no recipes saved
        binding?.favoritesSubTitle?.text = String.format(viewModel.remote.toolConfig()?.favoritesConfig?.noRecipesText ?: "", viewModel.favoriteRecipesList.size)
    }

    override fun onHeartClicked(item: MenuItem?) {
        viewModel.updateFavoritesList(item)
        favoritesAdapter.setItems(viewModel.favoriteRecipesList)
        favoritesAdapter.notifyDataSetChanged()
        updateUI()
    }

    override fun onMenuItemClicked(item: MenuItem?) {
        super.onMenuItemClicked(item)
    }

    private fun hide() {
        activity ?: return
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .hide(this)
            .commitAllowingStateLoss()
    }

    fun onBackPressed(): Boolean {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
        hide()
        return false
    }
}