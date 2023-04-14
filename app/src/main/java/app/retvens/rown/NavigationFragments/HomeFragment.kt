package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet

class HomeFragment : Fragment() {
    lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = BottomSheet()
        val fragManager = (activity as FragmentActivity).supportFragmentManager
        fragManager?.let{bottomSheet.show(it, BottomSheet.TAG)}


        val btn = view.findViewById<CardView>(R.id.community_btn)

        btn.setOnClickListener {
            startActivity(Intent(context,CreateCommunity::class.java))

        }

    }
}