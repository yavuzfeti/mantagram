package com.overthinkers.mantagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.ana_sayfa.*

class ana_sayfa : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    private lateinit var readapter:ana_sayfa_recycle

    var gonderilist=ArrayList<Gönderi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ana_sayfa)

        supportActionBar?.setTitle("MANTAGRAM")

        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        verial()

        var layoutManager= LinearLayoutManager(this)
        recyclewiew.layoutManager=layoutManager
        readapter= ana_sayfa_recycle(gonderilist)
        recyclewiew.adapter=readapter
    }




    fun verial()
    {
        database.collection("Gönderiler").orderBy("Tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception!=null)
            {
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else
            {
                if (snapshot != null)
                {
                    if (!snapshot.isEmpty)
                    {
                        val documents= snapshot.documents
                        gonderilist.clear()
                        for (document in documents)
                        {
                            val kullanici=document.get("Kullanıcı") as String
                            val yorum=document.get("Yorum") as String
                            val resimurl=document.get("Resim url") as String

                            val indirilengonderi= Gönderi(kullanici,yorum,resimurl)
                            gonderilist.add(indirilengonderi)
                        }
                        readapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuu =menuInflater
        menuu.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.paylass)
        {
            val intent=Intent(this,paylas::class.java)
            startActivity(intent)
        }

        else if (item.itemId==R.id.cikis)
        {
            auth.signOut()
            val intent=Intent(this,giris::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}