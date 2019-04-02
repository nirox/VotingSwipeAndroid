package com.mobgen.presentation.swipe.swipeView

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.MotionEvent
import android.view.View
import com.mobgen.presentation.load
import com.mobgen.presentation.swipe.SwipeViewModel
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
    private var animated = false
    private var animation: AnimatorSet? = null
    private var backView: View = viewSecond

    companion object {
        private const val ROTATION_RADIO = (Math.PI / 64).toFloat()
        private const val AUTOMATIC_RADIO = 45f
        private const val FINAL_HORIZONTAL_POINT = 3f
        private const val THRESHOLD_SWIPE_REWIND = 2
        private const val TOTAL_ANIMATION_DURATION = 1000L
        private const val TRANSITION_NAME = "transition"
    }

    init {
        val listener = View.OnTouchListener { view, event ->
            if (view.translationZ > 0) {
                backView = if (viewFirst == getTopView()) viewSecond else viewFirst
                currentX = event.rawX
                currentY = event.rawY
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.x
                        y = event.y
                        animation?.cancel()
                        animated = false
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
                            view.x == backView.x && view.y == backView.y && !animated -> listener.onCardClick(
                                currentPosition
                            )
                            currentX >= screenCenter + (screenCenter / THRESHOLD_SWIPE_REWIND) -> {
                                swipe()
                            }
                            currentX <= screenCenter - (screenCenter / THRESHOLD_SWIPE_REWIND) -> {
                                rewind()
                            }
                            else -> {
                                animateTo(view, 0f, 0f, 0f)
                                animation?.start()
                                listener.onCancel()
                            }
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
        if (currentPosition < list.size && !animated) {
            listener.onSwipe(currentPosition)
            automaticSwipeAnimation()
        }
    }

    fun rewind() {
        if (currentPosition < list.size && !animated) {
            listener.onRewind(currentPosition)
            automaticRewindAnimation()
        }
    }

    fun getTopView(): View = if (viewFirst.translationZ > viewSecond.translationZ) viewFirst else viewSecond

    private fun bindCard(view: View, position: Int) {
        if (position < list.size) {
            val imageView = if (view == viewFirst) view.imageViewTop else view.imageViewBottom
            imageView.transitionName = "$TRANSITION_NAME$position"
            hintImageVisibility(view, like = false, noLike = false)
            view.visibility = View.VISIBLE
            imageView.load(list[position].photo.first())
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

    private fun animateTo(view: View, translateX: Float, translateY: Float, rotation: Float) {
        val rotate = ObjectAnimator.ofFloat(view, View.ROTATION, rotation)
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, translateX)
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, translateY)
        val translation = ObjectAnimator.ofPropertyValuesHolder(view, translationX, translationY)
        animation = AnimatorSet().apply {
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
            animated = false
        }

        override fun onAnimationStart(animation: Animator?) {
            animated = true
        }
    }

    private fun automaticSwipeAnimation() {
        val top = getTopView()
        animateTo(
            top, screenCenter * FINAL_HORIZONTAL_POINT, top.y,
            AUTOMATIC_RADIO
        )
        animation?.apply {
            addListener(animationListener)
        }?.start()
    }

    private fun automaticRewindAnimation() {
        val top = getTopView()
        animateTo(top, -screenCenter * FINAL_HORIZONTAL_POINT, top.y, -AUTOMATIC_RADIO)
        animation?.apply {
            addListener(animationListener)
        }?.start()
    }

    open class Listener {
        open fun onSwipe(position: Int) {}
        open fun onRewind(position: Int) {}
        open fun onCancel() {}
        open fun onCardClick(position: Int) {}
    }
}