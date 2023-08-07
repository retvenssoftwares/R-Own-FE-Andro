package app.retvens.rown.viewAll.communityDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.NavigationFragments.home.Community
import app.retvens.rown.NavigationFragments.home.CommunityListAdapter
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityViewAllCommmunitiesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllCommmunitiesActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewAllCommmunitiesBinding

    lateinit var communityArrayList : ArrayList<Community>
    private lateinit var cummunity:Community
    lateinit var viewAllCommunitiesAdapter: ViewAllCommunitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllCommmunitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityBackBtn.setOnClickListener { onBackPressed() }

        binding.recyclerViewAllCommunity.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewAllCommunity.setHasFixedSize(true)
        communityArrayList = arrayListOf<Community>()

        cummunity = Community("","","","","")
        getCommunities()
    }
    private fun getCommunities() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()
        val getCommunity = RetrofitBuilder.ProfileApis.getCommunities(user_id)

        getCommunity.enqueue(object : Callback<List<GetCommunitiesData>?> {
            override fun onResponse(
                call: Call<List<GetCommunitiesData>?>,
                response: Response<List<GetCommunitiesData>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach{ it ->
                        val original = response.toList()
                        viewAllCommunitiesAdapter = ViewAllCommunitiesAdapter(this@ViewAllCommmunitiesActivity,response)
                        binding.recyclerViewAllCommunity.adapter = viewAllCommunitiesAdapter

                        binding.searchCommunity.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                val filter = original.filter { searchUser ->
                                    searchUser.group_name.contains(s.toString(),ignoreCase = false)
                                }
                                viewAllCommunitiesAdapter.searchCom(filter)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })

                    }
                }else{
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            override fun onFailure(call: Call<List<GetCommunitiesData>?>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
            }
        })
    }

}