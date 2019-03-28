package com.mobgen.presentation.swipe

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import com.mobgen.presentation.BaseViewModel
import com.mobgen.presentation.R
import com.mobgen.presentation.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_swipe.*
import javax.inject.Inject


class SwipeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SwipeViewModel

    private lateinit var swipeViewManager: SwipeViewManager

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, SwipeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SwipeViewModel::class.java)

        viewModel.data.observe(this, Observer {
            it?.let { data ->

                when (data.status) {
                    BaseViewModel.Status.LOADING -> {
                        //TODO
                    }
                    BaseViewModel.Status.SUCCESS -> {
                        //TODO
                    }
                    BaseViewModel.Status.ERROR -> {
                        //TODO
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
    }

    private fun initView() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)


        val list = listOf(
            SwipeViewModel.UserBindView(listOf("https://www.mainewomensnetwork.com/Resources/Pictures/vicki%20aqua%20headshot-smallmwn.jpg")),
            SwipeViewModel.UserBindView(listOf("https://firebasestorage.googleapis.com/v0/b/votingswipe.appspot.com/o/-LayG7v8_iWdbi088SX8%2Frivers.jpg?alt=media&token=5e552d03-c137-4bc0-9661-0487e795adea")),
            SwipeViewModel.UserBindView(listOf("https://firebasestorage.googleapis.com/v0/b/votingswipe.appspot.com/o/-Layg8IJv6XMslGedfT9%2FuserPhoto.jpg?alt=media&token=829a6e4e-ff19-4283-921c-5e7f7de1cd95")),
            SwipeViewModel.UserBindView(listOf("https://www.mainewomensnetwork.com/Resources/Pictures/vicki%20aqua%20headshot-smallmwn.jpg")),
            SwipeViewModel.UserBindView(listOf("https://firebasestorage.googleapis.com/v0/b/votingswipe.appspot.com/o/-LayG7v8_iWdbi088SX8%2Frivers.jpg?alt=media&token=5e552d03-c137-4bc0-9661-0487e795adea"))
        )

        swipeViewManager = SwipeViewManager(
            containerTop,
            containerBottom,
            list,
            metrics.widthPixels / 2f,
            object : SwipeViewManager.Listener() {
                override fun onSwipe(position: Int) {
                   viewModel.addLike(position)
                }
            })

    }

}
