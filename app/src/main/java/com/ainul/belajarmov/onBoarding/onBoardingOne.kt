package com.ainul.belajarmov.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ainul.belajarmov.R
import com.ainul.belajarmov.sign.signin.SignInActivity
import com.ainul.belajarmov.utils.Preferences
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class onBoardingOne : AppCompatActivity() {

    lateinit var preference: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)
        preference = Preferences(this)
//        mengecek pada prefences apakah key onboarding sudah terisi dengan value 1 [lihat di singinactivity.kt]
        if(preference.getValues("onboarding").equals("1")){
            finishAffinity()

            var goSignIn = Intent(this@onBoardingOne, SignInActivity::class.java)
            startActivity(goSignIn)
        }


        btn_login.setOnClickListener {
            var intent = Intent(this@onBoardingOne, onBoardingTwo::class.java)
            startActivity(intent)
        }
        btn_daftar.setOnClickListener {
            finishAffinity()
            var intent = Intent(this@onBoardingOne,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
