package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.DataCollections.FeedCollection.Vote
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.profile.hotels.AddHotelActivity
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import app.retvens.rown.databinding.EachItemBinding
import app.retvens.rown.databinding.GetjoblistBinding
import app.retvens.rown.databinding.ItemEventBinding
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class MainAdapter(val context: Context, private val dataItemList: List<DataItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(dataItem: PostItem)
        fun onItemClickForComment(banner: PostItem,position: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    inner class BannerItemViewHolder(private val binding : UsersPostsCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){

                        val post = banner

                        Glide.with(context).load(post.Profile_pic).into(binding.postProfile)
                        binding.userIdOnComment.text = post.User_name
                        Log.e("username",post.User_name)
                        binding.recentCommentByUser.text = post.caption
                        Log.e("caption",post.caption)
                        binding.userNamePost.text = post.User_name

                        if (post.Like_count != ""){
                            binding.likeCount.text = post.Like_count
                        }
                        if (post.Comment_count != ""){
                            binding.commentCount.text = post.Comment_count
                        }

                        post.media.forEach { item ->
                            Glide.with(context).load(item.post).into(binding.postPic)
                            formatTimestamp(item.date_added)
                        }

                        if (post.like == "Liked"){
                            binding.likePost.setImageResource(R.drawable.liked_vectore)
                        }else if (post.like == "Unliked"){
                            binding.likePost.setImageResource(R.drawable.svg_like_post)
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

            binding.postPic.setOnClickListener {
                val intent = Intent(context,PostsViewActivity::class.java)
                post.media.forEach { item ->
                    intent.putExtra("postPic",item.post)
                }
                intent.putExtra("caption",banner.caption)
                intent.putExtra("postId",banner.post_id)
                intent.putExtra("profilePic",banner.Profile_pic)
                context.startActivity(intent)

            }
            binding.postCard.setOnClickListener {
                val intent = Intent(context,PostDetailsActivity::class.java)
                intent.putExtra("profilePic",banner.Profile_pic)
                intent.putExtra("profileName",banner.Full_name)
                intent.putExtra("userName",banner.User_name)
                intent.putExtra("caption",banner.caption)
                post.media.forEach { item ->
                    intent.putExtra("postPic",item.post)
                }
                intent.putExtra("likeCount",banner.Like_count)
                intent.putExtra("commentCount",banner.Comment_count)
                intent.putExtra("like",banner.like)
                intent.putExtra("postId",banner.post_id)
                context.startActivity(intent)
            }

            binding.likePost.setOnClickListener {

                banner.islike = !banner.islike

                val count:Int
                if(banner.islike){
                    binding.likePost.setImageResource(R.drawable.liked_vectore)
                    count = post.Like_count.toInt()+1
                    binding.likeCount.text = count.toString()
                }else{
                    binding.likePost.setImageResource(R.drawable.svg_like_post)
                    count = post.Like_count.toInt()
                    binding.likeCount.text = count.toString()
                }

                onItemClickListener?.onItemClick(banner)
            }

            binding.comment.setOnClickListener {
                onItemClickListener?.onItemClickForComment(banner,position)
            }

        }

        fun formatTimestamp(timestamp: String): String {
            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val instant = Instant.parse(timestamp)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val now = LocalDateTime.now()
            val duration = Duration.between(localDateTime, now)

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                when {
                    duration.toSeconds() < 60 -> "${duration.toSeconds()} seconds ago"
                    duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
                    duration.toHours() < 24 -> "${duration.toHours()} hours ago"
                    duration.toDays() < 31 -> "${duration.toDays()} days ago"
                    else -> "${duration.toDays() / 30} months ago"
                }
            } else {
                TODO("VERSION.SDK_INT < S")
            }

            binding.postTime.text = duration.toString()
        }


    }

    inner class BannerItemViewHolderEvent(private val binding : ItemEventPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){

            val post = banner
            Glide.with(context).load(post.Profile_pic).into(binding.postProfile)
                binding.userIdOnComment.text = post.User_name
                Log.e("username",post.User_name)
                binding.recentCommentByUser.text = post.caption
                Log.e("caption",post.caption)
                binding.userNamePost.text = post.User_name

                binding.eventTitle.text = post.Event_name

                binding.titleStatus.text = "Hello all, I am going to ${post.Event_name} on ${post.event_start_date}"

                Glide.with(context).load(post.event_thumbnail).into(binding.eventImage)

                if (post.Like_count != ""){
                    binding.likeCount.text = post.Like_count
                }
                if (post.Comment_count != ""){
                    binding.commentCount.text = post.Comment_count
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

            binding.userNamePost.text = banner.Full_name
            binding.titleStatus.text = banner.caption
            Glide.with(context).load(banner.Profile_pic).into(binding.postProfile)


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


    inner class BannerItemViewHolderPoll(private val binding : ItemPollProfileBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){

            binding.checkVotes.visibility = View.GONE

            banner.pollQuestion.forEach { item ->
                binding.titlePoll.text = item.Question
                binding.option1.text = item.Options[0].Option
                binding.option2.text = item.Options[1].Option

                val progressBarOption1: RoundedProgressBar = binding.progressBar
                val progressBarOption2: RoundedProgressBar = binding.progressBar2


                val vote1: List<String> = item.Options[0].votes.map { it.user_id }
                val vote2: List<String> = item.Options[1].votes.map { it.user_id }


                binding.voteOption1.setOnClickListener {

                    if (banner.voted == "no"){
                        voteOption(banner.post_id,item.Options[0].option_id)

                        binding.Option1Votes.text = "${vote} votes"

                        val vote = vote1.size + 1


                        val totalVotes = vote + vote2.size
                        val completedTasks = vote
                        val completedPercentage = (completedTasks.toDouble() / totalVotes) * 100.0
                        if (!completedPercentage.isNaN()) {
                            progressBarOption1.setProgressPercentage(completedPercentage)
                            binding.option1Percentage.text = "${completedPercentage}%"
                        }
                    }



                }
                binding.voteOption2.setOnClickListener {

                    if (banner.voted == "no"){
                        voteOption(banner.post_id,item.Options[1].option_id)

                        val vote = vote2.size + 1

                        binding.Option1Votes.text = "${vote1.size} votes"
                        binding.Option2Votes.text = "${vote2.size} votes"


                        val totalVotes = vote1.size + vote
                        val completedTasks2 = vote
                        val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
                        if (!completedPercentage2.isNaN()) {
                            progressBarOption2.setProgressPercentage(completedPercentage2)
                            binding.option2Percentage.text = "${completedPercentage2}%"

                        }
                    }



                }

                    binding.Option1Votes.text = "${vote1.size} votes"
                    binding.Option2Votes.text = "${vote2.size} votes"

                    val totalVotes = vote1.size + vote2.size
                    val completedTasks = vote1.size
                val completedPercentage = (completedTasks.toDouble() / totalVotes) * 100.0
                if (!completedPercentage.isNaN()) {
                    progressBarOption1.setProgressPercentage(completedPercentage)
                    binding.option1Percentage.text = "${completedPercentage}%"
                }


                val completedTasks2 = vote2.size
                val completedPercentage2 = (completedTasks2.toDouble() / totalVotes) * 100.0
                if (!completedPercentage2.isNaN()) {
                    progressBarOption2.setProgressPercentage(completedPercentage2)
                    binding.option2Percentage.text = "${completedPercentage2}%"
                }



            }

        }

        private fun voteOption(postId: String, optionId: String) {

            val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            val postVote = RetrofitBuilder.feedsApi.votePost(postId,optionId, LikesCollection(user_id))

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
    }

    inner class RecyclerItemViewHolder(private val binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
        }
        fun bindCreateCommunityRecyclerView(recyclerItemList : List<DataItem.CreateCommunityRecyclerData>){
            val adapter = CreateCommunityChildAdapter(DataItemType.CREATE_COMMUNITY, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.constRecycler.visibility = View.GONE
        }
        fun bindCommunityRecyclerView(recyclerItemList : List<DataItem.CommunityRecyclerData>){
            val adapter = CommunityChildAdapter(context, DataItemType.COMMUNITY, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.recyclerHeading.text = "Check into the most comfortable stays."
            binding.viewAllItem.setOnClickListener {
                context.startActivity(Intent(context, ViewAllAvailableCommunitiesActivity::class.java))
            }
        }
        fun bindHotelSectionRecyclerView(recyclerItemList : List<HotelData>){
            val adapter = HotelSectionChildAdapter(context, DataItemType.HOTEL_SECTION, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.recyclerHeading.text = "Connect with the like-minded individuals"
            binding.viewAllItem.setOnClickListener {
                context.startActivity(Intent(context, AllHotelsActivity::class.java))
            }
        }
        fun bindBlogsRecyclerView(recyclerItemList : List<AllBlogsData>){
            val adapter = BlogsChildAdapter(context, DataItemType.BLOGS, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
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
            val adapter = VendorsChildAdapter(context, DataItemType.VENDORS, recyclerItemList)
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





}