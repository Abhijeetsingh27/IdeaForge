package com.example.ideaforge

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for the RecyclerView
class IdeaAdapter(private val ideaList: List<IdeaData>) :
    RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder>() {

    // ViewHolder for each item
    class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.ideaTitleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.ideaDescriptionTextView)
        val problemTextView: TextView = itemView.findViewById(R.id.problemTextView)
        val uspTextView: TextView = itemView.findViewById(R.id.uspTextView)
        val linksTextView: TextView = itemView.findViewById(R.id.linksTextView)
        val filesTextView: TextView = itemView.findViewById(R.id.filesTextView)
        val copiedFilesTextView: TextView = itemView.findViewById(R.id.copiedFilesTextView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea_layout, parent, false)
        return IdeaViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val currentIdea = ideaList[position]

        holder.titleTextView.text = "Title: ${currentIdea.title}"
        holder.descriptionTextView.text = "Description: ${currentIdea.description}"
        holder.problemTextView.text = "Problem: ${currentIdea.problem}"
        holder.uspTextView.text = "USP: ${currentIdea.usp}"

        holder.linksTextView.text = if (currentIdea.links.isEmpty()) {
            "No links attached"
        } else {
            "Links: ${currentIdea.links.joinToString(", ")}"
        }

        holder.filesTextView.text = if (currentIdea.files.isEmpty()) {
            "No files attached"
        } else {
            "Files: ${currentIdea.files.joinToString(", ") { it.lastPathSegment ?: "Unknown" }}"
        }

        holder.copiedFilesTextView.text = if (currentIdea.copiedFiles.isEmpty()) {
            "No files copied"
        } else {
            "Copied Files: ${currentIdea.copiedFiles.joinToString("\n")}"
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = ideaList.size
}
