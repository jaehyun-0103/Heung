package com.example.heung

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager

class CalDialog(context: Context)
{
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.activity_calwrite)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

    }
    fun showDialog2()
    {
        dialog.setContentView(R.layout.item_calserch)
        dialog.getWindow()?.setGravity(Gravity.TOP);
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}