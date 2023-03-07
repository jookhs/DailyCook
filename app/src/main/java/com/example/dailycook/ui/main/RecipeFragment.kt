package com.example.dailycook.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentMenuBinding
import com.example.dailycook.databinding.FragmentMenuItemBinding
import com.example.dailycook.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.ArrayList

class RecipeFragment: Fragment(R.layout.fragment_menu_item) {

    companion object {
        fun newInstance() = RecipeFragment()
    }

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireContext())
    }
    private lateinit var ingrddapter: IngrdAdapter
    private var binding: FragmentMenuItemBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuItemBinding.bind(view)

        if (savedInstanceState != null) {
            viewModel.currentRecipeName = savedInstanceState.getString("currentRecipeName")
            viewModel.currentPantryName = savedInstanceState.getString("currentPantryName")
            viewModel.myIngredientsList = savedInstanceState.getStringArrayList("ingredientsList")?.toMutableList() ?: emptyList<String>().toMutableList()
            viewModel.addToMyList(viewModel.myIngredientsList)

        }
        ingrddapter = IngrdAdapter(viewModel.getCurrentRecipe()?.ingrd)
        binding?.ingrdList?.apply {
            adapter = ingrddapter
        }
        binding?.btnBackMenuItem?.setOnClickListener {
            onBackPressed()
        }
        binding?.btnSaveMenuItem?.setOnClickListener {
            viewModel.updateFavoritesList(viewModel.getCurrentRecipe())
            binding?.btnSaveMenuItem?.isSelected = binding?.btnSaveMenuItem?.isSelected != true
            val text = if (binding?.btnSaveMenuItem?.isSelected != true) {
                " removed from Favorites"
            } else {
                " added to Favorites"
            }
            Toast.makeText(
                requireContext(),
                viewModel.currentRecipeName + text,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding?.btnSaveMenuItem?.isSelected = viewModel.favoriteRecipesList.find { it.title ==  viewModel.getCurrentRecipe()?.title } != null
        updateUI()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (onBackPressed()) activity?.onBackPressed()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val sheetBehavior = BottomSheetBehavior.from(binding?.menuItemContentLayout!!)
            sheetBehavior.isDraggable = false
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPantryName", viewModel.currentPantryName)
        outState.putString("currentRecipeName", viewModel.currentRecipeName)
        outState.putStringArrayList("ingredientsList", viewModel.myIngredientsList as? ArrayList<String>)
    }

    private fun updateUI() {
        binding?.menuItemIcon?.setImageURI(viewModel.getCurrentRecipe()?.image)
        binding?.menuItemTitle?.text = viewModel.getCurrentRecipe()?.title
        binding?.timeTv?.text = viewModel.getCurrentRecipe()?.prepTime.toString() + "m"
        binding?.ingrdTv?.text = viewModel.getCurrentRecipe()?.ingrd?.size.toString()
        binding?.descriptionTitle?.text = "Description"
        binding?.ingredients?.text = "Ingredients"
        binding?.description?.text = viewModel.getCurrentRecipe()?.desc
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
        if (viewModel.recipeOpenedFrom == FAVORITES) {
            openFav()
        } else {
            openMenu()
        }
        return false
    }

    private fun openMenu() {
        viewModel.loginOpenedFrom = MENU
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, MenuFragment.newInstance())
            .commitNow()
        hide()
    }

    private fun openFav() {
        viewModel.loginOpenedFrom = MENU
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, FavoritesFragment.newInstance())
            .commitNow()
        hide()
    }

}