package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.CandidateDataClass
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandidateDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit  var username:TextView
    private lateinit var type:TextView
    private lateinit var intro:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)

        val name = findViewById<TextView>(R.id.name_Candidate)
        username = findViewById<TextView>(R.id.userName_Candidate)
        val role = findViewById<TextView>(R.id.candidate_role)
        val location = findViewById<TextView>(R.id.location_candidate)
        type = findViewById<TextView>(R.id.candidate_jobType)
        intro = findViewById<TextView>(R.id.candidate_intro)
        val profile = findViewById<ShapeableImageView>(R.id.candidate_profile)

        name.text = intent.getStringExtra("name")
        role.text = intent.getStringExtra("role")
        location.text = intent.getStringExtra("city")

        val image = intent.getStringExtra("profile")

        Glide.with(applicationContext).load(image).into(profile)

        val id = intent.getStringExtra("applicationId")

        getCandidate(id)

    }

    private fun getCandidate(id: String?) {

        val getCandidate = RetrofitBuilder.jobsApis.getCandidate(id!!)

        getCandidate.enqueue(object : Callback<List<CandidateDataClass>?> {
            override fun onResponse(
                call: Call<List<CandidateDataClass>?>,
                response: Response<List<CandidateDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    username.text = response.get(0).User_name
                    type.text = response.get(0).jobType.get(0)
                    intro.text = response.get(0).self_introduction
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CandidateDataClass>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }
}