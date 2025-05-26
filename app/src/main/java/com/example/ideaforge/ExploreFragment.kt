package com.example.ideaforge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri
import android.widget.TextView
import com.example.ideaforge.databinding.FragmentExploreBinding // Import the binding class

// Data class to represent an idea
data class IdeaData(  // Define the IdeaData class here
    val title: String,
    val description: String,
    val problem: String,
    val usp: String,
    val links: List<String>,
    val files: List<Uri>,
    val copiedFiles: List<String>
)

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var ideaRecyclerView: RecyclerView
    private lateinit var ideaAdapter: IdeaAdapter
    private val ideaList = mutableListOf<IdeaData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        ideaRecyclerView = binding.ideaRecyclerView
        ideaRecyclerView.layoutManager = LinearLayoutManager(context)
        ideaAdapter = IdeaAdapter(ideaList)
        ideaRecyclerView.adapter = ideaAdapter

        // Observe the data from the SharedViewModel
        observeIdeaData()
    }

    private fun observeIdeaData() {
        // Observe each LiveData and update the ideaList
        sharedViewModel.ideaTitle.observe(viewLifecycleOwner) { title ->
            updateIdeaList()
        }
        sharedViewModel.ideaDescription.observe(viewLifecycleOwner) { description ->
            updateIdeaList()
        }
        sharedViewModel.problem.observe(viewLifecycleOwner) { problem ->
            updateIdeaList()
        }
        sharedViewModel.usp.observe(viewLifecycleOwner) { usp ->
            updateIdeaList()
        }
        sharedViewModel.attachedLinks.observe(viewLifecycleOwner) { links ->
            updateIdeaList()
        }
        sharedViewModel.attachedFiles.observe(viewLifecycleOwner) { files ->
            updateIdeaList()
        }
        sharedViewModel.copiedFiles.observe(viewLifecycleOwner) { copiedFiles ->
            updateIdeaList()
        }
    }

    private fun updateIdeaList() {
        // Clear the current list
        ideaList.clear()

        // Add the current idea from ViewModel to the list
        val idea = IdeaData(
            title = sharedViewModel.ideaTitle.value ?: "", // Default to empty string
            description = sharedViewModel.ideaDescription.value ?: "",
            problem = sharedViewModel.problem.value ?: "",
            usp = sharedViewModel.usp.value ?: "",
            links = sharedViewModel.attachedLinks.value ?: emptyList(),
            files = sharedViewModel.attachedFiles.value ?: emptyList(),
            copiedFiles = sharedViewModel.copiedFiles.value ?: emptyList()
        )
        ideaList.add(idea)

        // Notify the adapter that the data has changed
        ideaAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Adapter for the RecyclerView
//class IdeaAdapter(private val ideaList: List<IdeaData>) :  // Use IdeaData here
//    RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder>() {
//
//    // ViewHolder for each item
//    class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
//        val problemTextView: TextView = itemView.findViewById(R.id.problemTextView)
//        val uspTextView: TextView = itemView.findViewById(R.id.uspTextView)
//        val linksTextView: TextView = itemView.findViewById(R.id.linksTextView)
//        val filesTextView: TextView = itemView.findViewById(R.id.filesTextView)
//        val copiedFilesTextView: TextView = itemView.findViewById(R.id.copiedFilesTextView)
//    }
//
//    // Create new views (invoked by the layout manager)
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_idea_layout, parent, false) // Use idea_item_layout.xml
//        return IdeaViewHolder(itemView)
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
//        val currentIdea = ideaList[position]
//        holder.titleTextView.text = "Title: ${currentIdea.title}"
//        holder.descriptionTextView.text = "Description: ${currentIdea.description}"
//        holder.problemTextView.text = "Problem: ${currentIdea.problem}"
//        holder.uspTextView.text = "USP: ${currentIdea.usp}"
//
//        val linkText =
//            if (currentIdea.links.isEmpty()) "No links attached" else "Links: ${currentIdea.links.joinToString(", ")}"
//        holder.linksTextView.text = linkText
//
//        val fileText =
//            if (currentIdea.files.isEmpty()) "No files attached" else "Files: ${currentIdea.files.joinToString(", ") { it.lastPathSegment ?: "Unknown" }}"
//        holder.filesTextView.text = fileText
//
//        val copiedFileText =
//            if (currentIdea.copiedFiles.isEmpty()) "No files copied" else "Copied Files: ${currentIdea.copiedFiles.joinToString("\n")}"
//        holder.copiedFilesTextView.text = copiedFileText
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = ideaList.size
//}
