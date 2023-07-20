package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection


import app.retvens.rown.DataCollections.FeedCollection.Media
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.UsersPostsCardBinding
import com.bumptech.glide.Glide
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageSlideAdapter(private val context: Context, private var imageList: List<Media>, val banner: PostItem, val binding : UsersPostsCardBinding) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.image_slider_item, null)
        val ivImages = view.findViewById<ImageView>(R.id.iv_images)

        imageList[position].let {
            Glide.with(context)
                .load(it.post)
                .into(ivImages);
        }

        ivImages.setOnClickListener(DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View?) {
                val intent = Intent(context,PostsViewActivity::class.java)

                val images:ArrayList<String> = ArrayList()
                imageList.forEach { item ->
                    images.add(item.post)
                }
                intent.putStringArrayListExtra("postPic",images)

                intent.putExtra("caption",banner.caption)
                intent.putExtra("postId",banner.post_id)
                intent.putExtra("profilePic",banner.Profile_pic)
                intent.putExtra("like",banner.like)
                intent.putExtra("isSaved",banner.isSaved)
                context.startActivity(intent)
            }

            override fun onDoubleClick(view: View?) {
                val anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
                binding.likedAnimation.startAnimation(anim)
                binding.likedAnimation.visibility = View.VISIBLE
                    var count:Int =  banner.Like_count.toInt()
                    if(banner.islike){
                        app.retvens.rown.utils.postLike(banner.post_id, context) {
                            banner.like = "liked"
                            banner.islike = false
                            binding.likePost.setImageResource(R.drawable.liked_vectore)
                            count += 1
                            banner.Like_count = count.toString()
                            binding.likeCount.text = count.toString()
                        }
                    }
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    val anim = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
                    binding.likedAnimation.startAnimation(anim)
                    binding.likedAnimation.visibility = View.GONE }, 1000)

            }
        }))

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
    private fun postLike(postId:String) {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = LikesCollection(user_id)

        val like = RetrofitBuilder.feedsApi.postLike(postId,data)

        like.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                    if (response.isSuccessful) {
                        val response = response.body()!!
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT).show()
                    }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
            }
        })

    }

}