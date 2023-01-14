package com.example.roal.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roal.databinding.ActivityHomeBinding
import com.example.roal.view.management_worker.ManagementWorkerActivity
import com.example.roal.view.perfil_admin.PerfilActivity

@SuppressLint("SourceLockedOrientationActivity")
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectOption()
    }

    private fun selectOption(){
        binding.cardviewMantenimiento?.setOnClickListener {
            val i = Intent(applicationContext, ManagementWorkerActivity::class.java)
            startActivity(i)
        }

        binding.cardviewPerfil.setOnClickListener {
            val i = Intent(applicationContext, PerfilActivity::class.java)
            startActivity(i)
        }
    }
}
