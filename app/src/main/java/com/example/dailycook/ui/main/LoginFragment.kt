package com.example.dailycook.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dailycook.R
import com.example.dailycook.databinding.FragmentLoginBinding

class LoginFragment: Fragment(R.layout.fragment_login) {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private var binding: FragmentLoginBinding? = null
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        if (savedInstanceState != null) {
            binding?.verify?.visibility = savedInstanceState.getInt("verifyVisibility")
            viewModel.currentPantryName = savedInstanceState.getString("currentPantryName")
            viewModel.myIngredientsList = savedInstanceState.getStringArrayList("ingredientsList")?.toMutableList() ?: emptyList<String>().toMutableList()
            viewModel.addToMyList(viewModel.myIngredientsList)
        }
        setUpLoginPage()
        if (binding?.verify?.visibility == View.VISIBLE) {
            showSignUp()
        }
        binding?.btnBackLogin?.setOnClickListener {
            onBackPressed()
        }

        binding?.signUp?.setOnClickListener {
            showSignUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (onBackPressed()) activity?.onBackPressed()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("verifyVisibility", binding?.verify?.visibility ?: View.GONE)
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
        if (binding?.verify?.visibility == View.VISIBLE) {
            hideSignUp()
        } else {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, when (viewModel.loginOpenedFrom) {
                    FAVORITES -> FavoritesFragment.newInstance()
                    MENU -> MenuFragment.newInstance()
                    else -> MainFragment.newInstance()
                })
                .commitNow()
            hide()
        }
        return false
    }

    private fun showSignUp() {
        binding?.verify?.visibility = View.VISIBLE
        binding?.signUpLayout?.visibility = View.GONE
        binding?.forgot?.visibility = View.GONE
        binding?.signInBtn?.text = viewModel.remote.toolConfig()?.loginConfig?.start
    }

    private fun hideSignUp() {
        binding?.verify?.visibility = View.GONE
        binding?.signUpLayout?.visibility = View.VISIBLE
        binding?.forgot?.visibility = View.VISIBLE
        binding?.signInBtn?.text = viewModel.remote.toolConfig()?.loginConfig?.login
    }

    private fun setUpLoginPage() {
        binding?.email?.hint = viewModel.remote.toolConfig()?.loginConfig?.email
        binding?.password?.hint = viewModel.remote.toolConfig()?.loginConfig?.password
        binding?.verify?.hint = viewModel.remote.toolConfig()?.loginConfig?.verifyPass
        binding?.forgot?.text = viewModel.remote.toolConfig()?.loginConfig?.forgotPass
        binding?.signUpTv?.text = viewModel.remote.toolConfig()?.loginConfig?.signUpText
        binding?.signUp?.text = viewModel.remote.toolConfig()?.loginConfig?.signUp
        binding?.signInBtn?.text = viewModel.remote.toolConfig()?.loginConfig?.login
    }
}