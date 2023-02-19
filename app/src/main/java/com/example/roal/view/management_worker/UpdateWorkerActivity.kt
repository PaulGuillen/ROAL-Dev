package com.example.roal.view.management_worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roal.R
import com.example.roal.databinding.ActivityManagementWorkerBinding
import com.example.roal.databinding.ActivityUpdateWorkerBinding
import com.example.roal.utils.toolbarStyle

class UpdateWorkerActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateWorkerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(this@UpdateWorkerActivity,binding.include.toolbar,"Actualización de datos")
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}