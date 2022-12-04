package com.example.chat.auth
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.chat.R
import com.example.chat.databinding.FragmentAuthBinding
import com.example.chat.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit


class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private lateinit var auth: FirebaseAuth
    private var verId = " "
    private var phone = binding.inAuth.etNumber.text

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentAuthBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            auth = FirebaseAuth.getInstance()
            binding.inAuth.btnSend.setOnClickListener {

                sendPhoneNumber()

            }

            binding.inAccept.btnAccept.setOnClickListener {
                val credential = PhoneAuthProvider.getCredential(verId,
                    binding.inAccept.etCode.text.toString())
               signInWithPhoneAuthCredential(credential)
                if (binding.inAccept.etUsername.text.isEmpty()){
                    binding.inAccept.etUsername.setText(getString(R.string.default_username))
                }
            }
        }

    private fun sendPhoneNumber() {
        val phone = binding.inAuth.etNumber
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone.text.toString())       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

             signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, p1)
            verId = verificationId
            binding.acceptContainer.isVisible = true
            binding.authContainer.isVisible = false
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.e("aic", "signInWithPhoneAuthCredential: success")
                    saveUserData()
                } else {
                    Log.e("ai", "signInWithPhoneAuthCredential: error" + task.exception?.message)
                }
            }
    }

    private fun saveUserData() {
        val uid = auth.currentUser?.uid
        if (uid != null){
            val ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
            val userData = hashMapOf<String, String>()
            userData["uid"] = uid
            userData["phone"] = phone.toString()
            userData["userName"] = binding.inAccept.etUsername.text.toString()

            ref.set(userData).addOnCompleteListener {
                if (it.isSuccessful){
                    findNavController().navigateUp()
                }else it.exception?.message?.let { it1 -> showToast(it1) }

            }
        }
    }
}


