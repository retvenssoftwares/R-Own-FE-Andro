package app.retvens.rown.NavigationFragments.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.ConnectionCollection.hospitalityExpertInfo
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.viewRequests.ExperienceAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.*
import app.retvens.rown.databinding.ActivityUserDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsActivity : AppCompatActivity(),
    BottomSheetEditExperience.OnBottomEditExClickListener,
    BottomSheetEditEducation.OnBottomEditEdClickListener {

    lateinit var binding : ActivityUserDetailsBinding
    private lateinit var experienceAdapter: ExperienceAdapter
    private lateinit var educationAdapter: EducationAdapter
    private lateinit var type:TextInputEditText
    private lateinit var company:TextInputEditText
    private lateinit var disignation:TextInputEditText
    var role = ""
    var isOwner = true
    var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        user_id = sharedPreferences?.getString("user_id", "").toString()

        val isViewer = intent.getStringExtra("viewer")
        val userID = intent.getStringExtra("userID")
        if (isViewer == "viewer"){
            binding.addExperience.visibility = View.GONE
            binding.addEducation.visibility = View.GONE
            isOwner = false
            getProfile(userID!!)
        } else {
            getProfile(user_id)
        }

        binding.communityDetailBackBtn.setOnClickListener{ onBackPressed() }

        binding.addExperience.setOnClickListener {
            if (role =="Normal User" ){
                val bottomSheet = BottomSheetEditExperience("Normal User")
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetEditExperience.Edit_TAG)}
                bottomSheet.setOnEditExClickListener(this)
            }else{
                val bottomSheet = BottomSheetEditExperience("Hospitality Expert")
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetEditExperience.Edit_TAG)}
                bottomSheet.setOnEditExClickListener(this)
            }

        }

        binding.addEducation.setOnClickListener {
            val bottomSheet = BottomSheetEditEducation()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEditEducation.Edit_TAG)}
            bottomSheet.setOnEditEdClickListener(this)
        }

        binding.recyclerExperience.layoutManager = LinearLayoutManager(this)
        binding.recyclerExperience.setHasFixedSize(true)

        binding.recyclerEducation.layoutManager = LinearLayoutManager(this)
        binding.recyclerEducation.setHasFixedSize(true)

    }

    private fun getProfile(user_id : String) {

        val fetchUser = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        fetchUser.enqueue(object : Callback<UserProfileRequestItem?>,
            ExperienceAdapter.OnBottomSheetFilterCommunityClickListener,
            BottomSheetJobType.OnBottomJobTypeClickListener,
            BottomSheetCompany.OnBottomCompanyClickListener,
            BottomSheetJobTitle.OnBottomJobTitleClickListener,
            EducationAdapter.OnBottomSheetFilterCommunityClickListener,
            BottomSheetUpdateExperience.OnBottomEditExClickListener,
            BottomSheetUpdateEducation.OnBottomEditEdClickListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    try {
                        val response = response.body()!!

                        if (response.Profile_pic!!.isNotEmpty()) {
                            Glide.with(applicationContext).load(response.Profile_pic)
                                .into(binding.vendorProfile)
                        } else {
                            binding.vendorProfile.setImageResource(R.drawable.svg_user)
                        }

                        role = response.Role

                        binding.vendorName.text = response.Full_name
                        binding.username.text = response.User_name
                        if (response.userBio.isNotEmpty()) {
                            binding.bio.text = response.userBio
                        }else{
                            binding.bio.text = "Bio not added"
                        }

                            if (response.Role == "Normal User"){
                                Log.e("res", response.normalUserInfo.toString())
                                experienceAdapter = ExperienceAdapter(this@UserDetailsActivity,response, isOwner, "Normal User")
                                experienceAdapter.setOnFilterClickListener(this)
                            } else if (response.Role == "Hospitality Expert"){
                                Log.e("res", response.hospitalityExpertInfo.toString())
                                experienceAdapter = ExperienceAdapter(this@UserDetailsActivity,response, isOwner, "Hospitality Expert")
//                                binding.recyclerExperience.adapter = experienceAdapter
//                                experienceAdapter.notifyDataSetChanged()
                                experienceAdapter.setOnFilterClickListener(this)
                            }
                        binding.recyclerExperience.adapter = experienceAdapter
                        experienceAdapter.notifyDataSetChanged()

                            if (response.normalUserInfo.isEmpty() && response.hospitalityExpertInfo.isEmpty()) {
                                binding.expError.visibility = View.VISIBLE
                            }



                        if (response.studentEducation.isEmpty()){
                            binding.eduError.visibility = View.VISIBLE
                        } else {
                            educationAdapter = EducationAdapter(this@UserDetailsActivity,response, isOwner)
                            binding.recyclerEducation.adapter = educationAdapter
                            educationAdapter.notifyDataSetChanged()
                            educationAdapter.setOnFilterClickListener(this)
                        }

                    }catch (e:NullPointerException){
                        Log.e("error",e.message.toString())
                    }


                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }

            override fun onBottomSheetFilterCommunityClick(
                jonDetails: NormalUserInfoo,
                hospitalityExpertInfo: hospitalityExpertInfo,
                position: Int
            ) {
                val bottomSheet = BottomSheetUpdateExperience(jonDetails,hospitalityExpertInfo,position,role)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetUpdateExperience.Edit_TAG)}
                bottomSheet.setOnEditExClickListener(this)

            }

            override fun bottomJobTypeClick(jobTypeFrBo: String) {
                type.setText(jobTypeFrBo)
            }

            override fun bottomLocationClick(CompanyFrBo: String) {
                company.setText(CompanyFrBo)
            }

            override fun bottomJobTitleClick(jobTitleFrBo: String) {
                disignation.setText(jobTitleFrBo)
            }

            override fun onBottomSheetFilterCommunityClick(
                jonDetails: UserProfileRequestItem.StudentEducation,
                position: Int
            ) {
                val bottomSheet = BottomSheetUpdateEducation(jonDetails,position)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetUpdateEducation.Edit_TAG)}
                bottomSheet.setOnEditEdClickListener(this)
            }

            override fun bottomEditClick() {
                getProfile(user_id)
            }

            override fun bottomEditEdClick() {
                getProfile(user_id)
            }


        })

    }




    override fun bottomEditClick() {
        getProfile(user_id)
    }

    override fun bottomEditEdClick() {
        getProfile(user_id)
    }
}