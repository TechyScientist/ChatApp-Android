package com.johnnyconsole.android.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnnyconsole.android.chatapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val messages = ArrayList<String>()

    private class MessageHolder(val view: View): RecyclerView.ViewHolder(view)
    private inner class MessageListAdapter(val inflater: LayoutInflater): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return position % 2
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MessageHolder(
                inflater.inflate(
                if(viewType == 0) R.layout.layout_message_sent
                          else R.layout.layout_message_received,
                parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = (holder as MessageHolder).view
            view.findViewById<TextView>(R.id.tvMessageText).text = messages[position]
        }

        override fun getItemCount(): Int {
            return messages.count()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ChatActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            rvMessageHistory.layoutManager = LinearLayoutManager(this@ChatActivity)
            rvMessageHistory.adapter = MessageListAdapter(layoutInflater)

            btSendMessage.setOnClickListener { _ ->
                if(!etMessage.text.isNullOrBlank()) {
                    messages.add(etMessage.text.toString())
                    etMessage.text.clear()
                    rvMessageHistory.adapter!!.notifyItemInserted(messages.size)
                }
            }
        }
    }
}