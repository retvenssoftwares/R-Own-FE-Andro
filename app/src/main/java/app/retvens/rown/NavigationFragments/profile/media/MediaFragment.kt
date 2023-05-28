package app.retvens.rown.NavigationFragments.profile.media

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MediaFragment(val userId: String) : Fragment() {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var mediaAdapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaRecyclerView = view.findViewById(R.id.mediaRecyclerView)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)
        mediaRecyclerView.setHasFixedSize(true)


        getMedia(userId)

    }

    private fun getMedia(userId: String) {

        val getMedia = RetrofitBuilder.feedsApi.getUserProfileMedia(userId,"1")

        getMedia.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!


                    response.forEach { postsDataClass ->

                        mediaAdapter = MediaAdapter(requireContext(),postsDataClass.posts)
                        mediaRecyclerView.adapter = mediaAdapter
                        mediaAdapter.notifyDataSetChanged()

                    }

                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString() ,Toast.LENGTH_SHORT).show()
            }
        })



    }
}