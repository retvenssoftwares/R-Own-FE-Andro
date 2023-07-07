package app.retvens.rown.NavigationFragments.exploreForUsers.people

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ConnectionCollection.ConnectionDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplorePeopleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var explorePeopleAdapter: ExplorePeopleAdapter
    private lateinit var searchBar:EditText
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var currentPage = 1
    private var totalPages = 0
    private var isLoading = false
    lateinit var empty : TextView
    lateinit var errorImage : ImageView
    private lateinit var progress: ProgressBar
    private var peopleList:ArrayList<Post> = ArrayList()

    private var lastPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_people, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.explore_peoples_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        searchBar = view.findViewById(R.id.search_explore_peoples)

        explorePeopleAdapter = ExplorePeopleAdapter(requireContext(), peopleList)
        recyclerView.adapter = explorePeopleAdapter

        empty = view.findViewById(R.id.empty)
        errorImage = view.findViewById(R.id.errorImage)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_tasks_view_container)
        progress = view.findViewById(R.id.progress)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val currentItem = layoutManager.childCount
                    val totalItem = layoutManager.itemCount
                    val  scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (isLoading && (lastVisibleItemPosition == totalItem-1)){
                        if (currentPage > lastPage) {
                            isLoading = false
                            lastPage++
                            getData()
                        }

                    }
                }


            }
        })

        getAllProfiles()

    }

    private fun getData() {
        val handler = Handler()

        progress.setVisibility(View.VISIBLE);

        handler.postDelayed({
            getAllProfiles()
            progress.setVisibility(View.GONE);
        },
            3000)
    }

    private fun getAllProfiles() {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getProfiles = RetrofitBuilder.exploreApis.getPeople(user_id,currentPage.toString())

//        isLoading = true

        getProfiles.enqueue(object : Callback<List<ExplorePeopleDataClass>?>,
            ExplorePeopleAdapter.ConnectClickListener {
            override fun onResponse(
                call: Call<List<ExplorePeopleDataClass>?>,
                response: Response<List<ExplorePeopleDataClass>?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE

                        isLoading = false

                        if (response.body()!!.isNotEmpty()) {
                            val response = response.body()!!

                            response.forEach { explorePeopleDataClass ->
                                try {
                                    if (explorePeopleDataClass.posts.size >= 10){
                                        currentPage++
                                    }
                                    peopleList.addAll(explorePeopleDataClass.posts)
                                    explorePeopleAdapter.removeUser(explorePeopleDataClass.posts)
                                    explorePeopleAdapter.removeUsersFromList(explorePeopleDataClass.posts)
                                    explorePeopleAdapter.notifyDataSetChanged()
                                }catch (e:NullPointerException){
//                                    errorImage.visibility = View.VISIBLE
                                    Log.e("error",e.message.toString())
                                }


                            }

                            explorePeopleAdapter.setJobSavedClickListener(this)
                            explorePeopleAdapter.cancelConnRequest(this)

                            searchBar.addTextChangedListener(object :TextWatcher{
                                override fun beforeTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {
                                    val text = p0.toString()
                                    searchPeople(text)
                                }

                                override fun afterTextChanged(p0: Editable?) {

                                }
                            })

                } else {
                            empty.text = "You did'nt post yet"
                            errorImage.visibility = View.VISIBLE
                        }
                    } else {
                        errorImage.visibility = View.VISIBLE
                        empty.text = response.code().toString()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<List<ExplorePeopleDataClass>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again"
                errorImage.visibility = View.VISIBLE

                isLoading = false
            }



            override fun onJobSavedClick(connect: Post) {
                sendConnectionRequest(connect.User_id)
            }

            override fun onCancelRequest(connect: Post) {
                removeConnRequest(connect.User_id)
            }
        })





    }

    private fun searchPeople(text: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val searchPeople = RetrofitBuilder.exploreApis.searchPeople(text,user_id,"1")

        searchPeople.enqueue(object : Callback<List<ExplorePeopleDataClass>?> {
            override fun onResponse(
                call: Call<List<ExplorePeopleDataClass>?>,
                response: Response<List<ExplorePeopleDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!

                    val searchList:ArrayList<Post> = ArrayList()
                    response.forEach {
                        try {
                            searchList.addAll(it.posts)
                            explorePeopleAdapter.removeUser(it.posts)
                            explorePeopleAdapter.removeUsersFromList(it.posts)
                            explorePeopleAdapter.notifyDataSetChanged()

                            explorePeopleAdapter = ExplorePeopleAdapter(requireContext(), searchList)
                            recyclerView.adapter = explorePeopleAdapter
                        }catch (e:NullPointerException){
                            Log.e("error",e.message.toString())
                        }
                    }
                }else{
                    Log.e("error",response.code().toString())
                    getAllProfiles()
                }

            }
            override fun onFailure(call: Call<List<ExplorePeopleDataClass>?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun removeConnRequest(userId: String) {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val removeRequest = RetrofitBuilder.connectionApi.removeRequest(userId,
            ConnectionDataClass(user_id)
        )

        removeRequest.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }

    private fun sendConnectionRequest(userId: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val sendRequest = RetrofitBuilder.connectionApi.sendRequest(userId, ConnectionDataClass(user_id))

        sendRequest.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }


}