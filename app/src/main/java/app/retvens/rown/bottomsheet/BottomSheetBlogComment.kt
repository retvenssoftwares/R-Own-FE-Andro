package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.util.Log
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
import app.retvens.rown.viewAll.viewAllBlogs.BlogAllComments
import app.retvens.rown.viewAll.viewAllBlogs.BlogsCommentAdapter
import app.retvens.rown.viewAll.viewAllBlogs.Comment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class BottomSheetBlogComment(val blog_id :String, val blogProfile:String) : BottomSheetDialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var blogsCommentAdapter: BlogsCommentAdapter
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
            if (comment.text.isNotEmpty()) {
                if (child == "1") {
                    replyComment(user_id, parentCommentId, blog_id)
                } else {
                    postComment(blog_id)
                }
            } else {
                Toast.makeText(context, "Field can not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        Glide.with(requireContext()).load(blogProfile).into(profile)

        getCommentList(blog_id)
    }

    private fun replyComment(userId: String, parentCommentId: String, blog_id: String) {

        val comment = comment.text.toString()

        val replyCommnt = RetrofitBuilder.viewAllApi.replyBlogComment(blog_id, PostCommentReplyClass(userId,comment,parentCommentId))

        replyCommnt.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    getCommentList(blog_id)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun postComment(blog_id: String) {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val comment = comment.text.toString()

        val sendComment = RetrofitBuilder.viewAllApi.blogComment(blog_id, PostCommentClass(user_id,comment))

        sendComment.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    getCommentList(blog_id)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun getCommentList(blog_id: String) {
        val getComments = RetrofitBuilder.viewAllApi.getBlogComment(blog_id)

        getComments.enqueue(object : Callback<BlogAllComments?>, BlogsCommentAdapter.OnItemClickListener {
            override fun onResponse(call: Call<BlogAllComments?>, response: Response<BlogAllComments?>) {
                Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.d("BlogCommentsGET", response.toString())
                    try {
                        blogsCommentAdapter = BlogsCommentAdapter(requireContext(),response.comments)
                        recyclerView.adapter = blogsCommentAdapter
                        blogsCommentAdapter.notifyDataSetChanged()
                    } catch (e : NullPointerException){
                        Log.d("BlogCommentsGET", e.toString())
                    }


                    blogsCommentAdapter.setOnItemClickListener(this)
                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BlogAllComments?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(dataItem: Comment) {
                Glide.with(requireContext()).load(dataItem.Profile_pic).into(profile)
                child = "1"
                parentCommentId = dataItem.comment_id
            }
        })

    }


}