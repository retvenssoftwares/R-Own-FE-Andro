package app.retvens.rown.NavigationFragments.exploreForUsers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.media.MediaAdapter
import app.retvens.rown.NavigationFragments.profile.media.MediaData
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplorePostsFragment : Fragment() {

    lateinit var mediaRecyclerView: RecyclerView
    lateinit var mediaAdapter: MediaAdapter
    lateinit var searchBar:EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaRecyclerView = view.findViewById(R.id.explore_posts_recycler)
        mediaRecyclerView.layoutManager = GridLayoutManager(context,3)
        mediaRecyclerView.setHasFixedSize(true)

        searchBar = view.findViewById(R.id.search_explore_posts)


        getExplorePost()
    }

    private fun getExplorePost() {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val getExplorePost = RetrofitBuilder.exploreApis.getExplorePost(user_id,"1")

        getExplorePost.enqueue(object : Callback<List<PostsDataClass>?> {
            override fun onResponse(
                call: Call<List<PostsDataClass>?>,
                response: Response<List<PostsDataClass>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    val originalData = response.toList()
                    response.forEach { postsDataClass ->
                        mediaAdapter = MediaAdapter(requireContext(),postsDataClass.posts)
                        mediaRecyclerView.adapter = mediaAdapter
                        mediaAdapter.notifyDataSetChanged()


                        searchBar.addTextChangedListener(object : TextWatcher{
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

                                val letter = p0.toString()

                                searchPost(letter)

                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }

                        })

                    }


                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PostsDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun searchPost(letter: String) {

        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val searchPost = RetrofitBuilder.exploreApis.searchPost(letter,user_id,"1")

        searchPost.enqueue(object : Callback<List<PostsDataClass>?> {
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
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }


}