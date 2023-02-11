package com.example.roal.view.settings

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.roal.R
import com.example.roal.databinding.ActivityPerfilBinding
import com.example.roal.databinding.ActivitySettingsBinding
import com.example.roal.utils.toolbarStyle

@SuppressLint("SourceLockedOrientationActivity")
class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarStyle(this@SettingsActivity,binding.include.toolbar, "Ajustes")
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}