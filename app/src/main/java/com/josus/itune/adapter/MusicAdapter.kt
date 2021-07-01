package com.josus.itune.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.josus.itune.R
import com.josus.itune.model.Music
import kotlinx.android.synthetic.main.item.view.*

class MusicAdapter:RecyclerView.Adapter<MusicAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


    private val differCallback=object:DiffUtil.ItemCallback<Music>(){
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.artistId == newItem.artistId
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
          return oldItem == newItem
        }
    }

    val differ=AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
       return ArticleViewHolder(LayoutInflater.from(parent.context)
           .inflate(R.layout.item,parent,false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.artworkUrl100).into(imgArtist)

            txtArtistName.text=article.artistName

            btnAdd.setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
                Snackbar.make(this,"Item Saved",Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    private var onItemClickListener:((Music) -> Unit)?=null

    fun setOnItemClickListener(listener:(Music) -> Unit){
        onItemClickListener=listener
    }
}