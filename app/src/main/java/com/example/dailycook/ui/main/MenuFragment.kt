package com.example.dailycook.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dailycook.MainAdapterClickListener
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentMenuBinding
import com.example.dailycook.model.MenuItem
import java.util.*

class MenuFragment: Fragment(R.layout.fragment_menu), MainAdapterClickListener {
    companion object {
        fun newInstance() = MenuFragment()
    }

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireContext())
    }
    private lateinit var menuAdapter: MenuAdapter
    private var binding: FragmentMenuBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        if (savedInstanceState != null) {
            viewModel.currentPantryName = savedInstanceState.getString("currentPantryName")
            viewModel.myIngredientsList = savedInstanceState.getStringArrayList("ingredientsList")?.toMutableList() ?: emptyList<String>().toMutableList()
            viewModel.addToMyList(viewModel.myIngredientsList)
        }
        val layoutManager = StaggeredGridLayoutManager(if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2, LinearLayoutManager.VERTICAL)
        menuAdapter = MenuAdapter(viewModel.favoriteRecipesList, this)
        binding?.menuRcView?.apply {
            this.layoutManager = layoutManager
            adapter = menuAdapter
        }
        binding?.btnBackMenu?.setOnClickListener {
            onBackPressed()
        }
        binding?.profieMenu?.setOnClickListener {
            openLogin()
        }
        menuAdapter.setItems(viewModel.favoriteRecipesList)
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
        binding?.menuTitle?.text = viewModel.remote.toolConfig()?.menuConfig?.title
        binding?.menuSubTitle?.text = String.format(viewModel.remote.toolConfig()?.menuConfig?.titleSecond ?: "", viewModel.myIngredientsList.size)
    }

    override fun onHeartClicked(item: MenuItem?) {
        viewModel.updateFavoritesList(item)
        menuAdapter.setItems(viewModel.favoriteRecipesList)
        menuAdapter.notifyDataSetChanged()
        updateUI()
    }

    override fun onMenuItemClicked(item: MenuItem?) {
        super.onMenuItemClicked(item)
        viewModel.currentRecipeName = item?.title
        openRecipe()
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

    private fun openLogin() {
        viewModel.loginOpenedFrom = MENU
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance())
            .commitNow()
        hide()
    }

    private fun openRecipe() {
        viewModel.recipeOpenedFrom = MENU
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, RecipeFragment.newInstance())
            .commitNow()
        hide()
    }
}