package com.example.roal.view.perfil_admin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.roal.R
import com.example.roal.databinding.ActivityPerfilBinding
import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import com.example.roal.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.ChargeDialog

@SuppressLint("SourceLockedOrientationActivity")
class PerfilActivity : AppCompatActivity() {

    lateinit var binding: ActivityPerfilBinding
    private var usersProvider = UsersProvider()
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        binding.include.toolbar.setTitleTextAppearance(this, R.style.titulosNavbar)
        binding.include.toolbar.title = "Perfil de Usuario"
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getMainUserData()
    }

    private fun getMainUserData() {
        showLoading()
        val email = intent.getStringExtra("email").toString()
        usersProvider.getMainData(email)?.enqueue(object  : Callback<MainUser> {
            override fun onResponse(call: Call<MainUser>, response: Response<MainUser>) {
                if(response.isSuccessful){
                    hideLoading()
                    val textDNI = response.body()?.dni
                    val textEmail = response.body()?.email
                    val textName = response.body()?.name
                    val textLastName = response.body()?.lastname
                    val textFullName = "$textName $textLastName"
                    val textCreateAccount = response.body()?.createDateAccount
                    val textPassword = response.body()?.password

                    binding.textFullName.text = textFullName
                    binding.textDNI.text = textDNI
                    binding.textJoinCreateAccount.setText(textCreateAccount)
                    binding.textPassword.setText(textPassword)
                    binding.textEmail.setText(textEmail)
                }else{
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<MainUser>, t: Throwable) {
                hideLoading()
            }
        })
    }

    private fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

}