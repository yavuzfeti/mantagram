package com.overthinkers.mantagram


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.paylas.*
import java.util.*

class paylas : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    var secilen_resim : Uri?=null
    var secilen_bitmap: Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.paylas)

        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
    }


    fun paylas(view: View)
    {
        val uuid= UUID.randomUUID()
        val resimisim= "${uuid}.jpg"
        val reference= storage.reference
        val resimreference= reference.child("Resimler").child(resimisim)

        if (secilen_resim!= null)
        {
            resimreference.putFile(secilen_resim!!).addOnSuccessListener { taskSnapShot->

                val yuklenenresimreference = FirebaseStorage.getInstance().reference.child("Resimler").child(resimisim)
                yuklenenresimreference.downloadUrl.addOnSuccessListener { uri->

                    val downloadurl=uri.toString()
                    val kullanici=auth.currentUser!!.email.toString()
                    val yorum= yorum.text.toString()
                    val tarih= com.google.firebase.Timestamp.now()

                    val gonderihash= hashMapOf<String,Any>()
                    gonderihash.put("Resim url",downloadurl)
                    gonderihash.put("Kullanıcı",kullanici)
                    gonderihash.put("Yorum",yorum)
                    gonderihash.put("Tarih",tarih)

                    database.collection("Gönderiler").add(gonderihash).addOnCompleteListener { task->
                        if (task.isSuccessful)
                        {
                            finish()
                        }
                    }.addOnFailureListener{ exception->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{ exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun gorselsec(view: View)
    {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else
        {
            val galeri =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeri,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode==1)
        {
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                val galeri =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeri,2)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode==2 && resultCode==Activity.RESULT_OK && data!=null)
        {
            secilen_resim=data.data
            if (secilen_resim!=null)
            {
                if (Build.VERSION.SDK_INT>=28)
                {
                    val source =  ImageDecoder.createSource(this.contentResolver,secilen_resim!!)
                    secilen_bitmap=ImageDecoder.decodeBitmap(source)
                    gorselsec.setImageBitmap(secilen_bitmap)
                }
                else
                {
                    secilen_bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilen_resim)
                    gorselsec.setImageBitmap(secilen_bitmap)
                }
            }
        }



        super.onActivityResult(requestCode, resultCode, data)
    }


}