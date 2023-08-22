package app.retvens.rown.NavigationFragments.home.postDetails

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.bottomsheet.BottomSheetPostEdit
import app.retvens.rown.bottomsheet.BottomSheetReportPost
import app.retvens.rown.databinding.ActivityCheckInDetailsBinding
import app.retvens.rown.utils.postLike
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckInDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityCheckInDetailsBinding

    var postId = ""
    var user_id = ""

    private  var likeCount = 0
    private  var like = ""
    private  var captionString = ""
    private  var location = ""
    private  var profilePic = ""

    private var isLike = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener {
            startActivity(Intent(this, DashBoardActivity::class.java))
        }

        postId = intent.getStringExtra("postId").toString()
        getPost(postId)

        binding.likePost.setOnClickListener {
            if (isLike) {
                isLike = false
                binding.likePost.setImageResource(R.drawable.liked_vectore)
                likeCount += 1
//                    post.Like_count = count.toString()
                binding.likeCount.text = likeCount.toString()
                binding.likeCount.visibility = View.VISIBLE
                postLike(postId, applicationContext) {

                }
            } else {
                isLike = true
                binding.likePost.setImageResource(R.drawable.svg_like_post)
                likeCount -= 1
//                    post.Like_count = count.toString()
                binding.likeCount.text = likeCount.toString()
                if (likeCount == 0){
                    binding.likeCount.visibility = View.GONE
                }
                postLike(postId, applicationContext) {

                }
            }
        }

        binding.actionButton.setOnClickListener {
            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val User_id = sharedPreferences?.getString("user_id", "").toString()

            if (User_id == user_id){
                val bottomSheet = BottomSheetPostEdit(postId,captionString,location)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetPostEdit.Hotelier_TAG)}
            }else{
                val bottomSheet = BottomSheetReportPost(user_id,postId)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetReportPost.RATING_TAG)}
            }
        }

        binding.comment.setOnClickListener {
            val bottomSheet = BottomSheetComment(postId,profilePic!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
        }

    }

    private fun getPost(postId: String) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "").toString()

        val getpost = RetrofitBuilder.feedsApi.getPostData(userId ,postId)

        getpost.enqueue(object : Callback<List<PostItem>?> {
            override fun onResponse(
                call: Call<List<PostItem>?>,
                response: Response<List<PostItem>?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    response.forEach { response->

                        Log.e("res",response.toString())

                        captionString = response.caption
                        user_id = response.user_id
                        profilePic = response.Profile_pic

                        binding.titleStatus.text = "Hello all, I am here at ${response.hotelName}. Let’s Catch Up."

                        if (captionString.length > 150) {
                            binding.recentCommentByUser.text = Html.fromHtml(captionString.substring(0, 150) + "..." + "<font color='black'> <b>Read More</b></font>")
                        } else {
                            binding.recentCommentByUser.text = captionString
                        }
                        binding.recentCommentByUser.setOnClickListener {
                            if (binding.recentCommentByUser.text.toString().endsWith("Read More")) {
                                binding.recentCommentByUser.text = captionString
                            } else {
                                if (captionString.length > 150) {
                                    binding.recentCommentByUser.text = Html.fromHtml(captionString.substring(0, 150) + "..." + "<font color='black'> <b>Read More</b></font>")
                                } else {
                                    binding.recentCommentByUser.text = captionString
                                }
                            }
                        }

                        if (response.User_name.isNotEmpty()) {
                            binding.userIdOnComment.text = response.User_name
                        } else {
                            binding.userIdOnComment.text = response.Full_name
                        }

                        if (response.User_name.isEmpty()) {
                            binding.userNamePost.text = response.Full_name
                        } else {
                            binding.userNamePost.text = response.User_name
                        }
                        if (response.Profile_pic.isNotEmpty()){
                            Glide.with(applicationContext).load(response.Profile_pic).into(binding.postProfile)
                        } else {
                            binding.postProfile.setImageResource(R.drawable.svg_user)
                        }

                        Glide.with(applicationContext).load(response.hotelCoverpicUrl).into(binding.eventImage)

                        if (response.verificationStatus == "true"){
                            binding.verification.visibility = View.VISIBLE
                        }
                        binding.postTime.text = TimesStamp.convertTimeToText(response.date_added)
                        location = response.location
                        binding.postUserType.text = response.hotelName
                        binding.postUserDominican.text = response.hotelAddress
//                        role = response.Role

                        binding.eventImage.setOnClickListener {
                            val intent = Intent(applicationContext, HotelDetailsActivity::class.java)
                            intent.putExtra("name", response.hotelName)
                            intent.putExtra("logo", response.hotelCoverpicUrl)
                            intent.putExtra("hotelId", response.hotel_id)
                            intent.putExtra("hotelAddress", response.hotelAddress)
                            startActivity(intent)
                        }

                        binding.postUserType.setOnClickListener {
                            val intent = Intent(applicationContext, HotelDetailsActivity::class.java)
                            intent.putExtra("name", response.hotelName)
                            intent.putExtra("logo", response.hotelCoverpicUrl)
                            intent.putExtra("hotelId", response.hotel_id)
                            intent.putExtra("hotelAddress", response.hotelAddress)
                            startActivity(intent)
                        }

                        binding.book.setOnClickListener {
                            val uri: Uri = Uri.parse("https://${response.bookingengineLink}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }

                        like = response.liked
                        if (like == "Liked" || like == "liked"){
                            isLike = false
//            Toast.makeText(applicationContext, "$isLike", Toast.LENGTH_SHORT).show()
                            binding.likePost.setImageResource(R.drawable.liked_vectore)
                        }else if (like == "Unliked" || like == "not liked"){
                            isLike = true
//            Toast.makeText(applicationContext, "$isLike", Toast.LENGTH_SHORT).show()
                            binding.likePost.setImageResource(R.drawable.svg_like_post)
                        }

                        likeCount = response.likeCount.toInt()
                        val commentCount = response.commentCount

                        binding.likeCount.text = likeCount.toString()
                        binding.commentCount.text = commentCount

                        if (commentCount == "0"){
                            binding.commentCount.visibility = View.GONE
                        }

                        if (likeCount == 0){
                            binding.likeCount.visibility = View.GONE
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<PostItem>?>, t: Throwable) {

            }
        })
    }

}