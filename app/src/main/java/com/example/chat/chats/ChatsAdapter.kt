package com.example.chat.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.chat.databinding.ItemChatBinding

class ChatsAdapter(private val chats: ArrayList<Chat> = arrayListOf()):
    RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(chats[position])
        

        fun addChats(newChats: List<Chat>) {
            this.chats.clear()
            this.chats.addAll(newChats)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = chats.size


    inner class ChatsViewHolder(private var binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(chats: Chat) {
            binding.tvUser.text = chats.user
            binding.tvPhone.text = chats.phone
        }
    }
}