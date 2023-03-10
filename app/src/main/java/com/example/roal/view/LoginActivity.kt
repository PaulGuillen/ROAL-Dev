package com.example.roal.view

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roal.databinding.ActivityLoginBinding
import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import com.example.roal.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import com.example.roal.utils.ChargeDialog


class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    lateinit var binding: ActivityLoginBinding
    var usersProvider = UsersProvider()
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.i("OnCreate")
        binding.btnLogin.setOnClickListener { goToHomeView() }
        binding.textForgotPassword.setOnClickListener { gotToForgotPasswordView() }
    }

    private fun goToHomeView() {
        val email = binding.textEmail.text.toString()
        val password = binding.textPassword.text.toString()
        val action = "login"

        val mainUser = MainUser(
            email = email,
            password = password,
            action = action
        )
        if (isValidForm(email, password)) {
            showLoading()
            usersProvider.login(mainUser)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.code == 200) {
                        hideLoading()
                        goToHomeDashboard()
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()
                    } else {
                        hideLoading()
                        Toast.makeText(this@LoginActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                    hideLoading()
                    Toast.makeText(this@LoginActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this@LoginActivity, "Datos no validos", Toast.LENGTH_LONG).show()
        }
    }

    private fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(email: String, password: String): Boolean {

        if (email.isBlank()) {
            return false
        }
        if (password.isBlank()) {
            return false
        }
        if (!email.isEmailValid()) {
            return false
        }
        return true
    }

    private fun goToHomeDashboard() {
        val i = Intent(this, HomeActivity::class.java)
        val preferences = getSharedPreferences("temp", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("name", binding.textEmail.text.toString())
        editor.apply()
        Timber.i("SharedPref = Inicializado")
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun gotToForgotPasswordView() {
        val i = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(i)
    }

}