package com.example.roal.view

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.roal.databinding.ActivityNewPasswordBinding
import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import com.example.roal.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.roal.utils.ChargeDialog

class NewPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewPasswordBinding
    private var usersProvider = UsersProvider()
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnActualizar.setOnClickListener { sendingNewPassword() }
        binding.btnBack.setOnClickListener { backView() }
    }

    private fun sendingNewPassword() {

        val newPassword = binding.textPassword.text.toString()
        val confirmNewPassword = binding.textConfirmPassword.text.toString()
        val action = "change_password"
        val email = intent.getStringExtra("email")

        val mainUser = MainUser(
            new_password = newPassword,
            email = email,
            action = action
        )

        if (isValidForm(newPassword,confirmNewPassword)) {
            showLoading()
            usersProvider.resetPassword(mainUser)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.code == 200) {
                        hideLoading()
                        goToHomeDashboard()
                        Toast.makeText(this@NewPasswordActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    } else {
                        hideLoading()
                        Toast.makeText(this@NewPasswordActivity, "Contraseña no permitida", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                    hideLoading()
                    Toast.makeText(this@NewPasswordActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

    private fun isValidForm(
        password: String,
        confirmPassword: String
    ): Boolean {

        if (password.isBlank()) {
            Toast.makeText(this, "Debes ingresar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmPassword.isBlank()) {
            Toast.makeText(this, "Debes ingresar el la confirmación de contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun goToHomeDashboard() {
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }


    private fun backView() {
        val i = Intent(this, ForgotPasswordActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        backView()
    }
}