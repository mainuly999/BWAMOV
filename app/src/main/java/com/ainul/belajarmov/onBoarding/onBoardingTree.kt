package com.ainul.belajarmov.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ainul.belajarmov.R
import com.ainul.belajarmov.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_on_boarding_one.btn_login

class onBoardingTree : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_tree)

        btn_login.setOnClickListener {
//            untuk menghapus semua tab yang muncul sebelumnya
            finishAffinity()
            var intent = Intent(this@onBoardingTree, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
