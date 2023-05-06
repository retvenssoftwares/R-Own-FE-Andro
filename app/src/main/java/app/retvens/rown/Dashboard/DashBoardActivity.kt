package app.retvens.rown.Dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.AppDatabase
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.ChatSection.MesiboUsers
import app.retvens.rown.ChatSection.ReceiverProfileAdapter
import app.retvens.rown.ChatSection.UserChatList
import app.retvens.rown.CreateCommunity.CreateCommunity
import app.retvens.rown.Dashboard.profileCompletion.UserName
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.MainActivity
import app.retvens.rown.NavigationFragments.*
import app.retvens.rown.R
import app.retvens.rown.authentication.LoginActivity
import app.retvens.rown.databinding.ActivityDashBoardBinding
import app.retvens.rown.utils.clearProgress
import app.retvens.rown.utils.clearUserId
import app.retvens.rown.utils.moveToClear
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Math.abs


class DashBoardActivity : AppCompatActivity() {

    companion object number{
        var progress = ""
    }

    lateinit var binding: ActivityDashBoardBinding

    private lateinit var drawerLayout:DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var mActivityTitle : String
    var isPLVisible : Boolean? = false
    var isHLVisible : Boolean? = false

    private lateinit var auth:FirebaseAuth
    private lateinit var viewPager:ViewPager
    lateinit var dialog: Dialog
    private lateinit var popularUsersAdapter: PopularUsersAdapter
    private  var userList: List<MesiboUsersData> = emptyList()
    private lateinit var profile:ImageView
    private lateinit var name:TextView
    private lateinit var phone:TextView

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

//        replaceFragment(HomeFragment())
            replaceFragment(HomeFragment())



        //setUp drawerLayout
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        mActivityTitle = title.toString()

        Toast.makeText(applicationContext,number.progress,Toast.LENGTH_SHORT).show()

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Set the toolbar as the support action bar
        setSupportActionBar(toolbar)


        // Enable the up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24)
        // Set click listener for the up button
        toolbar.setNavigationOnClickListener { onBackPressed() }

        //setUp Header
        val header = LayoutInflater.from(this).inflate(R.layout.nav_header_dashboard,navView,false)
        navView.addHeaderView(header)
        val sharedPreferences = getSharedPreferences("SaveProgress", AppCompatActivity.MODE_PRIVATE)
        val toPo = sharedPreferences.getString("progress", "50").toString()
        val progress :Int = toPo.toInt()
        header.findViewById<TextView>(R.id.complete_your_profile).setOnClickListener {
            if (progress == 100){
                Toast.makeText(this, "You've already completed Your Profile", Toast.LENGTH_SHORT).show()
            }else {
                startActivity(Intent(this, UserName::class.java))
            }
        }
        val progressBar = header.findViewById<LinearProgressIndicator>(R.id.progress_Bar)

        if (progress != null) {
            if (progress >= 50){
                header.findViewById<TextView>(R.id.isComplete).text = "Your Profile is $progress% Complete"
                progressBar.progress = progress
            }
        }


        profile = header.findViewById<ImageView>(R.id.nav_profile)
        name = header.findViewById<TextView>(R.id.user_name)
        phone = header.findViewById<TextView>(R.id.nav_phone)

        getProfileInfo()

        //setUp BottomNav
        val bottom_Nav = findViewById<BottomNavigationView>(R.id.nav_Bottom)

        bottom_Nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.jobs -> replaceFragment(JobFragment())
//                R.id.jobs -> replaceFragment(JobsForHoteliers())
//                R.id.explore -> replaceFragment(ExploreFragment())
                R.id.explore -> replaceFragment(JobsForHoteliers())
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

        binding.navSecurity.setOnClickListener {
            if (!isPLVisible!!){
                binding.privacyList.visibility = View.VISIBLE
                isPLVisible = true
            }else{
                binding.privacyList.visibility = View.GONE
                isPLVisible = false
            }
        }

        binding.navHelp.setOnClickListener {
            if (!isHLVisible!!){
                binding.navHelpList.visibility = View.VISIBLE
                isHLVisible = true
            }else{
                binding.navHelpList.visibility = View.GONE
                isHLVisible = false
            }
        }

        binding.Setting.setOnClickListener {
            Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
        }

        binding.logout.setOnClickListener {

            auth.signOut()
            SharedPreferenceManagerAdmin.getInstance(applicationContext).clear()
            moveToClear(applicationContext)
            clearUserId(applicationContext)
            clearProgress(applicationContext)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }

    private fun getProfileInfo() {

        val sharedPreferences = applicationContext?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val send = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        send.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    name.text = response.Full_name
                    phone.text = response.Phone
                    Glide.with(applicationContext).load(response.Profile_pic).into(profile)
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getMesiboUsers() {
        val send = RetrofitBuilder.retrofitBuilder.getMesiboUsers()

        send.enqueue(object : Callback<UsersList?> {
            override fun onResponse(call: Call<UsersList?>, response: Response<UsersList?>) {
                if (response.isSuccessful) {
                    val response = response.body()!!
                    if (response.result) {
                        userList = response.users
                        // Update the adapter with the new data
                        popularUsersAdapter.userList = userList ?: emptyList()
                        popularUsersAdapter.notifyDataSetChanged()


                    }
                }else{
                    Toast.makeText(applicationContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UsersList?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
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
                val myFragment = ChatFragment()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, myFragment)
                    addToBackStack(null)
                    commit()
                }
                return true
            }
            R.id.action_notify -> {
                startActivity(Intent(this,CreateCommunity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}

