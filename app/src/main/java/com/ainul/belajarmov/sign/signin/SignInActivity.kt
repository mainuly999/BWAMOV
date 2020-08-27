package com.ainul.belajarmov.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ainul.belajarmov.home.HomeActivity
import com.ainul.belajarmov.R
import com.ainul.belajarmov.sign.signup.SignUpActivity
import com.ainul.belajarmov.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {
    lateinit var iUsername: String
    lateinit var iPassword: String

    lateinit var mDatabase: DatabaseReference
    lateinit var preference: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

//        path di getReference dibawah ini diambil dari database firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preference = Preferences(this)

//        membuat onboarding hanya muncul satu kali/pertamakali
        preference.setValues("onboarding", "1")
        //saat sudah masuk sign in activity maka akan setvalue "1" pada prefences dengan key onboarding

//        mengecek apakah key "status" pada prefences sudah ada value "1" [cek dibawah ada setonclicklistener.pushlogin]
        if(preference.getValues("status").equals("1")){
            finishAffinity()

            var goHome = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_login.setOnClickListener {
            // Write a message to the database
            iUsername = input_username.text.toString()
            iPassword = input_password.text.toString()

            if(iUsername.equals("")){
//                .error berfungsi seperti alert pada js
                input_username.error = "Silahkan isi username anda"
                input_username.requestFocus()
            }else if(iPassword.equals("")){
                input_password.error = "Silahkan isi password anda"
                input_password.requestFocus()
            }else{
                pushLogin(iUsername, iPassword)
            }
        }

        btn_daftar.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignInActivity, databaseError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user = dataSnapshot.getValue(User::class.java)
                if(user == null){
//                    toast sama kayak alert di js juga
                    Toast.makeText(this@SignInActivity, "User Tidak Ditemukan", Toast.LENGTH_LONG).show()
                }else{
                    if(user.password.equals(iPassword)){
                        finishAffinity()

                        preference.setValues("nama", user.nama.toString())
                        preference.setValues("user", user.username.toString())
                        preference.setValues("url", user.url.toString())
                        preference.setValues("email", user.email.toString())
                        preference.setValues("saldo", user.saldo.toString())
                        preference.setValues("status", "1")
                        //saat sudah berhasil dengan setonclicklistener login maka akan setvalue "1" pada prefences dengan key status

                        var intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@SignInActivity, "Password Anda Salah", Toast.LENGTH_LONG).show()
                    }

                }
            }
        })
    }
}
