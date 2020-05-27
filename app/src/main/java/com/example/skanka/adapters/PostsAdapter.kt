package com.example.skanka.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skanka.R
import com.example.skanka.model.Post
import kotlinx.android.synthetic.main.new_post.view.*

class PostsAdapter (val context: Context, val posts: List<Post>, var itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.new_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position], itemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: Post, clickListener: OnItemClickListener) {
            itemView.tvusername.text = post.user?.userName
            itemView.etHeadLine.text = post.headline
            Glide.with(context).load(post.imageUrl).into(itemView.itemImage)
            itemView.relativeTime.text = DateUtils.getRelativeTimeSpanString(post.creationTimeMs)
            itemView.setOnClickListener {
                clickListener.onItemClick(post)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(post:Post)
    }

}