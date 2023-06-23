package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.Posts

class SelfProfAdapter(private val postsList: MutableList<Posts>) :
    RecyclerView.Adapter<SelfProfAdapter.PostViewHolder>() {

    private var onItemClickListener: ((position: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selfprof, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList[position]
        holder.itemView.findViewById<TextView>(R.id.title).text = post.post_title
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
        holder.itemView.findViewById<TextView>(R.id.content).text = post.post_content
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClickListener = listener
    }
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

}