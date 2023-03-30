package app.retvens.rown.Dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import app.retvens.rown.authentication.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Math.abs


class DashBoardActivity : AppCompatActivity(), GestureDetector.OnGestureListener{

    private lateinit var gestureDetector: GestureDetector
    private lateinit var toggle: ActionBarDrawerToggle
    var x1:Float = 0.0f
    var x2:Float = 0.0f
    var y1:Float = 0.0f
    var y2:Float = 0.0f
    private lateinit var auth:FirebaseAuth

    companion object{
        const val MIN_DISTANCE = 150
    }


    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        window.statusBarColor = Color.TRANSPARENT

        auth = FirebaseAuth.getInstance()

//        replaceFragment(HomeFragment())
            replaceFragment(HomeFragment())

        //setUp Buttons
        val setting = findViewById<Button>(R.id.Setting)
        val logoutbtn = findViewById<Button>(R.id.logoutbtn)

        setting.setOnClickListener {
            Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
        }

        logoutbtn.setOnClickListener {

            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))

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


        gestureDetector = GestureDetector(this,this@DashBoardActivity)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        Toast.makeText(applicationContext,"op",Toast.LENGTH_SHORT)

        gestureDetector.onTouchEvent(event!!)

        when(event.action){
            0 ->{
                x1 = event.x
                y1 = event.y
            }
            1->{
                x2 = event.x
                y2 = event.y

                val valueX:Float = x2-x1
                val valueF:Float = y2-y1

                if (abs(valueX) > MIN_DISTANCE){

                    if (x2 > x1){
                        Toast.makeText(applicationContext,"Right swipe",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,"Left swipe",Toast.LENGTH_SHORT).show()
                    }

                }
                else if (abs(valueF) > MIN_DISTANCE){
                    if (y2 > y1){
                        Toast.makeText(applicationContext,"Bottom swipe",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,"Up swipe",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


        return super.onTouchEvent(event)
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

    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }



}

