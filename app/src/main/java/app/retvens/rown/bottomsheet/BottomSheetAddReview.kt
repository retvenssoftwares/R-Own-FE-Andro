package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.profile.vendorsReview.GetQuickReviewsAdapter
import app.retvens.rown.NavigationFragments.profile.vendorsReview.GetQuickReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.UploadReviewsIdData
import app.retvens.rown.R
import app.retvens.rown.viewAll.vendorsDetails.ReviewData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetAddReview(val title: String, val name: String, val userId : String) : BottomSheetDialogFragment() {

    var mListener: OnBottomSheetAddReviewClickListener ? = null
    fun setOnReviewClickListener(listener: OnBottomSheetAddReviewClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetAddReview? {
        return BottomSheetAddReview("", "","")
    }
    interface OnBottomSheetAddReviewClickListener{
        fun bottomReviewClick(reviewFrBo : String)
    }

    companion object {
        const val Review_TAG = "BottomSheetDailog"
    }


    lateinit var recyclerView : RecyclerView
    lateinit var getQuickReviewsAdapter: GetQuickReviewsAdapter

    var addedReviews : MutableList<UploadReviewsIdData> ? = mutableListOf()

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var titleT : TextView

    lateinit var tellUs : EditText
    lateinit var ratingBar : RatingBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tellUs = view.findViewById(R.id.tellUs)

        ratingBar = view.findViewById(R.id.ratingBar)

        titleT = view.findViewById(R.id.title)
        titleT.text = "$title $name?"

        view.findViewById<CardView>(R.id.addReview).setOnClickListener {
            if (title == "How was your experience with") {
                uploadReview(tellUs.text.toString(), ratingBar.rating.toString())
            } else {
                uploadHotelReview(tellUs.text.toString(), ratingBar.rating.toString())
            }
        }

        recyclerView = view.findViewById(R.id.recycler_review)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),3)
        recyclerView.setHasFixedSize(true)
        getQuickReview()

    }
    private fun getQuickReview() {
        val qR = RetrofitBuilder.exploreApis.getQuickReviews()
        qR.enqueue(object : Callback<List<GetQuickReviewsData>?>, GetQuickReviewsAdapter.onItemClickListener {
            override fun onResponse(
                call: Call<List<GetQuickReviewsData>?>,
                response: Response<List<GetQuickReviewsData>?>
            ) {
                if (response.isSuccessful){
                    getQuickReviewsAdapter = GetQuickReviewsAdapter(requireContext(),response.body()!!)
                    recyclerView.adapter = getQuickReviewsAdapter
                    getQuickReviewsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<GetQuickReviewsData>?>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onItemClickInterest(addedItems: MutableList<GetQuickReviewsData>) {
                addedItems.forEach {
                    addedReviews?.add(UploadReviewsIdData(it.reviews_id))
                }
            }
        })
    }

    private fun uploadHotelReview(tellUs: String, rating: String) {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val addR = RetrofitBuilder.exploreApis.addHotelReview(userId, ReviewData(user_id, addedReviews, rating, tellUs))
        addR.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d("reviewPost", response.toString())
                dismiss()
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadReview(tellUs: String, rating: String) {
        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val addR = RetrofitBuilder.viewAllApi.addVendorReview(userId, ReviewData(user_id, addedReviews, rating, tellUs))
        addR.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d("reviewPost", response.toString())
                dismiss()
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}