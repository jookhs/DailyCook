package com.example.dailycook.model

import com.google.gson.annotations.SerializedName

data class SettingsConfig(
    @SerializedName("login") val loginConfig: LoginConfig,
    @SerializedName("pantry") val pantryConfig: PantryConfig,
    @SerializedName("menu") val menuConfig: MenuConfig,
    @SerializedName("favorites") val favoritesConfig: FavoritesConfig
)

data class LoginConfig(
    @SerializedName("email_hint") val email: String,
    @SerializedName("password_hint") val password: String,
    @SerializedName("verify_hint") val verifyPass: String,
    @SerializedName("forgot_password") val forgotPass: String,
    @SerializedName("login_button") val login: String,
    @SerializedName("sign_up") val signUp: String,
    @SerializedName("start") val start: String
)

data class PantryConfig(
    @SerializedName("button_title") val title: String,
    @SerializedName("title_text") val titleText: String,
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
    @SerializedName("button_title") val button: String,
    @SerializedName("title") val title: String,
    @SerializedName("title_second") val titleSecond: String,
    @SerializedName("action_button") val actionButton: String,
    @SerializedName("action_button_second") val actionButtonSecond: String
)