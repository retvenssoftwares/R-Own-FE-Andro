package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


import app.retvens.rown.DataCollections.FeedCollection.Media
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ImageSlideAdapter(private val context: Context, private var imageList: List<Media>) : PagerAdapter() {
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

        ivImages.setOnClickListener {

            val intent = Intent(context,PostsViewActivity::class.java)

            val images:ArrayList<String> = ArrayList()
            imageList.forEach { item ->
                images.add(item.post)
            }
            intent.putStringArrayListExtra("postPic",images)

//            intent.putExtra("caption",banner.caption)
//            intent.putExtra("postId",banner.post_id)
//            intent.putExtra("profilePic",banner.Profile_pic)
            context.startActivity(intent)

        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}