package com.yasarkiremitci.firestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.yasarkiremitci.firestore.adapter.CommentAdapter
import com.yasarkiremitci.firestore.databinding.FragmentCommentBinding

class CommentFragment : Fragment() {
    private lateinit var binding: FragmentCommentBinding
    val db= Firebase.firestore
    val comment=ArrayList<Comment>()
    private lateinit var recyclerViewAdapter:CommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommentBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CommentFragmentArgs by navArgs()
        val data = args.id

        db.collection("Post").document(data).collection("comment").addSnapshotListener { value, error ->
            val documents = value!!.documents

            documents.forEach {
                comment.add(Comment("${it.getField<String>("comment")}","${it.getField<String>("username")}"))
                recyclerViewAdapter.notifyDataSetChanged()
            }



        }
        binding.rVComment.layoutManager= LinearLayoutManager(requireContext())
        binding.rVComment.setHasFixedSize(true)
        recyclerViewAdapter= CommentAdapter(comment)
        binding.rVComment.adapter=recyclerViewAdapter
    }

}