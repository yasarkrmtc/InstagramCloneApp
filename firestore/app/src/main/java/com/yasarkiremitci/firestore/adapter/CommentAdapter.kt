package com.yasarkiremitci.firestore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yasarkiremitci.firestore.Comment
import com.yasarkiremitci.firestore.R
import com.yasarkiremitci.firestore.databinding.CommentRowBinding


class CommentAdapter(val post_list: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.DataVH>() {

    class DataVH(itemView: View): RecyclerView.ViewHolder(itemView){

        private lateinit var binding: CommentRowBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataVH {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.comment_row,parent,false)
        return DataVH(itemView)
    }

    override fun getItemCount(): Int {
        return post_list.size
    }

    override fun onBindViewHolder(holder: DataVH, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.user_mail)
        val textView2= holder.itemView.findViewById<TextView>(R.id.commentTextuser)
        textView.text=post_list[position].userName
        textView2.text=post_list[position].comment
        holder.itemView.setOnClickListener {

        }

    }

}