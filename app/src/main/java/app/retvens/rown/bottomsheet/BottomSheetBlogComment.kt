package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.PostCommentReplyClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.utils.setupFullHeight
import app.retvens.rown.viewAll.viewAllBlogs.BlogsCommentAdapter
import app.retvens.rown.viewAll.viewAllBlogs.Comment
import app.retvens.rown.viewAll.viewAllBlogs.CommentData.BlogPostComment
import app.retvens.rown.viewAll.viewAllBlogs.CommentData.CommentBlog
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetBlogComment(val blog_id :String, val blogProfile:String) : BottomSheetDialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var blogsCommentAdapter: BlogsCommentAdapter
    private lateinit var comment:EditText
    private lateinit var profile:ShapeableImageView
    lateinit var child :String
    lateinit var parentCommentId:String

    private lateinit var replying: ConstraintLayout
    private lateinit var replyingText:TextView
    private lateinit var cancelReply: ImageView

    private lateinit var empty:TextView

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
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        empty = view.findViewById(R.id.empty)
        recyclerView = view.findViewById(R.id.comment_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        comment = view.findViewById(R.id.addThoughts)
        showKeyBoard(comment)
        profile = view.findViewById(R.id.profileOnComment)
        replying = view.findViewById(R.id.replying)
        replyingText = view.findViewById(R.id.replyingText)
        cancelReply = view.findViewById(R.id.cancelReply)

        val post = view.findViewById<TextView>(R.id.postComment)
        comment.addTextChangedListener {
            if (comment.text.isNotEmpty()) {
                post.isClickable = true
                post.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            } else {
                post.isClickable = false
                post.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_40))
            }
        }

        child = "0"

        cancelReply.setOnClickListener {
            child = "0"
            replying.visibility = View.GONE
            Glide.with(requireContext()).load(blogProfile).into(profile)
        }

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
                    child = "0"
                    replying.visibility = View.GONE
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

        val comments = comment.text.toString()

        val sendComment = RetrofitBuilder.viewAllApi.blogComment(blog_id, BlogPostComment(user_id,comments))

        sendComment.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
//                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    comment.text.clear()
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

        getComments.enqueue(object : Callback<CommentBlog?>, BlogsCommentAdapter.OnItemClickListener {
            override fun onResponse(call: Call<CommentBlog?>, response: Response<CommentBlog?>) {
//                Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                if (response.isSuccessful){
                    val response = response.body()!!
                    Log.d("BlogCommentsGET", response.toString())
                    try {
                        if (response.get(0).isEmpty()){
                            empty.visibility = View.VISIBLE
                        } else {
                            empty.visibility = View.GONE
                        }
                        blogsCommentAdapter = BlogsCommentAdapter(requireContext(),response.get(0))
                        recyclerView.adapter = blogsCommentAdapter
                        blogsCommentAdapter.notifyDataSetChanged()
                        blogsCommentAdapter.setOnItemClickListener(this)
                    } catch (e : NullPointerException){
                        empty.visibility = View.VISIBLE
                        Log.d("BlogCommentsGET", e.toString())
                    }


                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommentBlog?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(dataItem: Comment) {
                Glide.with(requireContext()).load(dataItem.Profile_pic).into(profile)
                child = "1"
                replyingText.text = " You are replying to ${dataItem.User_name}"
                replying.visibility = View.VISIBLE
                parentCommentId = dataItem.comment_id
            }
        })

    }

    fun showKeyBoard(otpET: EditText) {
        otpET.requestFocus()
        val inputMethodManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT)
    }

}