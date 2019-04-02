package com.mobgen.presentation.swipe.detail

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobgen.presentation.R
import com.mobgen.presentation.load
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : DaggerFragment() {
    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE_URL = "imageUrl"
        private const val ARG_DATE = "date"
        private const val ARG_TRANSITION ="transitionName"
        fun newInstance(name: String, urlImage: String, description: String, date: String, transitionName: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_DESCRIPTION, description)
                    putString(ARG_IMAGE_URL, urlImage)
                    putString(ARG_DATE, date)
                    putString(ARG_TRANSITION, transitionName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        initView()
    }

    private fun initView() {
        sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeImageTransform())
            .addTransition(ChangeBounds())
            .apply {
                imageView.apply {
                    transitionName = arguments?.getString(ARG_TRANSITION, "")
                    load(arguments?.getString(ARG_IMAGE_URL) ?: "", true){
                        startPostponedEnterTransition()
                    }
                }
            }
        enterTransition = Fade().apply {
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            excludeTarget(R.id.action_bar_container, true)
        }
    }


}