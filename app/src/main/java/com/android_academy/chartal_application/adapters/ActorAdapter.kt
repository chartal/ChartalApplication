package com.android_academy.chartal_application.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Actor


class ActorAdapter() :
    RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    private val items = mutableListOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.actor_item, parent, false)
        return ActorViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItems(newItems: List<Actor>) {
        val diffCallback = ActorDiffUtilCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class ActorViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        private val poster = itemView.findViewById<ImageView>(R.id.iv_actor)
        private val actorFullName = itemView.findViewById<TextView>(R.id.tv_full_actor_name)

        fun bind(actor: Actor) {
            poster.load(actor.picture) {
                placeholder(R.drawable.chartal_placeholder)
            }
            actorFullName.text = actor.name
        }
    }
}