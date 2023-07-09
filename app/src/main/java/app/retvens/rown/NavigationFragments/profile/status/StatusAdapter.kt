package app.retvens.rown.NavigationFragments.profile.status

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.ArraySet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.home.MainAdapter
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class StatusAdapter(val listS : ArrayList<PostItem>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LAYOUT_ONE = 1
    private val VIEW_TYPE_LAYOUT_TWO = 2
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(dataItem: PostItem)
        fun onItemClickForComment(banner: PostItem,position: Int)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    class LayoutOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.user_name_post)
        val postProfile = itemView.findViewById<ShapeableImageView>(R.id.post_profile)
        val status = itemView.findViewById<TextView>(R.id.title_status)
        val likeButton = itemView.findViewById<ImageView>(R.id.like_post)
        val commentButton = itemView.findViewById<ImageView>(R.id.comment)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
        val time = itemView.findViewById<TextView>(R.id.post_time)
        val likeCount = itemView.findViewById<TextView>(R.id.like_count)
        val commentCount = itemView.findViewById<TextView>(R.id.comment_count)
    }


    class LayoutTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val event_Image = itemView.findViewById<ShapeableImageView>(R.id.event_image)
        val name = itemView.findViewById<TextView>(R.id.user_name_post)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.post_profile)
        val verification = itemView.findViewById<ImageView>(R.id.verification)
        val status = itemView.findViewById<TextView>(R.id.title_status)
        val title = itemView.findViewById<TextView>(R.id.post_user_type)
        val location = itemView.findViewById<TextView>(R.id.post_user_dominican)
        val username = itemView.findViewById<TextView>(R.id.user_id_on_comment)
        val caption = itemView.findViewById<TextView>(R.id.recentCommentByUser)
        val time = itemView.findViewById<TextView>(R.id.post_time)
        val likeButton = itemView.findViewById<ImageView>(R.id.like_post)
        val commentButton = itemView.findViewById<ImageView>(R.id.comment)
        val likeCount = itemView.findViewById<TextView>(R.id.like_count)
        val commentCount = itemView.findViewById<TextView>(R.id.comment_count)
        val post_time = itemView.findViewById<TextView>(R.id.post_time)
        val book = itemView.findViewById<TextView>(R.id.book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)

        return when (viewType) {
            VIEW_TYPE_LAYOUT_ONE -> {
                val status: View = inflater.inflate(R.layout.item_status, parent, false)
                LayoutOneViewHolder(status)
            }
            VIEW_TYPE_LAYOUT_TWO -> {
                val status: View = inflater.inflate(R.layout.item_hotel_post, parent, false)
                LayoutTwoViewHolder(status)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {

        val data = listS[position]

        return if (data.post_type == "normal status"){
            VIEW_TYPE_LAYOUT_ONE
        }else{
            VIEW_TYPE_LAYOUT_TWO
        }


    }

    override fun getItemCount(): Int {
        return listS.size
    }


    override fun onBindViewHolder(holder: ViewHolder , position: Int) {

        val item = listS[position]

        when (holder.itemViewType) {
            VIEW_TYPE_LAYOUT_ONE -> {
                val layoutOneViewHolder = holder as LayoutOneViewHolder

                val time = TimesStamp.convertTimeToText(item.date_added)
                layoutOneViewHolder.time.text = time

                if (item.verificationStatus != "false"){
                    layoutOneViewHolder.verification.visibility = View.VISIBLE
                }

                if (item.User_name.isNotEmpty()){
                    layoutOneViewHolder.name.text = item.User_name
                } else{
                    layoutOneViewHolder.name.text = item.Full_name
                }
                layoutOneViewHolder.status.text = item.caption

                if (item.Profile_pic.isNotEmpty()) {
                    Glide.with(context).load(item.Profile_pic).into(layoutOneViewHolder.postProfile)
                } else {
                    layoutOneViewHolder.postProfile.setImageResource(R.drawable.svg_user)
                }

                if (item.likeCount != ""){
                    holder.likeCount.text = item.likeCount
                }
                if (item.commentCount != ""){
                    holder.commentCount.text = item.commentCount
                }

                if (item.liked != "not liked"){
                    holder.likeButton.setImageResource(R.drawable.liked_vectore)
                }else if (item.liked == "not liked"){
                    holder.likeButton.setImageResource(R.drawable.svg_like_post)
                }


                holder.likeButton.setOnClickListener {
                    item.islike = !item.islike

                    val count:Int
                    if(item.islike){
                        holder.likeButton.setImageResource(R.drawable.liked_vectore)
                        val like = item.likeCount.toInt()
                        count = like + 1
                        holder.likeCount.text = count.toString()
                    }else{
                        holder.likeButton.setImageResource(R.drawable.svg_like_post)
                        val like = item.likeCount.toInt()
                        count = like
                        holder.likeCount.text = count.toString()
                    }

                    onItemClickListener?.onItemClick(item)
                }

                holder.commentButton.setOnClickListener {
                    onItemClickListener?.onItemClickForComment(item,position)
                }



            }


            VIEW_TYPE_LAYOUT_TWO -> {
                val layoutTwoViewHolder = holder as LayoutTwoViewHolder

                val time = TimesStamp.convertTimeToText(item.date_added)
                layoutTwoViewHolder.time.text = time

                if (item.verificationStatus != "false"){
                    layoutTwoViewHolder.verification.visibility = View.VISIBLE
                }

                if (item.User_name.isNotEmpty()){
                    layoutTwoViewHolder.username.text = item.User_name
                } else{
                    layoutTwoViewHolder.username.text = item.Full_name
                }
                layoutTwoViewHolder.caption.text = item.caption
                try {
                    layoutTwoViewHolder.book.setOnClickListener {
                        val uri : Uri = Uri.parse("https://${item.bookingengineLink}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                    layoutTwoViewHolder.title.text = item.hotelName.toString()
                    layoutTwoViewHolder.location.text = item.hotelAddress.toString()
                    layoutTwoViewHolder.name.text = item.Full_name
                    layoutTwoViewHolder.status.text =
                        "Hello all, I am here at ${item.hotelName}. Let’s Catch Up."
                }catch (e:Exception){}

                if (item.Profile_pic.isNotEmpty()) {
                    Glide.with(context).load(item.Profile_pic).into(layoutTwoViewHolder.profile)
                } else {
                    layoutTwoViewHolder.profile.setImageResource(R.drawable.svg_user)
                }

                if (item.hotelCoverpicUrl.isNotEmpty()) {
                    Glide.with(context).load(item.hotelCoverpicUrl)
                        .into(layoutTwoViewHolder.event_Image)
                }

                layoutTwoViewHolder.event_Image.setOnClickListener {
                    val intent = Intent(context, HotelDetailsActivity::class.java)
                    intent.putExtra("name", item.hotelName)
                    intent.putExtra("logo", item.hotelCoverpicUrl)
                    intent.putExtra("hotelId", item.hotel_id)
                    intent.putExtra("hotelAddress", item.hotelAddress)
                    context.startActivity(intent)
                }

                layoutTwoViewHolder.title.setOnClickListener {
                    val intent = Intent(context, HotelDetailsActivity::class.java)
                    intent.putExtra("name", item.hotelName)
                    intent.putExtra("logo", item.hotelCoverpicUrl)
                    intent.putExtra("hotelId", item.hotel_id)
                    intent.putExtra("hotelAddress", item.hotelAddress)
                    context.startActivity(intent)
                }

                if (item.likeCount != ""){
                    holder.likeCount.text = item.likeCount
                }
                if (item.commentCount != ""){
                    holder.commentCount.text = item.commentCount
                }

                if (item.liked != "not liked"){
                    holder.likeButton.setImageResource(R.drawable.liked_vectore)
                }else if (item.liked == "not liked"){
                    holder.likeButton.setImageResource(R.drawable.svg_like_post)
                }


                holder.likeButton.setOnClickListener {
                    item.islike = !item.islike

                    val count:Int
                    if(item.islike){
                        holder.likeButton.setImageResource(R.drawable.liked_vectore)
                        val like = item.likeCount.toInt()
                        count = like + 1
                        holder.likeCount.text = count.toString()
                    }else{
                        holder.likeButton.setImageResource(R.drawable.svg_like_post)
                        val like = item.likeCount.toInt()
                        count = like
                        holder.likeCount.text = count.toString()
                    }

                    onItemClickListener?.onItemClick(item)
                }

                holder.commentButton.setOnClickListener {
                    onItemClickListener?.onItemClickForComment(item,position)
                }

            }
        }

    }

    fun removePostsFromList(data: List<PostItem>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    listS.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
}