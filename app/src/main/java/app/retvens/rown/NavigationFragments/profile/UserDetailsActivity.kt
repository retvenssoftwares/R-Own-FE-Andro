package app.retvens.rown.NavigationFragments.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.NavigationFragments.profile.viewRequests.ExperienceAdapter
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.*
import app.retvens.rown.databinding.ActivityUserDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackBtn.setOnClickListener{ onBackPressed() }

        binding.addExperience.setOnClickListener {
            val bottomSheet = BottomSheetEditExperience()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEditExperience.Edit_TAG)}
            bottomSheet.setOnEditExClickListener(this)
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

        getProfile()

    }

    private fun getProfile() {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val fetchUser = RetrofitBuilder.retrofitBuilder.fetchUser(user_id)

        fetchUser.enqueue(object : Callback<UserProfileRequestItem?>,
            ExperienceAdapter.OnBottomSheetFilterCommunityClickListener,
            BottomSheetJobType.OnBottomJobTypeClickListener,
            BottomSheetCompany.OnBottomCompanyClickListener,
            BottomSheetJobTitle.OnBottomJobTitleClickListener {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    experienceAdapter = ExperienceAdapter(applicationContext,response)
                    binding.recyclerExperience.adapter = experienceAdapter
                    experienceAdapter.notifyDataSetChanged()
                    experienceAdapter.setOnFilterClickListener(this)
                    Glide.with(applicationContext).load(response.Profile_pic).into(binding.vendorProfile)
                    binding.vendorName.text = response.Full_name
                    binding.username.text = response.User_name
                    binding.bio.text = response.userBio

                    educationAdapter = EducationAdapter(applicationContext,response)
                    binding.recyclerEducation.adapter = educationAdapter
                    educationAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }

            override fun onBottomSheetFilterCommunityClick(
                jonDetails: NormalUserInfoo,
                position: Int
            ) {

                val dialog = Dialog(baseContext)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.bottom_sheet_edit_expereince)

                dialog.show()
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
                dialog.window?.setGravity(Gravity.BOTTOM)

                disignation = dialog.findViewById<TextInputEditText>(R.id.rJob_et)
                company = dialog.findViewById<TextInputEditText>(R.id.recentComET)
                type = dialog.findViewById<TextInputEditText>(R.id.eType_et)
                val start = dialog.findViewById<TextInputEditText>(R.id.et_session_Start)
                val end = dialog.findViewById<TextInputEditText>(R.id.et_session_end)

                disignation.setText(jonDetails.jobTitle)
                company.setText(jonDetails.jobCompany)
                type.setText(jonDetails.jobType)
                start.setText(jonDetails.jobStartYear)
                end.setText(jonDetails.jobEndYear)

                type.setOnClickListener {
                    val bottomSheet = BottomSheetJobType()
                    val fragManager = supportFragmentManager
                    fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
                    bottomSheet.setOnJobTypeClickListener(this)
                }

                company.setOnClickListener {
                    val bottomSheet = BottomSheetCompany()
                    val fragManager = supportFragmentManager
                    fragManager.let{bottomSheet.show(it, BottomSheetCompany.Company_TAG)}
                    bottomSheet.setOnCompanyClickListener(this)
                }

                disignation.setOnClickListener {
                    val bottomSheet = BottomSheetJobTitle()
                    val fragManager = supportFragmentManager
                    fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
                    bottomSheet.setOnJobTitleClickListener(this)
                }

                dialog.findViewById<CardView>(R.id.cardSave).setOnClickListener {



                }
//            val back =  dialog.findViewById<>()
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
        })

    }

    override fun bottomEditClick(CTCFrBo: String) {

    }

    override fun bottomEditEdClick(EdFrBo: String) {

    }
}