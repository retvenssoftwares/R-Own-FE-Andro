package app.retvens.rown.Dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ChatSection.ChatActivity
import app.retvens.rown.NavigationFragments.*
import app.retvens.rown.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.lang.Math.abs

class DashBoardActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var toggle: ActionBarDrawerToggle


    internal open class OnSwipeTouchListener(c: Context?) :
        View.OnTouchListener {
        private val gestureDetector: GestureDetector
        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(motionEvent)
        }
        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD: Int = 100
            private val SWIPE_VELOCITY_THRESHOLD: Int = 100
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                onClick()
                return super.onSingleTapUp(e)
            }
            override fun onDoubleTap(e: MotionEvent): Boolean {
                onDoubleClick()
                return super.onDoubleTap(e)
            }
            override fun onLongPress(e: MotionEvent) {
                onLongClick()
                super.onLongPress(e)
            }
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(
                                velocityX
                            ) > SWIPE_VELOCITY_THRESHOLD
                        ) {
                            if (diffX > 0) {
                                onSwipeRight()
                            }
                            else {
                                onSwipeLeft()
                            }
                        }
                    }
                    else {
                        if (abs(diffY) > SWIPE_THRESHOLD && abs(
                                velocityY
                            ) > SWIPE_VELOCITY_THRESHOLD
                        ) {
                            if (diffY < 0) {
                                onSwipeUp()
                            }
                            else {
                                onSwipeDown()
                            }
                        }
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return false
            }
        }
        open fun onSwipeRight() {}
        open fun onSwipeLeft() {}
        open fun onSwipeUp() {}
        open fun onSwipeDown() {}
        private fun onClick() {}
        private fun onDoubleClick() {}
        private fun onLongClick() {}
        init {
            gestureDetector = GestureDetector(c, GestureListener())
        }
    }

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        replaceFragment(HomeFragment())
        //setUp Buttons
        val setting = findViewById<Button>(R.id.Setting)
        val logoutbtn = findViewById<Button>(R.id.logoutbtn)

        setting.setOnClickListener {
            Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
        }

        logoutbtn.setOnClickListener {
            Toast.makeText(applicationContext,"logout",Toast.LENGTH_SHORT).show()
        }


        //setUp drawerLayout
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)



        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Set the toolbar as the support action bar
        setSupportActionBar(toolbar)

        // Set the title of the action bar
        supportActionBar?.title = "My Action Bar Title"

        // Enable the up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24)
        // Set click listener for the up button
        toolbar.setNavigationOnClickListener { onBackPressed() }

        //setUp Header
        val header = LayoutInflater.from(this).inflate(R.layout.nav_header_dashboard,navView,false)
        navView.addHeaderView(header)

        //setUp BottomNav
        val bottom_Nav = findViewById<BottomNavigationView>(R.id.nav_Bottom)

        bottom_Nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.jobs -> replaceFragment(JobFragment())
                R.id.explore -> replaceFragment(ExploreFragment())
                R.id.events -> replaceFragment(EventFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                else -> null
            }
            true
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.partner -> Toast.makeText(applicationContext,"click", Toast.LENGTH_SHORT).show()
                R.id.tutorial ->  Toast.makeText(applicationContext,"click", Toast.LENGTH_SHORT).show()

            }

            true
        }

        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val frame = findViewById<FrameLayout>(R.id.fragment_container)

        frame.setOnClickListener(object : OnSwipeTouchListener(this@DashBoardActivity),
            View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(applicationContext,"left swipe",Toast.LENGTH_SHORT).show()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                Toast.makeText(applicationContext,"left swipe",Toast.LENGTH_SHORT).show()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                Toast.makeText(applicationContext,"right swipe",Toast.LENGTH_SHORT).show()
            }

        })

    }


    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_chats -> {
                Toast.makeText(applicationContext,"chats",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_notify -> {
                Toast.makeText(applicationContext,"Notification",Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
