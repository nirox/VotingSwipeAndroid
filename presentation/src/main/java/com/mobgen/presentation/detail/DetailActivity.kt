package com.mobgen.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import com.mobgen.presentation.BaseActivity
import com.mobgen.presentation.R
import com.mobgen.presentation.load
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {
    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE_URL = "imageUrl"
        private const val ARG_DATE = "date"
        private const val ARG_TRANSITION = "transitionName"
        fun newInstance(
            context: Context,
            name: String,
            urlImage: String,
            description: String,
            date: String,
            transitionName: String
        ) = Intent(context, DetailActivity::class.java).apply {
            putExtra(ARG_NAME, name)
            putExtra(ARG_DESCRIPTION, description)
            putExtra(ARG_IMAGE_URL, urlImage)
            putExtra(ARG_DATE, date)
            putExtra(ARG_TRANSITION, transitionName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        postponeEnterTransition()
        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun initView() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        window.sharedElementEnterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)
            .apply {
                imageView.apply {
                    transitionName = intent.getStringExtra(ARG_TRANSITION)
                    load(intent.getStringExtra(ARG_IMAGE_URL)) {
                        startPostponedEnterTransition()
                    }
                }
            }
        window.enterTransition = Fade().apply {
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            excludeTarget(R.id.action_bar_container, true)
        }
    }


}