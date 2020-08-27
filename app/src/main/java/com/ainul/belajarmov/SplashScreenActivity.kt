package com.ainul.belajarmov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ainul.belajarmov.onBoarding.onBoardingOne

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var handler = Handler()
        handler.postDelayed({
            var intent = Intent(this@SplashScreenActivity, onBoardingOne::class.java)
            startActivity(intent)
//            kemudian buat fungsi untuk menghacurkan actvity class ini
            finish()
        },5000)
    }
}
