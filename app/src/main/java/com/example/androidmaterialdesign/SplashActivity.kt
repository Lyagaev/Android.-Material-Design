package com.example.androidmaterialdesign

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.LinearInterpolator
import com.example.androidmaterialdesign.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    var handler = Handler()
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView1.animate().rotationBy(750f)
                .setInterpolator(LinearInterpolator()).setDuration(3000)
                .setListener(object : Animator.AnimatorListener {

                    override fun onAnimationEnd(animation: Animator?) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })

        binding.imageView2.animate().rotationBy(200f)
                .setInterpolator(LinearInterpolator()).setDuration(3000)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {}
                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationRepeat(p0: Animator?) {}
                })
    }
}