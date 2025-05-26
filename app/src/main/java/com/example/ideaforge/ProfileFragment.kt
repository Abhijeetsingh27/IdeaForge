package com.example.ideaforge

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find views by their IDs
        val profileTitle: TextView? = view.findViewById(R.id.profileTitle)
        val settingsIcon: ImageView? = view.findViewById(R.id.settingsIcon)
        val profileCard: MaterialCardView? = view.findViewById(R.id.profileCard)
        val profileImage: ImageView? = view.findViewById(R.id.profileImage)
        val profileName: TextView? = view.findViewById(R.id.profileName)
        val profileEmail: TextView? = view.findViewById(R.id.profileEmail)
        val profilePhone: TextView? = view.findViewById(R.id.profilePhone)
        val profileAddress: TextView? = view.findViewById(R.id.profileAddress)
        val aboutTitle: TextView? = view.findViewById(R.id.aboutTitle)
        val profileAbout: TextView? = view.findViewById(R.id.profileAbout)
        val btnEditProfile: Button? = view.findViewById(R.id.btnEditProfile)
        val btnLogout: Button? = view.findViewById(R.id.btnLogout)

        // Set sample data (replace with your actual data loading logic)
        profileTitle?.text = "My Profile"
        profileName?.text = "John Doe"
        profileEmail?.text = "john.doe@example.com"
        profilePhone?.text = "+91 9876543210"
        profileAddress?.text = "123, New Delhi, India"
        profileAbout?.text = "Android Developer who loves building clean, intuitive and responsive UIs."
        profileImage?.setImageResource(R.drawable.profile_pic) // Make sure you have this drawable

        // Set onClickListeners for the buttons and icons
        settingsIcon?.setOnClickListener {
            // Handle settings icon click (e.g., navigate to settings screen)
            Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show()
            // Example of starting a new activity:
            // startActivity(Intent(context, SettingsActivity::class.java))
        }

        btnEditProfile?.setOnClickListener {
            // Handle edit profile button click (e.g., navigate to edit profile screen)
            Toast.makeText(context, "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
            // Example of starting a new activity:
            // startActivity(Intent(context, EditProfileActivity::class.java))
        }

        btnLogout?.setOnClickListener {
            // Handle logout button click (e.g., perform logout actions and navigate to login screen)
            Toast.makeText(context, "Logout Clicked", Toast.LENGTH_SHORT).show()
            // In a real app, you would:
            // 1. Clear user session (e.g., remove tokens, clear shared preferences)
            // 2. Navigate the user back to the login screen
            // Example of starting a new activity and finishing the current one:
            // startActivity(Intent(context, LoginActivity::class.java))
            // activity?.finish()
        }

        // You can add more logic here to fetch and display actual user data

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}