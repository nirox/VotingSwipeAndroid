package com.mobgen.presentation.register

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.mobgen.domain.addSuffix
import com.mobgen.presentation.BaseViewModel
import com.mobgen.presentation.R
import com.mobgen.presentation.Util
import com.mobgen.presentation.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.register_main.*
import javax.inject.Inject


class RegisterActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: RegisterViewModel
    lateinit var file: Uri
    private var photoList = mutableListOf<String>()

    companion object {
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASS = "password"
        const val SUFFIX_FIELDS_NAMES = ":"
        const val MIN_CHARS = 6
        const val GET_FROM_GALLERY = 100
        const val GET_FROM_CAMERA = 101
        const val CAMERA_PERMISSON = 1
        fun newInstance(context: Context): Intent = Intent(context, RegisterActivity::class.java)
        const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)

        initView()
        initListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initListener() {
        registerButton.setOnClickListener {
            viewModel.registration(
                name.text.toString(),
                date.text.toString(),
                emailReg.text.toString(),
                passwordReg.text.toString(),
                description.text.toString(),
                photoList.toList()
            )
        }

        photoUpload.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), GET_FROM_GALLERY
            )
        }

        photoTake.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), CAMERA_PERMISSON
                )
            } else {
                openCamera()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSON -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    openCamera()
                }
                return
            }
        }
    }

    private fun openCamera() {
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = Uri.fromFile(Util.getOutputMediaFile(getString(R.string.app_name)))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file)
        startActivityForResult(intent, GET_FROM_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                photoList.add(Util.getPathFromURI(it, contentResolver))
                photo.setImageURI(it)
            }
        }

        if (requestCode == GET_FROM_CAMERA && resultCode == RESULT_OK) {
            photo.setImageURI(file)
            photoList.add(file.path ?: "")
        }
    }

    private fun initView() {
        buttonBackInActionBar(true)
        nameText.text = getString(R.string.name).addSuffix(SUFFIX_FIELDS_NAMES)
        dateText.text = getString(R.string.date).addSuffix(SUFFIX_FIELDS_NAMES)
        emailText.text = getString(R.string.email).addSuffix(SUFFIX_FIELDS_NAMES)
        passwordText.text = getString(R.string.password).addSuffix(SUFFIX_FIELDS_NAMES)
        descriptionText.text = getString(R.string.description).addSuffix(SUFFIX_FIELDS_NAMES)
        photoText.text = getString(R.string.photo).addSuffix(SUFFIX_FIELDS_NAMES)

        viewModel.data.observe(this, Observer {
            it?.let { data ->

                when (data.status) {
                    BaseViewModel.Status.LOADING -> {
                        changeVisiblility(true)
                    }
                    BaseViewModel.Status.SUCCESS -> {
                        changeVisiblility(false)
                        setResult(Activity.RESULT_OK, Intent(Intent.ACTION_SEND).apply {
                            putExtra(EXTRA_EMAIL, emailReg.text.toString())
                            putExtra(EXTRA_PASS, passwordReg.text.toString())
                        })
                        finish()
                    }
                    else -> {
                        changeVisiblility(false)
                        hideKeyboard()
                        data.error?.let { error ->
                            showError(error)
                        }
                    }
                }
            }
        })
    }

    private fun changeVisiblility(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
        progressBar.isIndeterminate = visible
    }

    private fun buttonBackInActionBar(check: Boolean) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(check)
            setDisplayShowHomeEnabled(check)
        }
    }

    private fun createToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                it.windowToken,
                0
            )
        }
    }

    private fun showError(errorRegister: RegisterViewModel.ErrorRegister) {
        when (errorRegister) {
            RegisterViewModel.ErrorRegister.NAME -> createToast(
                String.format(
                    getString(R.string.fill),
                    getString(R.string.name).toLowerCase(),
                    ""
                )
            )
            RegisterViewModel.ErrorRegister.DATE -> createToast(
                String.format(
                    getString(R.string.fill),
                    getString(R.string.date).toLowerCase(),
                    getString(R.string.slashes).toLowerCase()
                )
            )
            RegisterViewModel.ErrorRegister.EMAIL -> createToast(
                String.format(
                    getString(R.string.fill),
                    getString(R.string.email).toLowerCase(),
                    getString(R.string.symbols).toLowerCase()
                )
            )
            RegisterViewModel.ErrorRegister.PASSWORD -> createToast(
                String.format(
                    getString(R.string.fill),
                    getString(R.string.password).toLowerCase(),
                    getString(R.string.minChars).toLowerCase()
                )
            )
            RegisterViewModel.ErrorRegister.DESCRIPTION -> createToast(
                String.format(
                    getString(R.string.fill),
                    getString(R.string.description).toLowerCase(),
                    ""
                )
            )
            RegisterViewModel.ErrorRegister.NOT_REGISTERED -> createToast(
                getString(R.string.notRegistered)
            )
            RegisterViewModel.ErrorRegister.USER_EXISTS -> createToast(
                getString(R.string.userExists)
            )
            RegisterViewModel.ErrorRegister.NOT_KEY -> createToast(
                getString(R.string.notKey)
            )
        }
    }

}