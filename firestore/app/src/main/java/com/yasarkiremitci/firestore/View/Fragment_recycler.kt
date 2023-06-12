package com.yasarkiremitci.firestore.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yasarkiremitci.firestore.model.Post
import com.yasarkiremitci.firestore.databinding.FragmentRecyclerBinding
import com.yasarkiremitci.firestore.NewAdapter
import com.yasarkiremitci.firestore.R


class Fragment_recycler : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var Firebaselist = ArrayList<Post>()
    private val firestore = Firebase.firestore
    private lateinit var recyclerViewAdapter: NewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment'ın layout dosyasını şişir
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)


        //println(post_list)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        getInformation()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = NewAdapter(Firebaselist)
        binding.recyclerView.adapter = recyclerViewAdapter

        binding.materialToolbar.inflateMenu(R.menu.menu_file)
        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.share_photo) {
                findNavController().navigate(R.id.action_fragment_recycler_to_fragmentLoadingImage)
                println("share")
            }else if (it.itemId==R.id.log_out){
                auth.signOut()
                findNavController().navigate(R.id.action_fragment_recycler_to_fragmentLogin)
            }
            true
        }
        return binding.root
    }

    fun getInformation() {
        firestore.collection("Post").orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { snapshot ->
                for (documents in snapshot.documents){
                    println(documents.get("email"))
                }
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            val documents = snapshot
                            Firebaselist.clear()
                            for (document in documents) {
                                val email = document.get("email") as String
                                val comment = document.get("comment") as String
                                val image = document.get("imageUrl") as String
                                val downoaldPost = Post(email, comment, image,document.id)
                                Firebaselist.add(downoaldPost)
                            }
                            recyclerViewAdapter.notifyDataSetChanged()
                        }


                    }


                }


            }



    }


