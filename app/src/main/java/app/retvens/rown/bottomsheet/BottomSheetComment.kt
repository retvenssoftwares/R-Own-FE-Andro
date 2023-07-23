package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
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
import app.retvens.rown.utils.setupFullHeight
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetComment(val postID:String,val postprofile:String) : BottomSheetDialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var comments:EditText
    private lateinit var profile:ShapeableImageView
    lateinit var child :String
    lateinit var parentCommentId:String

    private lateinit var replying:ConstraintLayout
    private lateinit var replyingText:TextView
    private lateinit var cancelReply:ImageView

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

        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        empty = view.findViewById(R.id.empty)

        recyclerView = view.findViewById(R.id.comment_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        comments = view.findViewById(R.id.addThoughts)
showKeyBoard(comments)
        profile = view.findViewById(R.id.profileOnComment)
        replying = view.findViewById(R.id.replying)
        replyingText = view.findViewById(R.id.replyingText)
        cancelReply = view.findViewById(R.id.cancelReply)

        val post = view.findViewById<TextView>(R.id.postComment)

        child = "0"

        cancelReply.setOnClickListener {
            child = "0"
            replying.visibility = View.GONE
            if (postprofile.isNotEmpty()) {
                Glide.with(requireContext()).load(postprofile).into(profile)
            } else {
                profile.setImageResource(R.drawable.svg_user)
            }
        }

        comments.addTextChangedListener {
            if (comments.text.isNotEmpty()) {
                post.isClickable = true
                post.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            } else {
                post.isClickable = false
                post.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_40))
            }
        }

        post.setOnClickListener {

            if (comments.text.isNotEmpty()) {
                if (child == "1") {
                    replyComment(user_id, parentCommentId, postID)
                } else {
                    postComment(postID)
                }
            }else {
                Toast.makeText(context, "Field can not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        if (postprofile.isNotEmpty()) {
            Glide.with(requireContext()).load(postprofile).into(profile)
        } else {
            profile.setImageResource(R.drawable.svg_user)
        }

        getCommentList(postID)
    }
    private fun replyComment(userId: String, parentCommentId: String, postID: String) {

        val comment = comments.text.toString()

        val replyCommnt = RetrofitBuilder.feedsApi.replyComment(postID, PostCommentReplyClass(userId,comment,parentCommentId))

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
                    getCommentList(postID)
                    comments.text.clear()
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

        val comment = comments.text.toString()

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
                    comments.text.clear()
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
                    if (response.post.comments.isEmpty()){
                        empty.visibility = View.VISIBLE
                    } else {
                        empty.visibility = View.GONE
                    }
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
                if (dataItem.Profile_pic.isNotEmpty()) {
                    Glide.with(requireContext()).load(dataItem.Profile_pic).into(profile)
                } else {
                    profile.setImageResource(R.drawable.svg_user)
                }
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
            activity?.baseContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT)
    }
}