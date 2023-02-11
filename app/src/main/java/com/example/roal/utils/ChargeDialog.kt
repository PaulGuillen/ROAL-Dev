package com.example.roal.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.example.roal.R

object ChargeDialog {

    fun showLoadingDialog(context:Context):Dialog{
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.charge_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)

            return it
        }
    }

}
