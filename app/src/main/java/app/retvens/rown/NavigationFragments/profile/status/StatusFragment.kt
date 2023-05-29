package app.retvens.rown.NavigationFragments.profile.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsAdapter
import app.retvens.rown.NavigationFragments.profile.polls.PollsData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusFragment(val userId: String) : Fragment() {

    lateinit var statusRecycler : RecyclerView
    lateinit var statusAdapter: StatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusRecycler = view.findViewById(R.id.statusRecycler)

        statusRecycler.layoutManager = LinearLayoutManager(context)
        statusRecycler.setHasFixedSize(true)




        getMedia(userId)
    }

    private fun getMedia(userId: String) {

        val getMedia = RetrofitBuilder.feedsApi.getNormalUserStatus(userId, "1")

        getMedia.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()!!

                    response.forEach { postsDataClass ->

                        try {
                            statusAdapter = StatusAdapter(postsDataClass.posts, requireContext())
                            statusRecycler.adapter = statusAdapter
                            statusAdapter.notifyDataSetChanged()
                        }catch (e:NullPointerException){

                        }



                    }

                } else {
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT)
                        .show()

                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}