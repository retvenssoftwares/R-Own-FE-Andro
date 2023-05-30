package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.Comments
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.DataCollections.FeedCollection.PostCommentClass
import app.retvens.rown.DataCollections.FeedCollection.PostCommentReplyClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.FragmntAdapters.CommentAdapter
import app.retvens.rown.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetComment(val postID:String,val postprofile:String) : BottomSheetDialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var comment:EditText
    private lateinit var profile:ShapeableImageView
    lateinit var child :String
    lateinit var parentCommentId:String


    var mListener: OnBottomWhatToPostClickListener ? = null
    fun setOnWhatToPostClickListener(listener: OnBottomWhatToPostClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetWhatToPost? {
        return BottomSheetWhatToPost()
    }
    interface OnBottomWhatToPostClickListener{
        fun bottomWhatToPostClick(WhatToPostFrBo : String) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        recyclerView = view.findViewById(R.id.comment_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        comment = view.findViewById(R.id.addThoughts)

        profile = view.findViewById(R.id.profileOnComment)

        val post = view.findViewById<TextView>(R.id.postComment)

        child = "0"

        post.setOnClickListener {

            if (child == "1"){
                replyComment(user_id,parentCommentId,postID)
            }else{
                postComment(postID)
            }



        }

        Glide.with(requireContext()).load(postprofile).into(profile)

        getCommentList(postID)
    }

    private fun replyComment(userId: String, parentCommentId: String, postID: String) {

        val comment = comment.text.toString()

        val replyCommnt = RetrofitBuilder.feedsApi.replyComment(postID, PostCommentReplyClass(userId,comment,parentCommentId))

        replyCommnt.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    getCommentList(postID)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun postComment(postID: String) {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val comment = comment.text.toString()

        val sendComment = RetrofitBuilder.feedsApi.postComment(postID, PostCommentClass(user_id,comment))

        sendComment.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    getCommentList(postID)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun getCommentList(postID: String) {
        val getComments = RetrofitBuilder.feedsApi.getComment(postID)

        getComments.enqueue(object : Callback<GetComments?>, CommentAdapter.OnItemClickListener {
            override fun onResponse(call: Call<GetComments?>, response: Response<GetComments?>) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    commentAdapter = CommentAdapter(requireContext(),response)
                    recyclerView.adapter = commentAdapter
                    commentAdapter.notifyDataSetChanged()

                    commentAdapter.setOnItemClickListener(this)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetComments?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(dataItem: Comments) {
                Glide.with(requireContext()).load(dataItem.Profile_pic).into(profile)
                child = "1"
                parentCommentId = dataItem.comment_id
            }
        })

    }


}