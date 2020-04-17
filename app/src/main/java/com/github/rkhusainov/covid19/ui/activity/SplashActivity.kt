package com.github.rkhusainov.covid19.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.github.rkhusainov.covid19.R

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val MILLIS = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            finish()
        }, MILLIS.toLong())
    }
}
