package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.net.Uri.encode
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SavePost
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import app.retvens.rown.databinding.EachItemBinding
import app.retvens.rown.databinding.ItemEventPostBinding
import app.retvens.rown.databinding.ItemPollProfileBinding
import app.retvens.rown.databinding.ItemStatusBinding
import app.retvens.rown.databinding.UsersPostsCardBinding
import app.retvens.rown.viewAll.AllHotelsActivity
import app.retvens.rown.viewAll.vendorsDetails.ViewAllVendorsActivity
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllBlogsActivity
import app.retvens.rown.viewAll.viewAllCommunities.ViewAllAvailableCommunitiesActivity
import com.bumptech.glide.Glide
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import me.relex.circleindicator.CircleIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.databinding.ItemHotelPostBinding
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import okio.ByteString
import okio.ByteString.Companion.encodeString
import java.nio.charset.Charset
import kotlin.collections.ArrayList


class MainAdapter(val context: Context, private val dataItemList: ArrayList<DataItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    lateinit var viewPagerAdapter: ImageSlideAdapter
    lateinit var indicator: CircleIndicator

    interface OnItemClickListener {
        fun onItemClick(dataItem: PostItem)
        fun onItemClickForComment(banner: PostItem,position: Int)
        fun onItemsharePost(share:String)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    inner class BannerItemViewHolder(private val binding : UsersPostsCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){
            var save = true
            var operatioin = "push"

                        val post = banner

            if (banner.Profile_pic.isNotEmpty()) {
                Glide.with(context).load(banner.Profile_pic).into(binding.postProfile)
            }else {
                binding.postProfile.setImageResource(R.drawable.svg_user)
            }

            if (post.User_name.isNotEmpty()){
                binding.userIdOnComment.text = post.User_name
            } else{
                binding.userIdOnComment.text = post.Full_name
            }
                        Log.e("username",post.toString())
                        binding.recentCommentByUser.text = post.caption
                        Log.e("caption",post.caption)
                        binding.userNamePost.text = post.Full_name

                        binding.location.text = post.location

            if(post.verificationStatus != "false"){
                binding.verification.visibility = View.VISIBLE
            }

            if (post.isSaved == "saved"){
                operatioin = "pop"
                save = false
                binding.savePost.setImageResource(R.drawable.svg_saved)
            } else {
                operatioin = "push"
                save = true
                binding.savePost.setImageResource(R.drawable.svg_save_post)
            }
           binding.savePost.setOnClickListener {
               if (post.post_id != null) {
                   savePosts(post.post_id, binding, operatioin, save) {
                       if (it == 0) {
                           operatioin = "pop"
                           save = !save
                           post.isSaved = "saved"
                       } else {
                           operatioin = "push"
                           save = !save
                           post.isSaved = "not saved"
                       }
                   }
               }
            }

                        if (post.Like_count != ""){
                            binding.likeCount.text = post.Like_count
                        }
                        if (post.Comment_count != ""){
                            binding.commentCount.text = post.Comment_count
                        }

                        post.media.forEach {  item ->
                            // Set up the ImageSlideAdapter and ViewPager
                            viewPagerAdapter = ImageSlideAdapter(context, post.media, banner, binding)
                            binding.viewPager.adapter = viewPagerAdapter


                            if (post.media.size >1) {
                                indicator = binding.indicator as CircleIndicator
                                indicator.setViewPager(binding.viewPager)
                            } else{
                                binding.indicator.visibility = View.GONE
                            }
                        }

//

                        if (post.like == "Liked"){
                            binding.likePost.setImageResource(R.drawable.liked_vectore)
                        }else if (post.like == "Unliked"){
                            binding.likePost.setImageResource(R.drawable.svg_like_post)
                        }

            val messageType = "Post"
            val userId = post.user_id
            val postId = post.post_id
            val profilePictureLink = post.Profile_pic
            val firstImageLink = post.media.get(0).post
            val username = post.User_name
            val caption = post.caption
            val fullName = post.Full_name
            val verificationStatus = post.verificationStatus
            val encodedData = encodeData(messageType, userId, postId, profilePictureLink, firstImageLink, username, caption,verificationStatus,fullName)

            binding.sharePost.setOnClickListener {
                onItemClickListener?.onItemsharePost(encodedData)
            }


                Log.e("date",banner.date_added)
                val timestamp = convertTimeToText(banner.date_added)

                binding.postTime.text = timestamp


            binding.postProfile.setOnClickListener {

//                if (banner.Role == "Normal User" || banner.Role == "Hospitality Expert"){
//
//                }else
                if(banner.Role == "Business Vendor / Freelancer"){
                    val intent = Intent(context,VendorProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                }else if (banner.Role == "Hotel Owner"){
                    val intent = Intent(context,OwnerProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context,UserProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                }


            }

            binding.postCard.setOnClickListener {

                Log.e("media",post.media.toString())

                val intent = Intent(context,PostDetailsActivity::class.java)
                intent.putExtra("profilePic",banner.Profile_pic)
                intent.putExtra("profileName",banner.Full_name)
                intent.putExtra("userName",banner.User_name)
                intent.putExtra("caption",banner.caption)
                val images:ArrayList<String> = ArrayList()
                post.media.forEach { item ->
                    images.add(item.post)
                    intent.putExtra("time",item.date_added)
                }
                intent.putStringArrayListExtra("postPic",images)
                intent.putExtra("likeCount",banner.Like_count)
                intent.putExtra("commentCount",banner.Comment_count)
                intent.putExtra("like",banner.like)
                intent.putExtra("isSaved",banner.isSaved)
                intent.putExtra("postId",banner.post_id)
                context.startActivity(intent)
            }

            binding.likePost.setOnClickListener {

                if (post.like == "Unliked"){
                    banner.islike = !banner.islike

                    val count:Int
                    if(banner.islike){
                        post.like = "Liked"
                        binding.likePost.setImageResource(R.drawable.liked_vectore)
                        count = post.Like_count.toInt()+1
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                    }else{
                        post.like = "Unliked"
                        binding.likePost.setImageResource(R.drawable.svg_like_post)
                        count = post.Like_count.toInt()
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                    }

                    onItemClickListener?.onItemClick(banner)
                }
                if (post.like == "Liked"){
                    banner.islike = !banner.islike

                    val count:Int
                    if(banner.islike){
                        post.like = "Unliked"
                        binding.likePost.setImageResource(R.drawable.svg_like_post)
                        count = post.Like_count.toInt()-1
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                    }else{
                        post.like = "Liked"
                        binding.likePost.setImageResource(R.drawable.liked_vectore)
                        count = post.Like_count.toInt()
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                    }

                    onItemClickListener?.onItemClick(banner)
                }



            }

            binding.comment.setOnClickListener {
                onItemClickListener?.onItemClickForComment(banner,position)
            }

        }




    }

    inner class BannerItemViewHolderEvent(private val binding : ItemEventPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){

            var save = true
            var operatioin = "push"

            val post = banner

            if (banner.Profile_pic.isNotEmpty()) {
                Glide.with(context).load(banner.Profile_pic).into(binding.postProfile)
            }else {
                binding.postProfile.setImageResource(R.drawable.svg_user)
            }

            if (post.User_name.isNotEmpty()){
                binding.userIdOnComment.text = post.User_name
            } else{
                binding.userIdOnComment.text = post.Full_name
            }

            if(post.verificationStatus != "false"){
                binding.verification.visibility = View.VISIBLE
            }

            Log.e("username",post.User_name)
                binding.recentCommentByUser.text = post.caption
                Log.e("caption",post.caption)
                binding.userNamePost.text = post.Full_name

                binding.eventTitle.text = post.Event_name

                binding.titleStatus.text = "Hello all, I am going to ${post.Event_name} on ${post.event_start_date}"

                Glide.with(context).load(post.event_thumbnail).into(binding.eventImage)

                if (post.Like_count != ""){
                    binding.likeCount.text = post.Like_count
                }
                if (post.Comment_count != ""){
                    binding.commentCount.text = post.Comment_count
                }

            if (post.isSaved == "saved"){
                operatioin = "pop"
                save = false
                binding.savePost.setImageResource(R.drawable.svg_saved)
            } else {
                operatioin = "push"
                save = true
                binding.savePost.setImageResource(R.drawable.svg_save_post)
            }

            binding.savePost.setOnClickListener {
                if (post.post_id != null) {
                    saveEvent(post.post_id, binding, operatioin, save) {
                        if (it == 0) {
                            operatioin = "pop"
                            save = !save
                        } else {
                            operatioin = "push"
                            save = !save
                        }
                    }
                }
            }



            binding.postProfile.setOnClickListener {

                if(banner.Role == "Business Vendor/Freelancer"){
                    val intent = Intent(context,VendorProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                }else if (banner.Role == "Hotel Owner"){
                    val intent = Intent(context,OwnerProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context,UserProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)

                }
            }

            binding.eventImage

            binding.likePost.setOnClickListener {
                onItemClickListener?.onItemClick(banner)
            }

            binding.comment.setOnClickListener {
                onItemClickListener?.onItemClickForComment(banner,position)
            }

        }
    }


    inner class BannerItemViewHolderCheck(private val binding : ItemHotelPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){

            val post = banner

            var save = true
            var like = true
            var operatioin = "push"

            if (post.Profile_pic.isNotEmpty()) {
                Glide.with(context).load(post.Profile_pic).into(binding.postProfile)
            }else {
                binding.postProfile.setImageResource(R.drawable.svg_user)
            }

            if (post.User_name.isNotEmpty()){
                binding.userIdOnComment.text = post.User_name
            } else{
                binding.userIdOnComment.text = post.Full_name
            }

            if(post.verificationStatus != "false"){
                binding.verification.visibility = View.VISIBLE
            }

            Log.e("username",post.User_name)
            binding.recentCommentByUser.text = post.caption
//            Log.e("caption",post.caption)
            binding.userNamePost.text = post.Full_name

            binding.titleStatus.text = "Hello all, I am here at ${post.Event_name}. Let’s Catch Up."

            Glide.with(context).load(post.checkinVenue).into(binding.eventImage)

            if (post.Like_count != ""){
                binding.likeCount.text = post.Like_count
            }
            if (post.Comment_count != ""){
                binding.commentCount.text = post.Comment_count
            }
            if (post.like != "Unliked"){
                like = false
                binding.likePost.setImageResource(R.drawable.liked_vectore)
            }else {
                like = true
                binding.likePost.setImageResource(R.drawable.svg_like_post)
            }

            if (post.isSaved == "saved"){
                operatioin = "pop"
                save = false
                binding.savePost.setImageResource(R.drawable.svg_saved)
            } else {
                operatioin = "push"
                save = true
                binding.savePost.setImageResource(R.drawable.svg_save_post)
            }

            binding.savePost.setOnClickListener {
                if (post.post_id != null) {
                    saveHotel(post.post_id, binding, operatioin, save) {
                        if (it == 0) {
                            operatioin = "pop"
                            save = !save
                        } else {
                            operatioin = "push"
                            save = !save
                        }
                    }
                }
            }

            val timestamp = convertTimeToText(post.date_added)

            binding.postTime.text = timestamp


            binding.postProfile.setOnClickListener {

                if(banner.Role == "Business Vendor/Freelancer"){
                    val intent = Intent(context,VendorProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                }else if (banner.Role == "Hotel Owner"){
                    val intent = Intent(context,OwnerProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context,UserProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)

                }
            }


            var count:Int = 0

            binding.likePost.setOnClickListener {

                    if(like){
                        post.like = "Liked"
                        like = false
                        binding.likePost.setImageResource(R.drawable.liked_vectore)
                        count = post.Like_count.toInt()+1
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                        onItemClickListener?.onItemClick(banner)
                    } else{
                        post.like = "Unliked"
                        like = true
                        binding.likePost.setImageResource(R.drawable.svg_like_post)
                        count = post.Like_count.toInt()
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                        onItemClickListener?.onItemClick(banner)
                    }
            }

            binding.eventImage.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View?) {

                }

                override fun onDoubleClick(view: View?) {
                    val anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
                    binding.likedAnimation.startAnimation(anim)
                    binding.likedAnimation.visibility = View.VISIBLE

                    if(like){
                        post.like = "Liked"
                        like = false
                        binding.likePost.setImageResource(R.drawable.liked_vectore)
                        count = post.Like_count.toInt()+1
                        post.Like_count = count.toString()
                        binding.likeCount.text = count.toString()
                        onItemClickListener?.onItemClick(banner)
                    }

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
                        binding.likedAnimation.startAnimation(anim)
                        binding.likedAnimation.visibility = View.GONE }, 500)
                }
            }))

            binding.likePost.setOnClickListener {
                onItemClickListener?.onItemClick(banner)
            }

            binding.comment.setOnClickListener {
                onItemClickListener?.onItemClickForComment(banner,position)
            }

        }
    }


    inner class BannerItemViewHolderStatus(private val binding : ItemStatusBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){

            var save = true
            var operatioin = "push"

            if (banner.User_name.isNotEmpty()){
                binding.userNamePost.text = banner.User_name
            } else{
                binding.userNamePost.text = banner.Full_name
            }

            binding.titleStatus.text = banner.caption
            if (banner.Profile_pic.isNotEmpty()) {
                Glide.with(context).load(banner.Profile_pic).into(binding.postProfile)
            }else {
                binding.postProfile.setImageResource(R.drawable.svg_user)
            }

            binding.location.text = banner.location

            if(banner.verificationStatus != "false"){
                binding.verification.visibility = View.VISIBLE
            }

            if (banner.isSaved == "saved"){
                operatioin = "pop"
                save = false
                binding.savePost.setImageResource(R.drawable.svg_saved)
            } else {
                operatioin = "push"
                save = true
                binding.savePost.setImageResource(R.drawable.svg_save_post)
            }

            binding.savePost.setOnClickListener {
                if (banner.post_id != null) {
                    saveStatus(banner.post_id, binding, operatioin, save) {
                        if (it == 0) {
                            operatioin = "pop"
                            save = !save
                        } else {
                            operatioin = "push"
                            save = !save
                        }
                    }
                }
            }

            binding.postProfile.setOnClickListener {

                if (banner.Role == "Normal User" || banner.Role == "Hospitality Expert"){

                    val intent = Intent(context,UserProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)

                }else if(banner.Role == "Business Vendor / Freelancer"){
                    val intent = Intent(context,VendorProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                }else if (banner.Role == "Hotel Owner"){
                    val intent = Intent(context,OwnerProfileActivity::class.java)
                    intent.putExtra("userId",banner.user_id)
                    context.startActivity(intent)
                }


            }

            if (banner.Like_count != ""){
                binding.likeCount.text = banner.Like_count
            }
            if (banner.Comment_count != ""){
                binding.commentCount.text = banner.Comment_count
            }

            if (banner.like == "Liked"){
                binding.likePost.setImageResource(R.drawable.liked_vectore)
            }else if (banner.like == "Unliked"){
                binding.likePost.setImageResource(R.drawable.svg_like_post)
            }


            binding.likePost.setOnClickListener {
                banner.islike = !banner.islike

                val count:Int
                if(banner.islike){
                    binding.likePost.setImageResource(R.drawable.liked_vectore)
                    count = banner.Like_count.toInt()+1
                    binding.likeCount.text = count.toString()
                }else{
                    binding.likePost.setImageResource(R.drawable.svg_like_post)
                    count = banner.Like_count.toInt()
                    binding.likeCount.text = count.toString()
                }

                onItemClickListener?.onItemClick(banner)
            }

            binding.comment.setOnClickListener {
                onItemClickListener?.onItemClickForComment(banner,position)
            }

        }
    }


    inner class BannerItemViewHolderPoll(private val binding : ItemPollProfileBinding) : RecyclerView.ViewHolder(binding.root),
        PollsAdapter.OnItemClickListener {
        fun bindBannerView(banner: PostItem, position: Int){

            binding.checkVotes.visibility = View.GONE


            if (banner.Profile_pic.isNotEmpty()) {
                Glide.with(context).load(banner.Profile_pic).into(binding.postProfile)
            }else {
                binding.postProfile.setImageResource(R.drawable.svg_user)
            }

            binding.userNamePost.text = banner.Full_name

            val date = TimesStamp.convertTimeToText(banner.date_added)
            binding.postTime.text = date

            if(banner.verificationStatus != "false"){
                binding.verification.visibility = View.VISIBLE
            }

            binding.postProfile.setOnClickListener {
                    if (banner.Role == "Normal User" || banner.Role == "Hospitality Expert"){

                        val intent = Intent(context,UserProfileActivity::class.java)
                        intent.putExtra("userId",banner.user_id)
                        context.startActivity(intent)

                    }else if(banner.Role == "Business Vendor/Freelancer"){
                        val intent = Intent(context,VendorProfileActivity::class.java)
                        intent.putExtra("userId",banner.user_id)
                        context.startActivity(intent)
                    }else if (banner.Role == "Hotel Owner"){
                        val intent = Intent(context,OwnerProfileActivity::class.java)
                        intent.putExtra("userId",banner.user_id)
                        context.startActivity(intent)
                    }
                }

            binding.votesOptionsrecycler.layoutManager = LinearLayoutManager(context)
            val adapter = PollsAdapter(context, banner.pollQuestion[0].Options,
                PollsDetails(banner.post_id,banner.voted)
            )
            binding.votesOptionsrecycler.adapter = adapter

            banner.pollQuestion.forEach { item ->
                binding.titlePoll.text = item.Question

                val timestamp = convertTimeToText(banner.date_added)
                binding.postTime.text = timestamp
            }

            binding.location.text = banner.Role

            adapter.setOnItemClickListener(this)
//            banner.pollQuestion.forEach { item ->
//                binding.titlePoll.text = item.Question
//                binding.option1.text = item.Options[0].Option
//                binding.option2.text = item.Options[1].Option
//
//                val progressBarOption1: RoundedProgressBar = binding.progressBar
//                val progressBarOption2: RoundedProgressBar = binding.progressBar2
//
//
//                val vote1: List<String> = item.Options[0].votes.map { it.user_id }
//                val vote2: List<String> = item.Options[1].votes.map { it.user_id }
//
//                val timestamp = convertTimeToText(item.date_added)
//                binding.postTime.text = timestamp
//
//
//
//                binding.voteOption1.setOnClickListener {
//
//                    if (banner.voted == "no") {
//                        voteOption(banner.post_id, item.Options[0].option_id)
//                        val vote = vote1.size+1
//                        binding.Option1Votes.text = "${vote} votes"
//                        val totalVotes = vote + vote2.size
//                        val completedTasks = totalVotes
//                        val completedPercentage = (completedTasks.toDouble() / totalVotes) * 100.0
//                        if (!completedPercentage.isNaN()) {
//                            progressBarOption1.setProgressPercentage(completedPercentage)
//                            binding.option1Percentage.text = "${completedPercentage}%"
//                        }
//                    }
//                }
//
//
//
//
//                binding.voteOption2.setOnClickListener {
//
//                    if (banner.voted == "no"){
//                        voteOption(banner.post_id,item.Options[1].option_id)
//

//                    }
//
//                }
//
//                    binding.Option1Votes.text = "${vote1.size} votes"
//                    binding.Option2Votes.text = "${vote2.size} votes"
//
//                    val totalVotes = vote1.size + vote2.size
//                    val completedTasks = vote1.size
//                val completedPercentage = (completedTasks.toDouble() / totalVotes) * 100.0
//                if (!completedPercentage.isNaN()) {
//                    progressBarOption1.setProgressPercentage(completedPercentage)
//                    binding.option1Percentage.text = "${completedPercentage}%"
//                }
//
//
//                val completedTasks2 = vote2.size
//                val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
//                if (!completedPercentage2.isNaN()) {
//                    progressBarOption2.setProgressPercentage(completedPercentage2)
//                    binding.option2Percentage.text = "${completedPercentage2}%"
//                }




//            }

        }

        private fun voteOption(postId: String, optionId: String) {

            val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            val postVote = RetrofitBuilder.feedsApi.votePost(postId,optionId, VoteCollection(user_id))

            postVote.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    if (response.isSuccessful){
                        val response = response.body()!!
                        Toast.makeText(context,response.message,Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })


        }

        override fun onItemClick(optionId: String, postId: String) {
            voteOption(postId,optionId)
        }


    }

    inner class RecyclerItemViewHolder(private val binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
        }
        fun bindCreateCommunityRecyclerView(recyclerItemList : List<DataItem.CreateCommunityRecyclerData>){
            val adapter = CreateCommunityChildAdapter(DataItemType.CREATE_COMMUNITY,
                recyclerItemList as ArrayList<DataItem.CreateCommunityRecyclerData>
            )
            adapter.removeCommunityFromList(recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.constRecycler.visibility = View.GONE
        }
        fun bindCommunityRecyclerView(recyclerItemList : List<DataItem.CommunityRecyclerData>){
            val adapter = CommunityChildAdapter(context, DataItemType.COMMUNITY,
                recyclerItemList as ArrayList<DataItem.CommunityRecyclerData>
            )
            adapter.removeCommunityFromList(recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.recyclerHeading.text = "Connect with the like-minded individuals"
            binding.viewAllItem.setOnClickListener {
                context.startActivity(Intent(context, ViewAllAvailableCommunitiesActivity::class.java))
            }
        }
        fun bindHotelSectionRecyclerView(recyclerItemList : List<HotelData>){
            val adapter = HotelSectionChildAdapter(context, DataItemType.HOTEL_SECTION, recyclerItemList as ArrayList<HotelData>)
            binding.childRecyclerView.adapter = adapter
            adapter.removeHotelFromList(recyclerItemList)
            binding.recyclerHeading.text = "Check into the most comfortable stays"
            binding.viewAllItem.setOnClickListener {
                context.startActivity(Intent(context, AllHotelsActivity::class.java))
            }
        }
        fun bindBlogsRecyclerView(recyclerItemList : List<AllBlogsData>){
            val adapter = BlogsChildAdapter(context, DataItemType.BLOGS,
                recyclerItemList as ArrayList<AllBlogsData>
            )
            binding.childRecyclerView.adapter = adapter
            adapter.removeBlogsFromList(recyclerItemList)
            binding.recyclerHeading.text = "Popular blogs you must read."

            binding.viewAllItem.setOnClickListener {
                context.startActivity(Intent(context, ViewAllBlogsActivity::class.java))
            }
        }
        fun bindHotelAwardsRecyclerView(recyclerItemList : List<DataItem.AwardsRecyclerData>){
            val adapter = AwardsChildAdapter(DataItemType.HOTEL_AWARDS, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.recyclerHeading.text = "Check what's in store"
            binding.viewAllItem.setOnClickListener {
                Toast.makeText(context, "Hotel Awards", Toast.LENGTH_SHORT).show()
            }
        }
        fun bindVendorsRecyclerView(recyclerItemList : List<ProfileServicesDataItem>){
            val adapter = VendorsChildAdapter(context, DataItemType.VENDORS,
                recyclerItemList as ArrayList<ProfileServicesDataItem>
            )
            adapter.removeVendorFromList(recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.recyclerHeading.text = "Avail the Best-in-class service for yourself."

            binding.viewAllItem.setOnClickListener {
                context.startActivity(Intent(context, ViewAllVendorsActivity::class.java))
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(dataItemList[position].viewType){
            DataItemType.BANNER ->
                R.layout.users_posts_card

            DataItemType.POLL ->
                R.layout.item_poll_profile

            DataItemType.Status ->
                R.layout.item_status

            DataItemType.Event ->{
                R.layout.item_event_post
            }

            DataItemType.CheckIn ->{
                R.layout.item_hotel_post
            }

            else ->
                R.layout.each_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.users_posts_card -> {
                val binding =
                    UsersPostsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BannerItemViewHolder(binding)
            }

            R.layout.item_poll_profile ->{
                  val poll = ItemPollProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                BannerItemViewHolderPoll(poll)
            }

            R.layout.item_status ->{
                val status = ItemStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                BannerItemViewHolderStatus(status)
            }

            R.layout.item_event_post ->{
                val event = ItemEventPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                BannerItemViewHolderEvent(event)
            }

            R.layout.item_hotel_post ->{
                val hotel = ItemHotelPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                BannerItemViewHolderCheck(hotel)
            }

            else -> {
                val binding =
                    EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecyclerItemViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){



            is BannerItemViewHolder -> {
                dataItemList[position].banner?.let { holder.bindBannerView(it,position) }
            }
            is BannerItemViewHolderPoll ->{
                dataItemList[position].banner?.let { holder.bindBannerView(it,position) }
            }
            is BannerItemViewHolderStatus ->{
                dataItemList[position].banner?.let { holder.bindBannerView(it,position) }
            }
            is BannerItemViewHolderEvent ->{
                dataItemList[position].banner?.let { holder.bindBannerView(it,position) }
            }
            is BannerItemViewHolderCheck -> {
                dataItemList[position].banner?.let { holder.bindBannerView(it,position) }
            }


            else -> {
                when (dataItemList[position].viewType) {
                    DataItemType.HOTEL_SECTION -> {
                        dataItemList[position].hotelSectionList?.let {
                            (holder as RecyclerItemViewHolder).bindHotelSectionRecyclerView(it)
                        }

                    }


                    DataItemType.BLOGS -> {
                        dataItemList[position].blogsRecyclerDataList?.let {
                            (holder as RecyclerItemViewHolder).bindBlogsRecyclerView(it)
                        }
                    }
                    DataItemType.HOTEL_AWARDS -> {
                        dataItemList[position].hotelAwardsList?.let {
                            (holder as RecyclerItemViewHolder).bindHotelAwardsRecyclerView(it)
                        }
                    }
                    DataItemType.VENDORS -> {
                        dataItemList[position].vendorsRecyclerDataList?.let {
                            (holder as RecyclerItemViewHolder).bindVendorsRecyclerView(it)
                        }
                    }
                    DataItemType.CREATE_COMMUNITY -> {
                        dataItemList[position].createCommunityRecyclerDataList?.let {
                            (holder as RecyclerItemViewHolder).bindCreateCommunityRecyclerView(it)
                        }
                    }
                    else -> {
                        dataItemList[position].communityRecyclerDataList?.let{
                            (holder as RecyclerItemViewHolder).bindCommunityRecyclerView(it)
                        }
                    }
                }
            }
        }
    }

    fun convertTimeToText(dataDate: String): String? {
        var convTime: String? = null
        val suffix = "Ago"

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val pasTime: Date = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val seconds = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hours = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val days = TimeUnit.MILLISECONDS.toDays(dateDiff)

            when {
                seconds < 60 -> convTime = "$seconds Sec $suffix"
                minutes < 60 -> convTime = "$minutes Min $suffix"
                hours < 24 -> convTime = "$hours Hrs $suffix"
                days >= 7 -> {
                    convTime = when {
                        days > 360 -> "${days / 360} Yr $suffix"
                        days > 30 -> "${days / 30} Months $suffix"
                        else -> "${days / 7} Week $suffix"
                    }
                }
                days < 7 -> convTime = "$days D $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }

    private fun saveStatus(blogId: String?, binding : ItemStatusBinding, operation: String, like: Boolean, onLiked : (Int) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.feedsApi.savePost(user_id, SavePost(operation,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        binding.savePost.setImageResource(R.drawable.svg_saved)
                        onLiked.invoke(0)
                    } else {
                        binding.savePost.setImageResource(R.drawable.svg_save_post)
                        onLiked.invoke(1)
                    }
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("savePost", "${response.toString()} ${response.body().toString()}")
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveEvent(blogId: String?, binding : ItemEventPostBinding, operation: String, like: Boolean, onLiked : (Int) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.feedsApi.savePost(user_id, SavePost(operation,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        binding.savePost.setImageResource(R.drawable.svg_saved)
                        onLiked.invoke(0)
                    } else {
                        binding.savePost.setImageResource(R.drawable.svg_save_post)
                        onLiked.invoke(1)
                    }
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("savePost", "${response.toString()} ${response.body().toString()}")
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveHotel(blogId: String?, binding : ItemHotelPostBinding, operation: String, like: Boolean, onLiked : (Int) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.feedsApi.savePost(user_id, SavePost(operation,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        binding.savePost.setImageResource(R.drawable.svg_saved)
                        onLiked.invoke(0)
                    } else {
                        binding.savePost.setImageResource(R.drawable.svg_save_post)
                        onLiked.invoke(1)
                    }
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("savePost", "${response.toString()} ${response.body().toString()}")
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun savePosts(blogId: String?, binding : UsersPostsCardBinding, operation: String, like: Boolean, onLiked : (Int) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.feedsApi.savePost(user_id, SavePost(operation,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        binding.savePost.setImageResource(R.drawable.svg_saved)
                        onLiked.invoke(0)
                    } else {
                        binding.savePost.setImageResource(R.drawable.svg_save_post)
                        onLiked.invoke(1)
                    }
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("savePost", "${response.toString()} ${response.body().toString()}")
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun removePostsFromList(data: List<DataItem>){
        try {
            data.forEach {
                if (it.banner!!.display_status == "0"){

                    if (dataItemList.contains(DataItem(DataItemType.BANNER, banner = it.banner))) {
                        dataItemList.remove(DataItem(DataItemType.BANNER, banner = it.banner))
                    }
                    if (dataItemList.contains(DataItem(DataItemType.POLL, banner = it.banner))) {
                        dataItemList.remove(DataItem(DataItemType.POLL, banner = it.banner))
                    }
                    if (dataItemList.contains(DataItem(DataItemType.Status, banner = it.banner))) {
                        dataItemList.remove(DataItem(DataItemType.Status, banner = it.banner))
                    }
                    if (dataItemList.contains(DataItem(DataItemType.Event, banner = it.banner))) {
                        dataItemList.remove(DataItem(DataItemType.Event, banner = it.banner))
                    }
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

    fun encodeData(
        messageType: String,
        userId: String,
        postId: String,
        profilePictureLink: String,
        firstImageLink: String,
        username: String,
        caption: String,
        verificationStatus: String,
        fullName: String
    ): String {
        val encodedMessageType = encodeString(messageType, 3)
        val encodedUserId = encodeString(userId, 5)
        val encodedPostId = encodeString(postId, 2)
        val encodedProfilePictureLink = encodeString(profilePictureLink, 4)
        val encodedFirstImageLink = encodeString(firstImageLink, 1)
        val encodedUsername = encodeString(username, 6)
        val encodedCaption = encodeString(caption, 7)
        val encodedVerificationStatus = encodeString(verificationStatus, 8)
        val encodedFullName = encodeString(fullName, 9)

        return "$encodedMessageType|$encodedUserId|$encodedPostId|$encodedProfilePictureLink|$encodedFirstImageLink|$encodedUsername|$encodedCaption|$encodedVerificationStatus|$encodedFullName"
    }

    fun encodeString(input: String, shift: Int): String {
        val encodedData = StringBuilder()
        for (char in input) {
            val asciiValue = char.toInt()
            if (char.isLetter()) {
                val isUpperCase = char.isUpperCase()
                val base = if (isUpperCase) 65 else 97
                val encodedAscii = (asciiValue - base + shift) % 26
                val encodedChar = (encodedAscii + base).toChar()
                encodedData.append(encodedChar)
            } else {
                encodedData.append(char)
            }
        }
        return encodedData.toString()
    }
}