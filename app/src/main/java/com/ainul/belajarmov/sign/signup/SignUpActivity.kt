package com.ainul.belajarmov.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ainul.belajarmov.R
import com.ainul.belajarmov.sign.signin.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    lateinit var sUsername: String
    lateinit var sPassword: String
    lateinit var sNama: String
    lateinit var sEmail: String

    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabaseReference = mFirebaseInstance.getReference("User")

        btn_login.setOnClickListener {
            sUsername = input_username.text.toString()
            sPassword = input_password.text.toString()
            sNama = input_nama.text.toString()
            sEmail = input_email.text.toString()

            if(sPassword.equals("")){
                input_password.error="Silahkan Isi Pass Anda"
                input_password.requestFocus()
            }else if(sUsername.equals("")){
                input_username.error="Silahkan Isi Username Anda"
                input_username.requestFocus()
            }else if(sNama.equals("")){
                input_nama.error="Silahkan Isi Nama Anda"
                input_nama.requestFocus()
            }else if(sEmail.equals("")){
                input_email.error="Silahkan Isi Email Anda"
                input_email.requestFocus()
            }else{
                saveUsername(sUsername,sPassword,sNama,sEmail)
            }
        }
    }

    private fun saveUsername(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        var data = User()
        data.email = sEmail
        data.username = sUsername
        data.nama = sNama
        data.password = sPassword

//        check apakah username telah digunakan
        if(sUsername != null){
            checkingUsername(sUsername, data)
        }
    }

    private fun checkingUsername(sUsername: String, data: User) {
//   mDatabaseReference.child(x) artinya akan mencari x di variable mDatabasereference yang mana kita sudah insiasi
//   jadi fungsinya nanti akan membawa data dibawah "user" unutk masuk di snapshot
        mDatabaseReference.child(sUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+databaseError.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                class user seperti penyambung dan penyimpan sementara data yang diambil dari database firebase
                var user = dataSnapshot.getValue(User::class.java)
//                kemudian jika username tidak ada pada user di firebase/database maka kita bisa buat akunnya
                if(user == null){
//                    pada firebase di user(karena mDatabaseReference lihat di atas) akan dibuat di childnya dengan key dari sUsername
//                    dan value berupada data yang masuk dari "data" di paramater checkingUsername(sUsername, data)
                    mDatabaseReference.child(sUsername).setValue(data)

                    var kePhotoScreen = Intent(this@SignUpActivity, SignUpPhotoscreenActivity::class.java).putExtra("namacuu", data.nama)
                    startActivity(kePhotoScreen)
                }else {
                    Toast.makeText(this@SignUpActivity, "Username Sudah Digunakan", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
