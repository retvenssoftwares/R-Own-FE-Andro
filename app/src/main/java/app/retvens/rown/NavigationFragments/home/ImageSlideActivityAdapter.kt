package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse


import app.retvens.rown.R
import app.retvens.rown.utils.postLike
import com.bumptech.glide.Glide
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageSlideActivityAdapter(
    private val context: Context,
    private var imageList: ArrayList<String>
) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    var mListener: OnImageClickListener ? = null
    fun setOnImageClickListener(listener: OnImageClickListener?){
        mListener = listener
    }
    interface OnImageClickListener{
        fun imageClick(CTCFrBo : String)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.image_slider_item, null)
        val ivImages = view.findViewById<ImageView>(R.id.iv_images)

        imageList[position].let {
            Glide.with(context)
                .load(it)
                .into(ivImages);
        }

        ivImages.setOnClickListener(DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View?) {
            }

            override fun onDoubleClick(view: View?) {
                mListener?.imageClick("image")
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
}