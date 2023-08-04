package app.retvens.rown.NavigationFragments.exploreForUsers.ExploreOpenCommunity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.R
import app.retvens.rown.viewAll.viewAllCommunities.ViewAllCommunityAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreOpenCommunity : Fragment() {

    lateinit var viewAllCommunityAdapter: ViewAllCommunityAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar:EditText
    private lateinit var noCommunities:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_open_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar = view.findViewById(R.id.search_community)

        recyclerView = view.findViewById(R.id.viewAllCommRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        noCommunities = view.findViewById(R.id.noCommunities)

        fetchCommunity()
    }

    private fun fetchCommunity() {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val fetchCommunity = RetrofitBuilder.feedsApi.fetchOpenCommunity(user_id)

        fetchCommunity.enqueue(object : Callback<List<GetCommunitiesData>?> {
            override fun onResponse(
                call: Call<List<GetCommunitiesData>?>,
                response: Response<List<GetCommunitiesData>?>
            ) {
                if (response.isSuccessful) {
                    try {
                        val response = response.body()!!
                        if (response.isNotEmpty()){
                            viewAllCommunityAdapter = ViewAllCommunityAdapter(
                                response as ArrayList<GetCommunitiesData>,
                                requireContext()
                            )
                            recyclerView.adapter = viewAllCommunityAdapter
                            viewAllCommunityAdapter.notifyDataSetChanged()


                            searchBar.addTextChangedListener(object : TextWatcher {
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
                                    val original = response.toList()
                                    val filter = original.filter { searchUser ->
                                        searchUser.group_name.contains(s.toString(), ignoreCase = true)
                                    }
                                    viewAllCommunityAdapter.searchView(filter as ArrayList<GetCommunitiesData>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })
                        }else{
                            noCommunities.visibility = View.VISIBLE
                        }

                    }catch (e:NullPointerException){
                        noCommunities.visibility = View.VISIBLE
                    }
                }else{
                    noCommunities.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<GetCommunitiesData>?>, t: Throwable) {
                noCommunities.visibility = View.VISIBLE
            }
        })
    }
}