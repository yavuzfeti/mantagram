package com.overthinkers.mantagram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerow.view.*

class ana_sayfa_recycle (val gonderilist: ArrayList<Gönderi>):RecyclerView.Adapter<ana_sayfa_recycle.Gonderiholder>() {

    class Gonderiholder(itemView: View):RecyclerView.ViewHolder(itemView)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Gonderiholder {
        val inflater= LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recyclerow,parent,false)
        return Gonderiholder(view)
    }

    override fun onBindViewHolder(holder: Gonderiholder, position: Int) {
        holder.itemView.recyclewiew_kullanici.text=gonderilist[position].kullanici
        holder.itemView.recyclewiew_yorum.text=gonderilist[position].yorum
        Picasso.get().load(gonderilist[position].resimurl).into(holder.itemView.recyclewiew_resim)
    }

    override fun getItemCount(): Int {
        return gonderilist.size
    }
}