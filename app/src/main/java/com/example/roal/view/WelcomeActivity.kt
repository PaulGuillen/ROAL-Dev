package com.example.roal.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.roal.R
import com.example.roal.databinding.ActivityMainBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ROAL)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnIngresar.setOnClickListener {goToLoginActivity()}
    }

    private fun goToLoginActivity() {
        val i = Intent(this@WelcomeActivity, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

//
//    private fun getUserFromSession() {
//        val sharedPref = SharedPref(this)
//        if (!sharedPref.getData("user").isNullOrBlank()) {
//            goToHomeDashboard()
//        }
//    }

    private fun goToHomeDashboard() {
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}
