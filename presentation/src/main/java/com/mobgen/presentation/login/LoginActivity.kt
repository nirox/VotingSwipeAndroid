package com.mobgen.presentation.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.mobgen.presentation.BaseViewModel
import com.mobgen.presentation.R
import com.mobgen.presentation.ViewModelFactory
import com.mobgen.presentation.register.RegisterActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.login_main.*
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: LoginViewModel

    companion object {
        const val CODE_REQUEST_REGISTER = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        loginButton.setOnClickListener {
            viewModel.authenticate(email.text.toString(), password.text.toString())
        }

        goToRegister.setOnClickListener {
            startActivityForResult(RegisterActivity.newInstance(this),
                CODE_REQUEST_REGISTER
            )
        }

        viewModel.data.observe(this, Observer {
            it?.let { data ->

                when (data.status) {
                    BaseViewModel.Status.LOADING -> {
                        changeVisiblility(true)
                    }
                    BaseViewModel.Status.SUCCESS -> {
                        changeVisiblility(false)
                        //TODO go to next activity, user authenticated
                    }
                    BaseViewModel.Status.ERROR -> {
                        changeVisiblility(false)
                        hideKeyboard()
                        Toast.makeText(this, data.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_REQUEST_REGISTER && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.authenticate(
                    it.getStringExtra(RegisterActivity.EXTRA_EMAIL),
                    it.getStringExtra(RegisterActivity.EXTRA_PASS)
                )
            }
        }
    }

    private fun changeVisiblility(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
        progressBar.isIndeterminate = visible
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                it.windowToken,
                0
            )
        }
    }
}
