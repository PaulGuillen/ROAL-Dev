package com.example.roal.view.emergency

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.roal.R
import com.example.roal.databinding.ActivityEmergencyBinding
import com.example.roal.databinding.ActivityPerfilBinding

class EmergencyActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmergencyBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        binding.include.toolbar.setTitleTextAppearance(this, R.style.titulosNavbar)
        binding.include.toolbar.title = "Emergencia"
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSearchByDNI.setOnClickListener { searchingByDNI() }
        binding.btnSearchByPhoto.setOnClickListener { searchingByPhoto() }
        binding.btnSearchByQR.setOnClickListener { searchingByQR() }
    }

    private fun searchingByDNI() {
        binding.searchBox.visibility = View.VISIBLE
        binding.imagePhoto.visibility = View.GONE
        binding.imageQR.visibility = View.GONE
        /**Si el DNI existe se muestra*/
      // binding.linearDNI.visibility = View.VISIBLE
        binding.btnSearch.visibility = View.VISIBLE
    }

    private fun searchingByPhoto() {
        binding.searchBox.visibility = View.GONE
        binding.imagePhoto.visibility = View.VISIBLE
        binding.imageQR.visibility = View.GONE
        /**Si el DNI existe se muestra*/
      //  binding.linearDNI.visibility = View.VISIBLE
        binding.btnSearch.visibility = View.VISIBLE
    }

    private fun searchingByQR() {
        binding.searchBox.visibility = View.GONE
        binding.imagePhoto.visibility = View.GONE
        binding.imageQR.visibility = View.VISIBLE
        /**Si el DNI existe se muestra*/
        //  binding.linearDNI.visibility = View.VISIBLE
        binding.btnSearch.visibility = View.VISIBLE
    }

}