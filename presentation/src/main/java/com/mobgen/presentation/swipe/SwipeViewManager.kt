package com.mobgen.presentation.swipe

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_swipe.view.*

class SwipeViewManager(
    private val viewFirst: View,
    private val viewSecond: View,
    private var list: List<SwipeViewModel.UserBindView>,
    private val screenCenter: Float,
    private val listener: Listener
) {
    private var x = 0f
    private var y = 0f
    private var currentX = 0f
    private var currentY = 0f
    private var currentPosition = 0

    companion object {
        private const val ROTATION_RADIO = (Math.PI / 64).toFloat()
        private const val AUTOMATIC_RADIO = 45f
        private const val FINAL_HORIZONTAL_POINT = 3f
        private const val THRESHOLD_SWIPE_REWIND = 2
        private const val TOTAL_ANIMATION_DURATION = 1000L
    }

    init {
        val listener = View.OnTouchListener { view, event ->
            currentX = event.rawX
            currentY = event.rawY

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                    y = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    //TODO this should be more accurate
                    view.x = currentX - x
                    view.y = currentY - y

                    view.rotation = (currentX - screenCenter) * ROTATION_RADIO

                    when {
                        currentX >= screenCenter + (screenCenter / THRESHOLD_SWIPE_REWIND) -> {
                            hintImageVisibility(view, like = true, noLike = false)
                        }
                        currentX <= screenCenter - (screenCenter / THRESHOLD_SWIPE_REWIND) -> {
                            hintImageVisibility(view, like = false, noLike = true)
                        }
                        else -> {
                            hintImageVisibility(view, like = false, noLike = false)
                        }
                    }

                }
                MotionEvent.ACTION_UP -> {

                    when {
                        currentX >= screenCenter + (screenCenter / THRESHOLD_SWIPE_REWIND) -> {
                            swipe()
                        }
                        currentX <= screenCenter - (screenCenter / THRESHOLD_SWIPE_REWIND) -> {
                            rewind()
                        }
                        else -> {
                            animateTo(view, 0f, 0f, 0f).start()
                            listener.onCancel()
                        }
                    }


                }
            }
            view.translationZ > 0
        }
        viewFirst.setOnTouchListener(listener)
        viewSecond.setOnTouchListener(listener)

        updateTopCard()
        updateBackCard()
    }

    fun swipe() {
        if (currentPosition < list.size) {
            listener.onSwipe(currentPosition)
            automaticSwipeAnimation()
        }
    }

    fun rewind() {
        if (currentPosition < list.size) {
            listener.onRewind(currentPosition)
            automaticRewindAnimation()
        }
    }

    private fun bindCard(view: View, position: Int) {
        if (position < list.size) {
            val imageView = if (view == viewFirst) view.imageViewTop else view.imageViewBottom
            hintImageVisibility(view, like = false, noLike = false)
            Glide.with(view.context).load(list[position].photo.first()).into(imageView)
        } else {
            view.visibility = View.GONE
        }
    }

    private fun hintImageVisibility(parent: View, like: Boolean, noLike: Boolean) {
        val imageViewLike = if (parent == viewFirst) parent.likeCardTop else parent.likeCardBottom
        val imageViewNoLike = if (parent == viewFirst) parent.noLikeCardTop else parent.noLikeCardBottom
        imageViewLike.visibility = if (like) View.VISIBLE else View.INVISIBLE
        imageViewNoLike.visibility = if (noLike) View.VISIBLE else View.INVISIBLE
    }

    private fun updateTopCard() {
        val view = getTopView()
        bindCard(view, currentPosition)
    }

    private fun updateBackCard() {
        val view = if (getTopView() == viewFirst) viewSecond else viewFirst
        bindCard(view, currentPosition + 1)
    }

    private fun animateTo(view: View, translateX: Float, translateY: Float, rotation: Float): AnimatorSet {
        val rotate = ObjectAnimator.ofFloat(view, View.ROTATION, rotation)
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, translateX)
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, translateY)
        val translation = ObjectAnimator.ofPropertyValuesHolder(view, translationX, translationY)
        return AnimatorSet().apply {
            play(translation).with(rotate)
            duration = TOTAL_ANIMATION_DURATION
        }
    }

    private fun resetView(viewToReset: View, otherView: View) {
        val topValue = viewToReset.translationZ
        viewToReset.translationZ = if (viewToReset.translationZ > 0) 0f else topValue
        otherView.translationZ = if (otherView.translationZ > 0) 0f else topValue
        currentPosition++

        viewToReset.x = otherView.x
        viewToReset.y = otherView.y
        viewToReset.rotation = otherView.rotation

        updateBackCard()

    }

    private val animationListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            val top = if (viewFirst.translationZ > viewSecond.translationZ) viewFirst else viewSecond
            resetView(top, if (top == viewFirst) viewSecond else viewFirst)
        }
    }

    private fun automaticSwipeAnimation() {
        val top = getTopView()
        animateTo(top, screenCenter * FINAL_HORIZONTAL_POINT, top.y, AUTOMATIC_RADIO).apply {
            addListener(animationListener)
        }.start()
    }

    private fun automaticRewindAnimation() {
        val top = getTopView()
        animateTo(top, -screenCenter * FINAL_HORIZONTAL_POINT, top.y, -AUTOMATIC_RADIO).apply {
            addListener(animationListener)
        }.start()
    }

    private fun getTopView(): View = if (viewFirst.translationZ > viewSecond.translationZ) viewFirst else viewSecond

    open class Listener {
        open fun onSwipe(position: Int) {}
        open fun onRewind(position: Int) {}
        open fun onCancel() {}
    }
}