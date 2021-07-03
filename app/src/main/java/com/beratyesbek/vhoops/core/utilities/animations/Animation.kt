package com.beratyesbek.vhoops.core.utilities.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView


class Animation {
    companion object{
         fun revealAnim(view: View) {

            val cx = view.width / 2
            val cy = view.height / 2
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
            view.visibility = View.VISIBLE
            anim.start()
        }

         fun hideAnim(view: View) {
            val cx = view.width / 2
            val cy = view.height / 2
            val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })
            anim.start()
        }
        fun listItemAnimation(recyclerView: RecyclerView){
            val context = recyclerView.context
            val animationController = AnimationUtils.loadLayoutAnimation(context,com.beratyesbek.vhoops.R.anim.recyclerview_animation_bottom)
            recyclerView.layoutAnimation = animationController

        }
        fun listItemAnimationSecond(recyclerView: RecyclerView){
            val context = recyclerView.context
            val animationController = AnimationUtils.loadLayoutAnimation(context,com.beratyesbek.vhoops.R.anim.recycler_view_animation_fade_top)
            recyclerView.layoutAnimation = animationController
        }
    }


}