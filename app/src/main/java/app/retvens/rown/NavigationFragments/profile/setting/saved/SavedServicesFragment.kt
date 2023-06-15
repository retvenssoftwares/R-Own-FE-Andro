package app.retvens.rown.NavigationFragments.profile.setting.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesAdapter
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServicesData
import app.retvens.rown.R
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SavedServicesFragment : Fragment() {

    private lateinit var savedServicesRecyclerView: RecyclerView
    lateinit var savedServicesAdapter: SavedServicesAdapter

    lateinit var exploreServicesAdapter: ExploreServicesAdapter

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var empty : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedServicesRecyclerView = view.findViewById(R.id.savedServicesRecyclerView)
        savedServicesRecyclerView.layoutManager = GridLayoutManager(context,2)
        savedServicesRecyclerView.setHasFixedSize(true)

        val blogs = listOf<ExploreServicesData>(
            ExploreServicesData("Paradise Inn"),
            ExploreServicesData("Paradise Inn 2"),
            ExploreServicesData("Neck Deep Paradise Inn"),
            ExploreServicesData("Paradise Inn 23"),
            ExploreServicesData("Paradise Inn"),
            ExploreServicesData("Paradise Inn 2"),
            ExploreServicesData("Neck Deep Paradise Inn"),
            ExploreServicesData("Paradise Inn 23"),
            ExploreServicesData("Paradise Inn"),
            ExploreServicesData("Paradise Inn 2"),
            ExploreServicesData("Neck Deep Paradise Inn"),
            ExploreServicesData("Paradise Inn 23"),
        )

//        savedServicesAdapter = SavedServicesAdapter(blogs, requireContext())
//        savedServicesRecyclerView.adapter = savedServicesAdapter
//        savedServicesAdapter.notifyDataSetChanged()

        empty = view.findViewById(R.id.empty)

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        getServices()
    }

    private fun getServices() {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val serv = RetrofitBuilder.ProfileApis.getSaveService(user_id,"1")
        serv.enqueue(object : Callback<List<ExploreServiceData>?> {
            override fun onResponse(
                call: Call<List<ExploreServiceData>?>,
                response: Response<List<ExploreServiceData>?>
            ) {
                    if (isAdded){
                        if (response.isSuccessful){
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                            if (response.body()!!.isNotEmpty()) {
                                try {
                                val data = response.body()!!
                                data.forEach {
                                    exploreServicesAdapter = ExploreServicesAdapter(it.events, requireContext())
                                    savedServicesRecyclerView.adapter = exploreServicesAdapter
                                    exploreServicesAdapter.notifyDataSetChanged()

                                }
                                }catch (e:Exception){
                                Toast.makeText(requireContext(),"No Services Yet", Toast.LENGTH_SHORT).show()
                            }
                            } else {
                                empty.visibility = View.VISIBLE
                            }
                        } else {
                            empty.visibility = View.VISIBLE
                            empty.text = response.code().toString()
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }
                    }


            }

            override fun onFailure(call: Call<List<ExploreServiceData>?>, t: Throwable) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                empty.text = "Try Again - ${t.localizedMessage}"
                empty.visibility = View.VISIBLE
            }
        })
    }

}