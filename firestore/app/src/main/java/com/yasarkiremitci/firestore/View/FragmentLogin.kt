package com.yasarkiremitci.firestore.View


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yasarkiremitci.firestore.R
import com.yasarkiremitci.firestore.databinding.FragmentLoginBinding

class FragmentLogin : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment'ın layout dosyasını şişir
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.button2.setOnClickListener {
            signUpClicked(it)
        }
        binding.button.setOnClickListener {
            signInClicked(it)
        }

        return binding.root
    }

    private fun signInClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //success
                val verification = auth.currentUser?.isEmailVerified
                if (verification == true) {
                    findNavController().navigate(R.id.action_fragmentLogin_to_fragment_recycler2)
                } else {

                    Toast.makeText(
                        requireContext(),
                        "Öncelikle Mail Adresinizi Doğrulamalısınız",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }

    }
    private fun signUpClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please enter email and password",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //success
                auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {

                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}