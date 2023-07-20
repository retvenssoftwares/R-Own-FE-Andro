package app.retvens.rown.Dashboard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.notificationScreen.NotificationActivity
import app.retvens.rown.Dashboard.profileCompletion.UserName
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UsersList
import app.retvens.rown.MainActivity
import app.retvens.rown.MesiboApi
import app.retvens.rown.NavigationFragments.*
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventCategoryActivity
import app.retvens.rown.NavigationFragments.jobforvendors.JobsPostedByUser
import app.retvens.rown.NavigationFragments.profile.viewConnections.ViewConnectionsActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheet.Companion.TAG
import app.retvens.rown.databinding.ActivityDashBoardBinding
import app.retvens.rown.sideNavigation.*
import app.retvens.rown.utils.*
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllBlogsActivity
import com.arjun.compose_mvvm_retrofit.SharedPreferenceManagerAdmin
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    private lateinit var checkNetworkConnection: CheckNetworkConnection

    var replace = true

    var connectionNo = 0
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        startAnimation()

        Thread {
            // Run whatever background code you want here.
            replaceFragment(HomeFragment())
        }.start()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setUp drawerLayout
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val server = intent.getStringExtra("server")

        Toast.makeText(this, "$serverCode", Toast.LENGTH_SHORT).show()
        if (serverCode == 502 || server == "502"){
            binding.noInternetImage.setImageResource(R.drawable.svg_server_down)
            binding.noInternet.visibility = View.VISIBLE
            binding.noInternetLayout.visibility = View.GONE
            binding.navView.visibility = View.GONE
            binding.dashboard.visibility = View.GONE
        } else {
            binding.noInternet.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
            binding.dashboard.visibility = View.VISIBLE
            if (replace){
                replaceFragment(HomeFragment())
            }
        }

        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (serverCode == 502){
                binding.noInternetImage.setImageResource(R.drawable.svg_server_down)
                binding.noInternet.visibility = View.VISIBLE
                binding.noInternetLayout.visibility = View.GONE
                binding.navView.visibility = View.GONE
                binding.dashboard.visibility = View.GONE
            } else {
                binding.noInternet.visibility = View.GONE
                binding.navView.visibility = View.VISIBLE
                binding.dashboard.visibility = View.VISIBLE
                if (replace){
                    replaceFragment(HomeFragment())
                }
            }
            if (isConnected) {
                binding.noInternet.visibility = View.GONE
                binding.navView.visibility = View.VISIBLE
                binding.dashboard.visibility = View.VISIBLE
                if (replace){
                    replace = false
                    replaceFragment(HomeFragment())
                }
//                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            } else {
                binding.noInternet.visibility = View.VISIBLE
                binding.navView.visibility = View.GONE
                binding.dashboard.visibility = View.GONE
//                Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
            }
        }

        if (checkForInternet(this)) {
            binding.noInternet.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
            binding.dashboard.visibility = View.VISIBLE
        } else {
            binding.noInternet.visibility = View.VISIBLE
            binding.navView.visibility = View.GONE
            binding.dashboard.visibility = View.GONE
        }

        binding.retry.setOnClickListener {
            if (checkForInternet(this)) {
                binding.noInternet.visibility = View.GONE
                binding.navView.visibility = View.VISIBLE
                binding.dashboard.visibility = View.VISIBLE
            } else {
                binding.noInternet.visibility = View.VISIBLE
                binding.navView.visibility = View.GONE
                binding.dashboard.visibility = View.GONE
            }
        }

        auth = FirebaseAuth.getInstance()

        val sharedPreferences2 = getSharedPreferences("savePhoneNo", AppCompatActivity.MODE_PRIVATE)
        val phone2 = sharedPreferences2?.getString("savePhoneNumber", "").toString()



        devicetoken()

        mActivityTitle = title.toString()

        val backThread = Thread{
            MesiboApi.init(applicationContext)
            MesiboApi.startMesibo(true)
        }

        backThread.start()



//        val foregroundServiceIntent = Intent(applicationContext, MyForegroundService::class.java)
//        ContextCompat.startForegroundService(applicationContext, foregroundServiceIntent)



        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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

        val progressBar = header.findViewById<LinearProgressIndicator>(R.id.progress_Bar)

        progressBar.progress = profileCompletionStatus.toInt()

        header.findViewById<TextView>(R.id.isComplete).text = "Your Profile is $profileCompletionStatus% Complete"

         header.findViewById<TextView>(R.id.complete_your_profile).setOnClickListener {
            if (profileCompletionStatus == "100"){
                header.findViewById<TextView>(R.id.complete_your_profile).text = "Profile completed"
                progressBar.progress = profileCompletionStatus.toInt()
                header.findViewById<TextView>(R.id.isComplete).text = "Your Profile is $profileCompletionStatus% Complete"
                Toast.makeText(this, "You've already completed Your Profile", Toast.LENGTH_SHORT).show()
            }else {
                startActivity(Intent(this, UserName::class.java))
            }
        }

            if (profileCompletionStatus == "100"){
                header.findViewById<TextView>(R.id.complete_your_profile).text = "Profile completed"
            }

        profile = header.findViewById<ImageView>(R.id.nav_profile)
        val verification = header.findViewById<ImageView>(R.id.verification)
        name = header.findViewById<TextView>(R.id.user_name)
        val phoneH = header.findViewById<TextView>(R.id.nav_phone)

        if (verificationStatus != "false" && verificationStatus != ""){
            verification.visibility = View.VISIBLE
        }
        phoneH.text = phone

        val sharedPreferencesName = getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()

        val sharedPreferencesPic = getSharedPreferences("SaveProfileImage", AppCompatActivity.MODE_PRIVATE)
        val profilePic = sharedPreferencesPic?.getString("profile_image", "").toString()

        if (profilePic.isNotEmpty()) {
            Glide.with(applicationContext).load(profilePic).into(profile)
        }  else {
            if (profileImage.isNotEmpty()) {
                Glide.with(this).load(profileImage).into(profile)
            } else{
            getProfileInfo(this){
                if (it == 502){
                    binding.noInternetImage.setImageResource(R.drawable.svg_server_down)
                    binding.noInternet.visibility = View.VISIBLE
                    binding.noInternetLayout.visibility = View.GONE
                    binding.navView.visibility = View.GONE
                    binding.dashboard.visibility = View.GONE
                }
            }
                if (profileImage.isNotEmpty()) {
                    Glide.with(this).load(profileImage).into(profile)
                } else {
                    profile.setImageResource(R.drawable.svg_user)
                }
            }
        }
        name.setText("Hi $profileName")

        Thread{
            bottomNavSetUp(toolbar, header)
        }.start()

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
        if (role =="Hotel Owner" && profileCompletionStatus == "100"){
            binding.postedJobsSn.visibility = View.GONE
            binding.appliedJobsSn.visibility = View.GONE
        } else {
            binding.postedJobsSn.visibility = View.GONE
            binding.appliedJobsSn.visibility = View.GONE
        }
        binding.notificationsSn.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        binding.myConnectionsSn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, ViewConnectionsActivity::class.java))
        }
        binding.appliedJobsSn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            replaceFragment(JobFragment())
        }
        binding.postedJobsSn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, JobsPostedByUser::class.java))
        }
        binding.eventsSn.setOnClickListener {
            startActivity(Intent(this, AllEventCategoryActivity::class.java))
        }
        binding.blogsSn.setOnClickListener {
            startActivity(Intent(this, ViewAllBlogsActivity::class.java))
        }

        binding.navSecurity.setOnClickListener {
            if (!isPLVisible!!){
                binding.privacyList.visibility = View.VISIBLE
                isPLVisible = true
                binding.securetyArrow.rotation = -180f
            }else{
                binding.privacyList.visibility = View.GONE
                isPLVisible = false
                binding.securetyArrow.rotation = 0f
            }
        }

        binding.navHelp.setOnClickListener {
            if (!isHLVisible!!){
                binding.navHelpList.visibility = View.VISIBLE
                isHLVisible = true
                binding.navArrow.rotation = -180f
            }else{
                binding.navHelpList.visibility = View.GONE
                isHLVisible = false
                binding.navArrow.rotation = 0f
            }
        }

        binding.tnc.setOnClickListener {
            startActivity(Intent(this, TermsAndCActivity::class.java))
        }

        binding.pp.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }

        binding.dropMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("rown@retvensservices.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...")
//            intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...")
            intent.putExtra(Intent.EXTRA_CC, "rown@retvensservices.com")
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }

        binding.chatWithUs.setOnClickListener {
            startActivity(Intent(applicationContext, ChatWithUsActivity::class.java))
        }

        binding.cardFaq.setOnClickListener {
            startActivity(Intent(applicationContext, FAQ_Activity::class.java))

        }

        binding.cardBug.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(applicationContext, BugSpottedActivity::class.java))
        }

        binding.Setting.setOnClickListener {
            Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
        }

        binding.logout.setOnClickListener {

            auth.signOut()
            SharedPreferenceManagerAdmin.getInstance(applicationContext).clear()
            moveToClear(applicationContext)
            clearUserId(applicationContext)
            clearFullName(applicationContext)
            clearProfileImage(applicationContext)
            clearConnectionNo(applicationContext)
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }


    }

    private fun bottomNavSetUp(toolbar: Toolbar, header: View) {

        //setUp BottomNav
        val bottom_Nav = findViewById<BottomNavigationView>(R.id.nav_Bottom)

        header.findViewById<CardView>(R.id.my_account).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            if (role =="Hotel Owner" && profileCompletionStatus == "100"){
            replaceFragment(ProfileFragmentForHotelOwner())
            toolbar.visibility = View.GONE
        }else if (role == "Business Vendor / Freelancer" && profileCompletionStatus == "100"){
            replaceFragment(ProfileFragmentForVendors())
            toolbar.visibility = View.GONE
        }else {
            replaceFragment(ProfileFragment())
            toolbar.visibility = View.GONE
        }
            toolbar.visibility = View.GONE
        }

        bottom_Nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    toolbar.visibility = View.VISIBLE
                }
                R.id.jobs ->
                    if ((role =="Hotel Owner") && profileCompletionStatus == "100"){
                        replaceFragment(JobsForHoteliers())
                        toolbar.visibility = View.GONE
                    }else{
                        replaceFragment(JobFragment())
                        toolbar.visibility = View.GONE
                    }
                R.id.explore -> {
                    replaceFragment(ExploreFragment())
                    toolbar.visibility = View.GONE
                }
                R.id.events ->
//                    replaceFragment(EventFragmentForHoteliers())
                    if (role =="Hotel Owner" && profileCompletionStatus == "100"){
                        replaceFragment(EventFragmentForHoteliers())
                        toolbar.visibility = View.GONE
                    }else{
                        replaceFragment(EventFragment())
                        toolbar.visibility = View.GONE
                    }
                R.id.profile -> {
                    if (role =="Hotel Owner" && profileCompletionStatus == "100"){
                        replaceFragment(ProfileFragmentForHotelOwner())
                        toolbar.visibility = View.GONE
                    }else if (role == "Business Vendor / Freelancer" && profileCompletionStatus == "100"){
                        replaceFragment(ProfileFragmentForVendors())
                        toolbar.visibility = View.GONE
                    }else {
                        replaceFragment(ProfileFragment())
                        toolbar.visibility = View.GONE
                    }
//                    replaceFragment(ProfileFragmentForVendors())
                }
                else -> null
            }
            true
        }
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
           /* R.id.action_settings -> {
                Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
                return true
            }*/
            R.id.action_chats -> {
               startActivity(Intent(this,ChatActivity::class.java))
                return true
            }
            R.id.action_notify -> {
                startActivity(Intent(this,NotificationActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun devicetoken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast

            Log.e("token",token.toString())

        })
    }

    private fun startAnimation() {
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()

        val anim = ObjectAnimator.ofFloat(imageView, "translationX", screenWidth, -imageView.width.toFloat())
        anim.duration = 1000
        anim.interpolator = LinearInterpolator()

        val revealAnim = ValueAnimator.ofFloat(0f, 1f)
        revealAnim.duration = 1500
        revealAnim.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Float
            textView.alpha = alpha
        }

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                textView.visibility = View.VISIBLE
            }
        })

        anim.start()
        revealAnim.startDelay = anim.duration
        revealAnim.start()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }





}

