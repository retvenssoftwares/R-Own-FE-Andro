package app.retvens.rown.Dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.retvens.rown.R


class ChatParentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_parent, container, false)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar

        // Hide the ActionBar (Toolbar)

        // Hide the ActionBar (Toolbar)
        actionBar?.hide()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.notifications_backBtn).setOnClickListener{
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
        }



        val myFragment = app.retvens.rown.MessagingModule.UserListFragment()
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_chat, myFragment)
            addToBackStack(null)
            commit()

        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}