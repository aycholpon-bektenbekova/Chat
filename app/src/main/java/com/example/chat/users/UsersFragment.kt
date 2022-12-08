package com.example.chat.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.chat.R
import com.example.chat.databinding.FragmentUsersBinding
import com.example.chat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UsersAdapter
    private val data = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ref = FirebaseFirestore.getInstance().collection("Users")
        adapter = UsersAdapter{ id ->
            findNavController().navigate(R.id.chatsFragment, bundleOf("uid" to id))
        }
        binding.rvUsers.adapter = adapter

        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                data.clear()
               for (item in it.result.documents) {
                   val user = item.toObject(User::class.java)
                    if (user != null && user.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                      data.add(user)
                   }
                }
                adapter.addUsers(data)
            }
        }

    }

}