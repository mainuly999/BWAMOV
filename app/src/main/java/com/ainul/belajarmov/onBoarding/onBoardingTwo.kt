package com.ainul.belajarmov.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ainul.belajarmov.R
import kotlinx.android.synthetic.main.activity_on_boarding_one.btn_login

class onBoardingTwo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_two)

        btn_login.setOnClickListener {
            startActivity(Intent(this@onBoardingTwo, onBoardingTree::class.java))
        }
    }
}
