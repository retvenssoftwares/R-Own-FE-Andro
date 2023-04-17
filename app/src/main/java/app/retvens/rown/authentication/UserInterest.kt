package app.retvens.rown.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.onboarding.GetInterests
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUserInterestBinding
import app.retvens.rown.utils.moveTo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInterest : AppCompatActivity() {
    lateinit var binding: ActivityUserInterestBinding

    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("user").toString()
        binding.userName.text = "Hello, $username!"

        binding.interestGrid.layoutManager = GridLayoutManager(this,3)
        binding.interestGrid.setHasFixedSize(true)

        binding.cardContinueInterest.setOnClickListener {
            moveTo(this,"MoveToUC")
            val intent = Intent(applicationContext, UserContacts::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("name",username)
            startActivity(intent)
        }
        getInterests()
    }

    private fun getInterests() {
        val retroInterest = RetrofitBuilder.retrofitBuilder.getInterests()
        retroInterest.enqueue(object : Callback<GetInterests?> {
            override fun onResponse(call: Call<GetInterests?>, response: Response<GetInterests?>) {
                Log.d("Interest",response.body().toString())
                Log.d("Interest",response.message().toString())
                Log.d("Interest",response.code().toString())
                Log.d("Interest",response.isSuccessful.toString())
            }
            override fun onFailure(call: Call<GetInterests?>, t: Throwable) {
                Log.d("Interest",t.localizedMessage,t)
            }
        })
    }
}