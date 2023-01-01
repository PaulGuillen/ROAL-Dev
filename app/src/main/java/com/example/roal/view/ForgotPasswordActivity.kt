package com.example.roal.view

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.roal.databinding.ActivityForgotPasswordBinding
import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import com.example.roal.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    var usersProvider = UsersProvider()
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRecuperarContrasena.setOnClickListener { recoveryPassword() }
        binding.btnBack.setOnClickListener { backView() }
    }

    private fun recoveryPassword() {
        val email = binding.textEmail.text.toString() // NULL POINTER EXCEPTION
        val action = "recuperar_contraseña"

        val mainUser = MainUser(
            email = email,
            action = action
        )

        if (isValidForm(email)) {
            usersProvider.recoveryPassword(mainUser)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.code == 200) {
                        goToCodeVerification()
                        Toast.makeText(this@ForgotPasswordActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@ForgotPasswordActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ForgotPasswordActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this@ForgotPasswordActivity, "Datos no validos", Toast.LENGTH_LONG).show()
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(email: String): Boolean {
        if (email.isBlank()) {
            return false
        }
        if (!email.isEmailValid()) {
            return false
        }
        return true
    }

    private fun goToCodeVerification() {
        val email = binding.textEmail.text.toString()
        val i = Intent(this, CodeVerificationActivity::class.java)
        i.putExtra("email", email)
        startActivity(i)
    }

    private fun backView() {
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}