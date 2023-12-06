package app.retvens.rown.viewAll.communityDetails

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.MesiboMediaAdapter
import app.retvens.rown.R
import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboMessage
import com.mesibo.api.MesiboProfile
import com.mesibo.api.MesiboReadSession
import com.mesibo.mediapicker.AlbumListData
import com.mesibo.mediapicker.AlbumPhotosData


class CommunityMediaFragment(val groupId:String) : Fragment(), MesiboProfile.Listener,Mesibo.MessageListener {
    private val MAX_THUMBNAIL_GALERY_SIZE = 35
    private lateinit var profile:MesiboProfile
    private  var galleryImages:ArrayList<AlbumListData> = ArrayList()
    private lateinit var mReadSession: MesiboReadSession
    private val mThumbnailMediaFiles:ArrayList<Bitmap> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profile = Mesibo.getProfile(groupId.toLong())

        Log.e("name",profile.name.toString())

        profile.addListener(this)
        Mesibo.addListener(this)

        mReadSession = profile.createReadSession(this)
        mReadSession.enableFiles(true)
        mReadSession.enableReadReceipt(true)
        mReadSession.read(100)

        recyclerView = view.findViewById(R.id.mediaItems)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),3)
         //recyclerView. //recyclerView.setHasFixedSize(true)
    }

    private fun addThumbnailToGallery(msg: MesiboMessage) {
        var thumbnailView: View? = null
        val path = msg.filePath
        mThumbnailMediaFiles?.add(msg.thumbnail)

        if (mThumbnailMediaFiles?.size!! < MAX_THUMBNAIL_GALERY_SIZE) {
            if (null != path) {
                val adapter = MesiboMediaAdapter(requireContext(),mThumbnailMediaFiles)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun MesiboProfile_onUpdate(p0: MesiboProfile?) {

    }

    override fun MesiboProfile_onEndToEndEncryption(p0: MesiboProfile?, p1: Int) {

    }

    override fun Mesibo_onMessage(msg: MesiboMessage) {
        if (!msg.hasFile()) return
        val newPhoto = AlbumPhotosData()
        newPhoto.setmPictueUrl(msg.filePath)
        newPhoto.setmSourceUrl(msg.filePath)

        Log.e("images",newPhoto.toString())

        addThumbnailToGallery(msg)

    }

    override fun Mesibo_onMessageStatus(p0: MesiboMessage) {

    }

    override fun Mesibo_onMessageUpdate(p0: MesiboMessage) {

    }


}