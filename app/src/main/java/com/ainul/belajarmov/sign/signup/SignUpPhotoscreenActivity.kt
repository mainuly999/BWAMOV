package com.ainul.belajarmov.sign.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.ainul.belajarmov.home.HomeActivity
import com.ainul.belajarmov.R
import com.ainul.belajarmov.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity(), PermissionListener {
    var REQUEST_IMAGE_CAPTURE = 1
//    status pencarian foto jadi jka sudah ada foto dia akan bernilai true
    var statusAdd: Boolean = false
//    membuat var untuk mencari file di storage android nantinya
    lateinit var filePath: Uri
//pastikan storage firebase udah diizinkan di rules pada storage read and write jadi true
    lateinit var storage: FirebaseStorage
    lateinit var storageReferensi: StorageReference
//    masuk preferences yang sudah dibuat di utils
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReferensi = storage.getReference()

        tv_hello.text = "Selamat Datang\n" + preferences.getValues("namacuu").toString()

        btn_ic_add.setOnClickListener {
            if(statusAdd){
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                btn_ic_add.setImageResource(R.drawable.ic_delete)
                iv_profile.setImageResource(R.drawable.user_pic)
            }else{
//                kalau statusAdd false atau bleum ada fotonya
//                Dexter.withActivity(this)
//                    .withPermission(Manifest.permission.CAMERA)
//                    .withListener(this)
//                    .check()
                ImagePicker.with(this)
                    .cameraOnly() //user hanya bisa caputer image lewat camera
                    .start()
            }
        }
        btn_home.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@SignUpPhotoscreenActivity,
                HomeActivity::class.java))
        }
        btn_save.setOnClickListener {
            if(filePath != null){
//                upload
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                var ref = storageReferensi.child("images"+ UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
//                        ketika upload sudah berhasil
                        progressDialog.dismiss()
                        Toast.makeText(this,"Uploaded",Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
//                            ketika sudah sukses uri disimpan pada preference
                            preferences.setValues("url",it.toString())
                        }

                        finishAffinity()
                        startActivity(Intent(this@SignUpPhotoscreenActivity,
                            HomeActivity::class.java))
                    }
                    .addOnFailureListener {
//                        Kalau gagal upload
                        progressDialog.dismiss()
                        Toast.makeText(this,"Failed to Upload Photo",Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
//                        untuk feedback ke user agar bisa melihat berapa jauh progress yang sedang terjadi
                        taskSnapshot -> var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload "+ progress.toInt()+"%")
                    }
            }else{
//                memebri tahu user unutk memencet tombol unutk opload gambar
            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//        kalau disetujuin
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
            takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also{
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this,"Anda Tidak bisa menambah photo profile",Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this,"Tergesah? Klik tombol upload nanti aja",Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            statusAdd = true
//            image tidak akan null
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save.visibility = View.VISIBLE
            btn_ic_add.setImageResource(R.drawable.ic_delete)
        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this,ImagePicker.getError(data),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Task Cancelled",Toast.LENGTH_SHORT).show()
        }
    }
}
