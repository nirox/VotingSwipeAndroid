package com.mobgen.presentation.swipe

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.util.DisplayMetrics
import android.view.View
import com.mobgen.presentation.BaseActivity
import com.mobgen.presentation.BaseViewModel
import com.mobgen.presentation.R
import com.mobgen.presentation.ViewModelFactory
import com.mobgen.presentation.detail.DetailActivity
import com.mobgen.presentation.login.LoginActivity
import com.mobgen.presentation.swipe.swipeView.SwipeViewManager
import kotlinx.android.synthetic.main.activity_swipe.*
import javax.inject.Inject


class SwipeActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SwipeViewModel

    private lateinit var swipeViewManager: SwipeViewManager

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, SwipeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SwipeViewModel::class.java)

        viewModel.data.observe(this, Observer {
            it?.let { data ->

                when (data.status) {
                    BaseViewModel.Status.LOADING -> {
                        changeProgressBarVisibility(true)
                    }
                    BaseViewModel.Status.SUCCESS -> {
                        val metrics = DisplayMetrics()
                        windowManager.defaultDisplay.getMetrics(metrics)
                        swipeViewManager = SwipeViewManager(
                            containerTop,
                            containerBottom,
                            it.usersViewData,
                            metrics.widthPixels / 2f,
                            object : SwipeViewManager.Listener() {
                                override fun onSwipe(position: Int) {
                                    viewModel.addLike(position)
                                }

                                override fun onCardClick(position: Int) {
                                    goDetail(viewModel.getUserBindView(position))
                                }
                            })
                        changeProgressBarVisibility(false)
                    }
                    BaseViewModel.Status.ERROR -> {
                        createToast(getString(R.string.errorLoadUsers))
                        changeProgressBarVisibility(false)
                    }
                }
            }
        })

        initView()
        iniListeners()
    }

    private fun iniListeners() {
        like.setOnClickListener {
            swipeViewManager.swipe()
        }

        noLike.setOnClickListener {
            swipeViewManager.rewind()
        }
        settings.setOnClickListener{
            startActivity(LoginActivity.newInstance(this))
            finish()
        }
    }

    private fun initView() {
        viewModel.init()
    }

    private fun changeProgressBarVisibility(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
        progressBar.isIndeterminate = visible
    }

    private fun goDetail(userBindView: SwipeViewModel.UserBindView) {
        val topContainer = swipeViewManager.getTopView()
        val imageViewTop = if (topContainer == containerTop) imageViewTop else imageViewBottom
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageViewTop, imageViewTop.transitionName)
                .toBundle()
        startActivity(
            DetailActivity.newInstance(
                this, userBindView.name,
                userBindView.photo.first(),
                userBindView.description,
                userBindView.date,
                imageViewTop.transitionName
            ),
            options
        )
    }

}
