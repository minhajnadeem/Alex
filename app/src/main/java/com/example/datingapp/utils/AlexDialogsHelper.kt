package com.example.datingapp.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.datingapp.R
import java.text.SimpleDateFormat
import java.util.*


interface LoginDialogListener {
    fun onClickLogin(email: String, password: String)
    fun onClickRegister()
}

interface RegisterDialogListener {
    fun onClickRegister(displayName: String, email: String, password: String,dob:Long)
    fun onClickLogin()
}

interface ChooserOnClick {
    fun onClickCamera()
    fun onClickGallery()
}

class AlexDialogsHelper {

    private var loginDialog: LoginDialog? = null
    private var registerDialog: RegisterDialog? = null

    fun showLoginEmailDialog(fragmentManager: FragmentManager, listener: LoginDialogListener) {
        loginDialog = LoginDialog(listener)
        loginDialog!!.show(fragmentManager, "Login")
    }

    fun hideLoginEmailDialog() {
        if (loginDialog == null)
            return
        loginDialog!!.dismiss()
    }

    fun hideLoginProgressBar() {
        if (loginDialog == null)
            return
        loginDialog!!.hideProgressBar()
    }

    fun hideRegisterProgressBar() {
        if (registerDialog == null)
            return
        registerDialog!!.hideProgressBar()
    }

    class LoginDialog(val listener: LoginDialogListener) : DialogFragment() {

        private lateinit var progressBar: ProgressBar

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(context)
            val layoutInflater = requireActivity().layoutInflater
            val view = layoutInflater.inflate(R.layout.dialog_login_email, null)
            initViews(view)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }

        private fun initViews(view: View?) {
            if (view != null) {
                progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.INVISIBLE
                val etEmail = view.findViewById<EditText>(R.id.etEmail)
                val etPassword = view.findViewById<EditText>(R.id.etPassword)
                val btnLogin = view.findViewById<Button>(R.id.btnLogin)
                btnLogin.setOnClickListener {
                    progressBar.visibility = View.VISIBLE
                    listener.onClickLogin(etEmail.text.toString(), etPassword.text.toString())
                }
                val tvNotRegistered = view.findViewById<TextView>(R.id.tvNotRegistered)
                tvNotRegistered.setOnClickListener {
                    dismiss()
                    listener.onClickRegister()
                }
            }
        }

        fun hideProgressBar() {
            progressBar.visibility = View.INVISIBLE
        }
    }

    fun showRegisterDialog(fragmentManager: FragmentManager, listener: RegisterDialogListener) {
        registerDialog = RegisterDialog(listener)
        registerDialog!!.show(fragmentManager, "Register")
    }

    fun showToastMessage(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    class RegisterDialog(val listener: RegisterDialogListener) : DialogFragment() {

        private lateinit var progressBar: ProgressBar
        val myCalendar:Calendar = Calendar.getInstance()
        private lateinit var editTextDob: EditText
        private var dateOfBirth:Long = 0

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(context)
            val layoutInflater = requireActivity().layoutInflater
            val view = layoutInflater.inflate(R.layout.dialog_register, null)
            initViews(view)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }

        var date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                dateOfBirth = myCalendar.timeInMillis
                updateLabel()
            }

        private fun initViews(view: View?) {
            if (view != null) {
                progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.INVISIBLE
                val btnLogin = view.findViewById<Button>(R.id.btnRegister)
                val etDisplayName = view.findViewById<EditText>(R.id.etDisplayName)
                val etPassword = view.findViewById<EditText>(R.id.etPassword)
                val etEmail = view.findViewById<EditText>(R.id.etEmail)
                val etDOB = view.findViewById<EditText>(R.id.etDOB)
                editTextDob=etDOB
                btnLogin.setOnClickListener {
                    progressBar.visibility = View.VISIBLE
                    listener.onClickRegister(
                        etDisplayName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        dateOfBirth
                    )
                }
                editTextDob.setOnClickListener {
                    DatePickerDialog(
                        it.context, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                        myCalendar[Calendar.DAY_OF_MONTH]
                    ).show()
                }
                val tvNotRegistered = view.findViewById<TextView>(R.id.tvAlreadyRegistered)
                tvNotRegistered.setOnClickListener {
                    dismiss()
                    listener.onClickLogin()
                }
            }
        }

        private fun updateLabel() {
            val myFormat = "MM/dd/yy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            editTextDob.setText(sdf.format(myCalendar.getTime()))
        }

        fun hideProgressBar() {
            progressBar.visibility = View.INVISIBLE
        }
    }

    fun hideRegisterEmailDialog() {
        if (registerDialog == null)
            return
        registerDialog!!.dismiss()
    }

    fun showChooser(context: Context, chooserOnClick: ChooserOnClick) {
        val adb = AlertDialog.Builder(context)
        val items =
            arrayOf<CharSequence>("Camera", "Gallery")
        adb.setItems(items, object : DialogInterface.OnClickListener {
            override fun onClick(d: DialogInterface?, n: Int) {
                if (n == 0) {
                    chooserOnClick.onClickCamera()
                } else {
                    chooserOnClick.onClickGallery()
                }
            }
        })
        adb.setNegativeButton("Cancel", null)
        adb.setTitle("Select Option")
        adb.show()
    }
}