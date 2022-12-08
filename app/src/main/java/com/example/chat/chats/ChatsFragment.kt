package com.example.chat.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chat.databinding.FragmentChatsBinding
import com.example.chat.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ChatsFragment : Fragment() {

    private lateinit var ref: CollectionReference
    private lateinit var binding: FragmentChatsBinding
    private var receiverId = ""
    private var adapter: ChatsAdapter = ChatsAdapter()
    private var messages = arrayListOf<Messages>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref =
            FirebaseFirestore.getInstance().collection("Messages")
        receiverId = arguments?.getString("uid").toString()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        binding.btnFabSend.setOnClickListener {
            sendMessages(uid.toString())
        }
        binding.rvChats.adapter = adapter
        ref.addSnapshotListener { value, error ->
            if (value != null) {
                messages.clear()
                for (item in value.documents) {
                    if (item.data?.get("receiverId")
                            .toString()==receiverId && item.data?.get("senderId")
                            .toString()==uid){

                        val message = Messages(
                            item.data?.get("senderId").toString(),
                            item.data?.get("receiverId").toString(),
                            item.data?.get("message").toString(),
                            item.data?.get("time") as Long
                        )
                        messages.add(message)
                    }
                    adapter.addMessages(messages)
                }
            }
        }
    }

    private fun sendMessages(uid: String) {
        val data = Messages(
            senderId = uid,
            receiverId = receiverId,
            binding.etMessage.text.toString()
        )
        ref.document().set(data).addOnCompleteListener {
            if (it.isSuccessful) {

                Log.e("aic", "onViewCreated: success")
            } else Log.e("aic", "onViewCreated: error")
        }
        clearMessages()
    }

    private fun clearMessages() {
        binding.etMessage.setText("")
    }
}