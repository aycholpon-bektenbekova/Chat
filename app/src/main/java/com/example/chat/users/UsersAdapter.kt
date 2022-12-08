package com.example.chat.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ItemUsersBinding
import com.example.chat.model.User

class UsersAdapter(private val onClick:(uid: String) -> Unit):
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val users: ArrayList<User> = arrayListOf()
    fun addUsers(newUsers: ArrayList<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ItemUsersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])

    }

    override fun getItemCount(): Int = users.size


    inner class UsersViewHolder(private var binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(users: User) {
            itemView.setOnClickListener {
                onClick(users.uid.toString())
            }
            binding.tvUser.text = users.userName
            binding.tvPhone.text = users.phone
        }
    }
}