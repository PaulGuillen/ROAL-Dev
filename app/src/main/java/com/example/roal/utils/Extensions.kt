package com.example.roal.utils


import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.roal.R

fun toolbarStyle(context: Context, toolbar: Toolbar,title : String){
    toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.black))
    toolbar.setTitleTextAppearance(context, R.style.titulosNavbar)
    toolbar.title = title
}

