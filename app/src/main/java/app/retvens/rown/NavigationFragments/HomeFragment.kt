package app.retvens.rown.NavigationFragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
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
        fragManager?.let { bottomSheet.show(it, BottomSheet.TAG) }

        val gesture = GestureDetector(
            activity,
            object : SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(
                    e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Log.i("tg", "onFling has been called!")
                    val SWIPE_MIN_DISTANCE = 120
                    val SWIPE_MAX_OFF_PATH = 250
                    val SWIPE_THRESHOLD_VELOCITY = 200
                    try {
                        if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) return false
                        if (e1.x - e2.x > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                        ) {
                            val transaction: FragmentTransaction =
                                fragmentManager!!.beginTransaction()
                            transaction.replace(R.id.fragment_container, ChatFragment())
                            transaction.addToBackStack(null)
                            transaction.commit()
                            Toast.makeText(context,"Chats",Toast.LENGTH_SHORT).show()
                        } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                        ) {
                            Toast.makeText(context,"LtR",Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })

        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return gesture.onTouchEvent(event!!)
            }
        })
    }
}