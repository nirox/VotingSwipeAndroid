package com.mobgen.presentation

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity

open class BaseActivity : DaggerAppCompatActivity() {


    protected fun hideKeyboard() {
        currentFocus?.let {
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                it.windowToken,
                0
            )
        }
    }

    protected fun createToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}