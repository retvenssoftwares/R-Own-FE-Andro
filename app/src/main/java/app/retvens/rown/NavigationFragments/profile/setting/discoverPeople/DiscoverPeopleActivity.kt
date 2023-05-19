package app.retvens.rown.NavigationFragments.profile.setting.discoverPeople

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.UsersProfileAdapter
import app.retvens.rown.databinding.ActivityDiscoverPeopleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverPeopleActivity : AppCompatActivity() {
    lateinit var binding : ActivityDiscoverPeopleBinding

    private lateinit var discoverAdapter: DiscoverAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.discoverRecycler.layoutManager = LinearLayoutManager(this)
        binding.discoverRecycler.setHasFixedSize(true)
        getProfiles()
    }

    private fun getProfiles(){
        val pro = RetrofitBuilder.retrofitBuilder.getProfile()
        pro.enqueue(object : Callback<List<UserProfileRequestItem>?> {
            override fun onResponse(
                call: Call<List<UserProfileRequestItem>?>,
                response: Response<List<UserProfileRequestItem>?>
            ) {
                Toast.makeText(applicationContext,response.toString(), Toast.LENGTH_SHORT).show()
                Log.d("Profile",response.toString())
                Log.d("Profile",response.body().toString())

                if(response.isSuccessful){
                    val data = response.body()!!
                    discoverAdapter = DiscoverAdapter(data, applicationContext )
                    discoverAdapter.notifyDataSetChanged()
                    binding.discoverRecycler.adapter = discoverAdapter

                }
            }
            override fun onFailure(call: Call<List<UserProfileRequestItem>?>, t: Throwable) {
                Toast.makeText(applicationContext,t.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
                Log.d("Profile",t.localizedMessage?.toString(),t)
            }
        })
    }

}