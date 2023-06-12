package com.yasarkiremitci.firestore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.yasarkiremitci.firestore.View.Fragment_recyclerDirections
import com.yasarkiremitci.firestore.databinding.FragmentRecyclerBinding
import com.yasarkiremitci.firestore.model.Post

class NewAdapter (val post_list : ArrayList<Post>): RecyclerView.Adapter<NewAdapter.PostHolder>(){
    val db= Firebase.firestore
    val auth= FirebaseAuth.getInstance()

    class PostHolder(itemView: View): RecyclerView.ViewHolder(itemView) {




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)


    }

    override fun getItemCount(): Int {
        return post_list.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.user_mail).text = "  E Mail : "+ post_list[position].mail
        holder.itemView.findViewById<TextView>(R.id.rec_comment).text = "  Write a description : " +post_list[position].comment
        Picasso.get().load(post_list[position].image).into(holder.itemView.findViewById<ImageView>(R.id.rec_img))
        holder.itemView.setOnClickListener {
            val action=Fragment_recyclerDirections.actionFragmentRecyclerToCommentFragment(post_list[position].id)
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.findViewById<Button>(R.id.commentButton).setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Edit Text Alert Geldi!")
            builder.setTitle("Edit Text Alert")
            val text = EditText(holder.itemView.context)
            builder.setView(text)
            builder.setPositiveButton("Göster"){_,_->
                val comment = hashMapOf(
                    "username" to auth.currentUser!!.email,
                    "comment" to text.text.toString()
                )
                Toast.makeText(holder.itemView.context,text.text, Toast.LENGTH_LONG).show()

                db.collection("Post")
                    .document(post_list[position].id)
                    .collection("comment")
                    .add(comment)

            }

            builder.setCancelable(true)
            val alert = builder.create()
            alert.show()

        }




    }

}