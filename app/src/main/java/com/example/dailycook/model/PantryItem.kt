package com.example.dailycook.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri

data class PantryItem(val image: Uri, val title: String, var added: Boolean = false)