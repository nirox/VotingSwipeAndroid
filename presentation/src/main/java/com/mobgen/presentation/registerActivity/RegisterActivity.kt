package com.mobgen.presentation.registerActivity

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mobgen.presentation.R
import com.mobgen.presentation.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.register_main.*
import javax.inject.Inject


class RegisterActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: RegisterViewModel

    companion object {
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASS = "password"

        fun newInstance(context: Context): Intent = Intent(context, RegisterActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)

        initListener()
    }

    private fun initListener() {
        registerButton.setOnClickListener {

            setResult(Activity.RESULT_OK, Intent(Intent.ACTION_SEND).apply {
                putExtra(EXTRA_EMAIL, emailReg.text.toString())
                putExtra(EXTRA_PASS, passwordReg.text.toString())
            })
            finish()

            viewModel.registration(
                name.text.toString(),
                date.text.toString(),
                emailReg.text.toString(),
                passwordReg.text.toString(),
                description.text.toString()
            )
        }
    }
}