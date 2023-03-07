package com.example.dailycook.model

import android.net.Uri

data class MenuItem (val image: Uri, val title: String, var favorite: Boolean = false, var viewType: Int, var ingrd: List<String>?, var desc: String?, var prepTime: Int?)