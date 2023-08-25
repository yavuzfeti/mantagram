package com.overthinkers.mantagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.giris.*

class giris : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.giris)

        auth= FirebaseAuth.getInstance()

        val kullanicigiris=auth.currentUser
        if (kullanicigiris!=null)
        {
            val intent=Intent(this,ana_sayfa::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun girisislemi(view: View)
    {
        val email=emailtext.text.toString()
        val sifre=sifretext.text.toString()
        auth.signInWithEmailAndPassword(email,sifre).addOnCompleteListener { task->

            if (task.isSuccessful)
            {
                val kullanici= auth.currentUser?.email.toString()
                Toast.makeText(applicationContext,"Hoşgeldin ${kullanici}",Toast.LENGTH_LONG).show()

                val intent=Intent(this,ana_sayfa::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener{ exception->

            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun kayitislemi(view: View)
    {
        val email=emailtext.text.toString()
        val sifre=sifretext.text.toString()

        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener { task ->

            if (task.isSuccessful)
            {
                val intent=Intent(this,ana_sayfa::class.java)
                startActivity(intent)
                finish()
            }

        }.addOnFailureListener{ exception->

            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}