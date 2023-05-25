package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.EachItemBinding
import app.retvens.rown.databinding.GetjoblistBinding
import app.retvens.rown.databinding.ItemPollProfileBinding
import app.retvens.rown.databinding.UsersPostsCardBinding
import app.retvens.rown.viewAll.vendorsDetails.ViewAllVendorsActivity
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllBlogsActivity
import app.retvens.rown.viewAll.viewAllCommunities.ViewAllAvailableCommunitiesActivity
import com.bumptech.glide.Glide


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

//
//            if (data != null && position <= data.size) {
//                banner.posts.forEach { it->

                        val post = banner

                    if (post.post_type == "share some media"){

                        Glide.with(context).load(post.Profile_pic).into(binding.postProfile)
                        binding.userIdOnComment.text = post.User_name
                        Log.e("username",post.User_name)
                        binding.recentCommentByUser.text = post.caption
                        Log.e("caption",post.caption)
                        binding.userNamePost.text = post.User_name

                        post.media.forEach { item ->
                            Glide.with(context).load(item.post).into(binding.postPic)
                        }


                    }

            binding.postProfile.setOnClickListener {
                context.startActivity(Intent(context, UserProfileActivity::class.java))
            }

            binding.postPic.setOnClickListener {
                context.startActivity(Intent(context, PostsViewActivity::class.java))
            }
            binding.postCard.setOnClickListener {
                context.startActivity(Intent(context, PostDetailsActivity::class.java))
            }

            binding.likePost.setOnClickListener {
                onItemClickListener?.onItemClick(banner)
            }

            binding.comment.setOnClickListener {
                onItemClickListener?.onItemClickForComment(banner,position)
            }

//                }
//            }
//        else{
//                Toast.makeText(context,"No Post Yet",Toast.LENGTH_SHORT).show()
//            }


//
//            val data = banner.posts
//            if (data != null && position <= data.size) {
//                val post = data[position]
//                Glide.with(context).load(post.Profile_pic).into(binding.postProfile)
//                binding.userIdOnComment.text = post.User_name
//                binding.recentCommentByUser.text = post.caption
//                binding.userNamePost.text = post.User_name
//
//               post.media.forEach { it ->
//                   Glide.with(context).load(it.post).into(binding.postPic)
//               }

//
//            } else {
//                Toast.makeText(context,"No Post Yet",Toast.LENGTH_SHORT).show()
//            }




        }
    }


    inner class BannerItemViewHolderPoll(private val binding : ItemPollProfileBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: PostItem, position: Int){


            banner.pollQuestion.forEach { item ->
                binding.titlePoll.text  = item.Question
            }















//            binding.postProfile.setOnClickListener {
//                context.startActivity(Intent(context, UserProfileActivity::class.java))
//            }
//
//            binding.postPic.setOnClickListener {
//                context.startActivity(Intent(context, PostsViewActivity::class.java))
//            }
//            binding.postCard.setOnClickListener {
//                context.startActivity(Intent(context, PostDetailsActivity::class.java))
//            }
//
//            binding.likePost.setOnClickListener {
//                onItemClickListener?.onItemClick(banner)
//            }
//
//            binding.comment.setOnClickListener {
//                onItemClickListener?.onItemClickForComment(banner)
//            }

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
        fun bindHotelSectionRecyclerView(recyclerItemList : List<DataItem.HotelSectionRecyclerData>){
            val adapter = HotelSectionChildAdapter(DataItemType.HOTEL_SECTION, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
            binding.recyclerHeading.text = "Connect with the like-minded individuals"
            binding.viewAllItem.setOnClickListener {
//                context.startActivity(Intent(context, ViewAllBlogsActivity::class.java))
            }
        }
        fun bindBlogsRecyclerView(recyclerItemList : List<DataItem.BlogsRecyclerData>){
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
        fun bindVendorsRecyclerView(recyclerItemList : List<DataItem.VendorsRecyclerData>){
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