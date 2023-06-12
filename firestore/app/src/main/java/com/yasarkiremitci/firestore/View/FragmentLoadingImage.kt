package com.yasarkiremitci.firestore.View

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yasarkiremitci.firestore.R
import com.yasarkiremitci.firestore.databinding.FragmentLoadingImageBinding
import java.util.UUID

class FragmentLoadingImage : Fragment() {

    var picked_image: Uri? = null
    var picked_bitmap: Bitmap? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: FragmentLoadingImageBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingImageBinding.inflate(inflater, container, false)
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.shareButton.setOnClickListener {
            val uuid = UUID.randomUUID()
            val name_ımage = "${uuid}.jpg"
            val reference = storage.reference
            val imageReference = reference.child("images").child(name_ımage)
            if (picked_image != null){
                imageReference.putFile(picked_image!!).addOnSuccessListener {
                    val uploadImageReference = FirebaseStorage.getInstance().reference.child("images").child(name_ımage)
                    uploadImageReference.downloadUrl.addOnSuccessListener {uri ->
                        val downloadUrl = uri.toString()
                        val currentuser_email = auth.currentUser!!.email.toString()
                        val user_comment = binding.commentText.text.toString()
                        val date = com.google.firebase.Timestamp.now()
                        val postHashMap = hashMapOf<String,Any>()
                        postHashMap.put("imageUrl",downloadUrl)
                        postHashMap.put("email",currentuser_email)
                        postHashMap.put("comment",user_comment)
                        postHashMap.put("date",date)
                        database.collection("Post").add(postHashMap).addOnCompleteListener{task ->
                            if (task.isSuccessful){
                                findNavController().navigate(R.id.action_fragmentLoadingImage_to_fragment_recycler)

                            }

                        }.addOnFailureListener {exception ->
                            Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()

                        }


                    }

                }.addOnFailureListener{
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()


                }

            }
        }

        binding.imageView.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES),1)
                Log.e("dsfs","ggffg")
            }else{
                Log.e("dsfs","vvvv")
                val photo_file_intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photo_file_intent,2)
            }

        }



        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode ==1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val photo_file_intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photo_file_intent,2)


            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }}

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && data !== null){
            picked_image = data.data
            Log.e("gdfbfgnfgn",data.data.toString())
            if (picked_image != null){
                if(Build.VERSION.SDK_INT >27){
                    val source = ImageDecoder.createSource(requireActivity().contentResolver,picked_image!!)
                    picked_bitmap = ImageDecoder.decodeBitmap(source)
                    Log.e("picked",picked_bitmap.toString())
                    binding.imageView.setImageURI(picked_image)

                }else{
                    picked_bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,picked_image)

                    Log.e("picked",picked_bitmap.toString())
                    binding.imageView.setImageURI(picked_image )
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
