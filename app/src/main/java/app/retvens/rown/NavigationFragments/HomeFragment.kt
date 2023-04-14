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

class HomeFragment : Fragment(), GestureDetector.OnGestureListener {
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

//        view?.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//
//            }
//        })
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
//    fun onTouchEvent(event: MotionEvent): Boolean {
//        return if (gestureDetector.onTouchEvent(event)) {
//            true
//        }
//        else {
////            super.onTouchEvent(event)
//            super.requireActivity().onTouchEvent(event)
////            view?.onTouchEvent(event)
//        }
//    }

    override fun onShowPress(e: MotionEvent) {
        return
    }
    override fun onDown(e: MotionEvent): Boolean {
        return false
    }
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }
    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }
    override fun onLongPress(p0: MotionEvent) {
        return
    }
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        Toast.makeText(context, "Left to Right swipe gesture", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Right to Left swipe gesture", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }

}