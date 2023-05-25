package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.DataCollections.FeedCollection.GetComments
import app.retvens.rown.DataCollections.FeedCollection.PostCommentClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.FragmntAdapters.CommentAdapter
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetComment(val postID:String) : BottomSheetDialogFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var comment:EditText


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

        recyclerView = view.findViewById(R.id.comment_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        comment = view.findViewById(R.id.addThoughts)

        val post = view.findViewById<TextView>(R.id.postComment)

        post.setOnClickListener {
            postComment(postID)
        }


        Log.e("postID",postID)
        getCommentList(postID)
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

        Toast.makeText(requireContext(),postID,Toast.LENGTH_SHORT).show()

        getComments.enqueue(object : Callback<GetComments?> {
            override fun onResponse(call: Call<GetComments?>, response: Response<GetComments?>) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    commentAdapter = CommentAdapter(requireContext(),response)
                    recyclerView.adapter = commentAdapter
                    commentAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetComments?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }


}