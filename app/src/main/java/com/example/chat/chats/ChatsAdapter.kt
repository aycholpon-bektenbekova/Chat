package com.example.chat.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.model.Messages
import com.google.firebase.auth.FirebaseAuth

class ChatsAdapter():
RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    private val messages: ArrayList<Messages> = arrayListOf()
    fun addMessages(newMessages: ArrayList<Messages>) {
        messages.clear()
        messages.addAll(newMessages)
        messages.reverse()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = messages[position]
        if (item.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            return CURRENT_USER
        } else return OTHER_USER

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        if (viewType == CURRENT_USER) {
            return ChatsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            )
        } else return ChatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(messages[position])

    }

    override fun getItemCount(): Int = messages.size

    companion object {
        private const val CURRENT_USER = 1
        private const val OTHER_USER = 0
    }

    inner class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView? = null
        fun bind(messages: Messages) {
            tvMessage?.text = messages.message
        }

        init {
            tvMessage?.text = itemView.findViewById(R.id.message_right)
        }
    }
}

