package com.example.dailycook.model

import com.google.gson.annotations.SerializedName

data class SettingsConfig(
    @SerializedName("login") val loginConfig: LoginConfig,
    @SerializedName("pantry") val pantryConfig: PantryConfig,
    @SerializedName("menu") val menuConfig: MenuConfig,
    @SerializedName("favorites") val favoritesConfig: FavoritesConfig,
    @SerializedName("my_pantry") val myPantry: MyPantry
)

data class LoginConfig(
    @SerializedName("email_hint") val email: String,
    @SerializedName("password_hint") val password: String,
    @SerializedName("verify_hint") val verifyPass: String,
    @SerializedName("forgot_password") val forgotPass: String,
    @SerializedName("login_button") val login: String,
    @SerializedName("sign_up_text") val signUpText: String,
    @SerializedName("sign_up") val signUp: String,
    @SerializedName("start") val start: String
)

data class PantryConfig(
    @SerializedName("button_title") val title: String,
    @SerializedName("title_text_1") val titleText1: String,
    @SerializedName("title_text_2") val titleText2: String,
    @SerializedName("search_hint") val searchHint: String,
    @SerializedName("apply_button") val applyBtn: String,
    @SerializedName("recipes_action_button") val recipesButton: String,
    @SerializedName("pantry_action_button") val pantryButton: String,
    @SerializedName("products") val products: List<Product>
)

data class Product(
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("set") val set: List<String>,
)

data class MenuConfig(
    @SerializedName("button_title") val button: String,
    @SerializedName("title") val title: String,
    @SerializedName("title_second") val titleSecond: String,
)

data class FavoritesConfig(
    @SerializedName("no_recipes") val noRecipesText: String,
    @SerializedName("title") val title: String,
    @SerializedName("title_second") val titleSecond: String,
    @SerializedName("title_third") val titleThird: String,
    @SerializedName("action_button") val actionButton: String,
    @SerializedName("action_button_second") val actionButtonSecond: String
)

data class MyPantry(
    @SerializedName("title") val title: String,
    @SerializedName("sub_title") val subTitle: String,
    @SerializedName("no_ingredients") val noItems: String
)