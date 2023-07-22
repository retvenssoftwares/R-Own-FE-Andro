/*
 * *
 *  * Created by Shivam Tiwari on 20/05/23, 3:30 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 20/05/23, 2:35 AM
 *
 */

package app.retvens.rown.MessagingModule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.mesibo.api.Mesibo;
import com.mesibo.api.MesiboFile;
import com.mesibo.emojiview.EmojiconTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.microedition.khronos.opengles.GL;

import app.retvens.rown.Dashboard.DashBoardActivity;
import app.retvens.rown.NavigationFragments.home.PostDetailsActivity;
import app.retvens.rown.NavigationFragments.home.PostDetailsActivityNotification;
import app.retvens.rown.NavigationFragments.home.PostsViewActivity;
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity;
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity;
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity;
import app.retvens.rown.R;

public class MessageView extends RelativeLayout {
    private static int mThumbailWidth = 0;
    private boolean hasImage = false;
    ImageView mAudioVideoLayer;
    private MessageData mData = null;
    TextView mHeadingView = null;
    LayoutInflater mInflater = null;
    EmojiconTextView mMessageTextView = null;
    View mMessageView = null;
    LayoutParams mMsgParams;
    RelativeLayout mPTTlayout = null;
    FrameLayout mPicLayout = null;
    LayoutParams mPicLayoutParam;
    ThumbnailProgressView mPictureThumbnail = null;
    FrameLayout mReplayContainer = null;
    ImageView mReplyImage;
    RelativeLayout mReplyLayout = null;
    TextView mReplyMessage;
    Context context;
    TextView mReplyUserName;
    TextView mSubTitleView = null;
    FrameLayout.LayoutParams mThumbnailParams;
    LinearLayout mTitleLayout = null;
    LayoutParams mTitleParams;
    TextView mTitleView = null;
    ShapeableImageView profile;
    TextView fullName;
    TextView userName;
    ShapeableImageView postImage;
    TextView caption;
    CardView postCard;
    CardView profileCard;
    TextView profileName;
    TextView profileRole;
    ShapeableImageView mprofile;
    String type;

    public MessageView(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        init();
    }

    @SuppressLint("MissingInflatedId")
    public void init() {
        View v = this.mInflater.inflate(R.layout.message_view, this, true);
        this.mMessageView = v;
        this.profileCard = v.findViewById(R.id.profileCard);
        this.mprofile = v.findViewById(R.id.profile);
        this.profileName = v.findViewById(R.id.profilename);
        this.profileRole = v.findViewById(R.id.profileRole);
        this.postCard = v.findViewById(R.id.postCard);
        this.profile = v.findViewById(R.id.post_profile);
        this.fullName = v.findViewById(R.id.user_name_post);
        this.postImage = v.findViewById(R.id.Image);
        this.userName = v.findViewById(R.id.user_id_on_comment);
        this.caption = v.findViewById(R.id.recentCommentByUser);
        this.mPicLayout = (FrameLayout) v.findViewById(R.id.m_piclayout);
        this.mTitleLayout = (LinearLayout) v.findViewById(R.id.m_titlelayout);
        this.mTitleView = (TextView) v.findViewById(R.id.m_ptitle);
        this.mSubTitleView = (TextView) v.findViewById(R.id.m_psubtitle);
        this.mHeadingView = (TextView) v.findViewById(R.id.m_pheading);
        this.mMessageTextView = v.findViewById(R.id.m_pmessage);
        this.mPTTlayout = (RelativeLayout) v.findViewById(R.id.message_layout);
        this.mReplayContainer = (FrameLayout) v.findViewById(R.id.reply_container);
        this.mPicLayoutParam = (LayoutParams) this.mPicLayout.getLayoutParams();
        this.mTitleParams = (LayoutParams) this.mTitleLayout.getLayoutParams();
        this.mMsgParams = (LayoutParams) this.mMessageTextView.getLayoutParams();
    }

    public void loadImageView() {
        if (this.mPictureThumbnail == null) {
            this.mPictureThumbnail = (ThumbnailProgressView) this.mInflater.inflate(R.layout.thumbnail_progress_view_layout, this.mPicLayout, true).findViewById(R.id.m_picture);
            this.mAudioVideoLayer = (ImageView) this.mPictureThumbnail.findViewById(R.id.audio_video_layer);
//            this.mAudioVideoLayer.setVisibility(8);
            if (this.mThumbnailParams == null) {
                this.mThumbnailParams = (FrameLayout.LayoutParams) this.mPictureThumbnail.getLayoutParams();
            }
        }
    }
//
    public void loadReplyView() {
        if (this.mReplyLayout == null) {
            View v = this.mInflater.inflate(R.layout.reply_layout, this.mReplayContainer, true);
            this.mReplyLayout = (RelativeLayout) v.findViewById(R.id.reply_layout);
            this.mReplyImage = (ImageView) v.findViewById(R.id.reply_image);
            this.mReplyUserName = (TextView) v.findViewById(R.id.reply_name);
            this.mReplyMessage = (TextView) v.findViewById(R.id.reply_text);
        }
    }

    @SuppressLint("CheckResult")
    public void setData(MessageData data) {
        this.mData = data;

        if (Objects.equals(data.msg.message, "Post")){
            Log.e("msg","working");
        }

        context = getContext();

        ViewGroup.LayoutParams PTTParams = getLayoutParams();
        String title = this.mData.getTitle();
        String subtitle = this.mData.getSubTitle();
        String message = this.mData.getMessage();
        Bitmap thumbnail = this.mData.getImage();
        if (thumbnail != null) {
            loadImageView();
        } else {
            if (this.mPictureThumbnail != null) {
                this.mPictureThumbnail.setVisibility(8);
            }
            if (this.mAudioVideoLayer != null) {
                this.mAudioVideoLayer.setVisibility(8);
            }
            this.mPicLayout.setVisibility(8);
        }
        if (this.mAudioVideoLayer != null) {
            this.mAudioVideoLayer.setVisibility(8);
        }
        MesiboFile file = this.mData.getMesiboMessage().getFile();
        if (!(file == null || this.mAudioVideoLayer == null || (3 != file.type && 2 != file.type))) {
            this.mAudioVideoLayer.setVisibility(0);
        }
        if (this.mData.isReply()) {
            loadReplyView();
            this.mReplayContainer.setVisibility(0);
            this.mReplyLayout.setVisibility(0);
            if (this.mData.getReplyString() != null) {
                this.mReplyMessage.setText(this.mData.getReplyString());
            } else {
                this.mReplyMessage.setText("");
            }
            this.mReplyUserName.setText(this.mData.getReplyName());
            if (this.mData.getReplyBitmap() != null) {
                this.mReplyImage.setVisibility(0);
                this.mReplyImage.setImageBitmap(this.mData.getReplyBitmap());
            } else {
                this.mReplyImage.setVisibility(8);
            }
        } else if (this.mReplyLayout != null) {
            this.mReplyLayout.setVisibility(8);
            this.mReplayContainer.setVisibility(8);
        }
        if (thumbnail != null) {
            int width = MesiboUI.getConfig().mHorizontalImageWidth;
            if (thumbnail.getHeight() > thumbnail.getWidth()) {
                width = MesiboUI.getConfig().mVerticalImageWidth;
            }
            mThumbailWidth = (Mesibo.getDisplayWidthInPixel() * width) / 100;
            PTTParams.width = mThumbailWidth;
            LayoutParams picLayoutParam = (LayoutParams) this.mPicLayout.getLayoutParams();
            FrameLayout.LayoutParams thumbnailParams = (FrameLayout.LayoutParams) this.mPictureThumbnail.getLayoutParams();
            this.mTitleLayout.setLayoutParams(this.mTitleParams);
            this.mMessageTextView.setLayoutParams(this.mMsgParams);
            thumbnailParams.width = mThumbailWidth;
            thumbnailParams.height = (mThumbailWidth * thumbnail.getHeight()) / thumbnail.getWidth();
            if (!this.mData.hasThumbnail() || (!this.mData.isImageVideo() && thumbnail.getWidth() < 200 && thumbnail.getHeight() < 200)) {
                thumbnailParams.height = mThumbailWidth / 4;
                thumbnailParams.width = mThumbailWidth / 4;
                if (Build.VERSION.SDK_INT >= 19) {
                    LayoutParams titleParams = new LayoutParams(this.mTitleParams);
                    LayoutParams msgParams = new LayoutParams(this.mMsgParams);
                    if (message == null || message.length() < 32) {
                        titleParams.addRule(1, R.id.m_piclayout);
                        titleParams.addRule(6, R.id.m_piclayout);
                        msgParams.addRule(3, R.id.m_piclayout);
                    } else {
                        titleParams.addRule(1, R.id.m_piclayout);
                        titleParams.addRule(6, R.id.m_piclayout);
                        titleParams.topMargin = thumbnailParams.topMargin + (thumbnailParams.height / 4);
                        msgParams.addRule(3, R.id.m_titlelayout);
                    }
                    this.mTitleLayout.setLayoutParams(titleParams);
                    this.mMessageTextView.setLayoutParams(msgParams);
                }
            }
            this.mTitleView.requestLayout();
            this.mMessageTextView.requestLayout();
            picLayoutParam.height = thumbnailParams.height;
            picLayoutParam.width = thumbnailParams.width;
            this.mPicLayout.setLayoutParams(picLayoutParam);
            this.mPicLayout.requestLayout();
            this.mPictureThumbnail.setLayoutParams(thumbnailParams);
            this.mPictureThumbnail.requestLayout();
            this.mPictureThumbnail.setData(this.mData);
            Log.e("error",mPictureThumbnail.toString());
            this.mPicLayout.setVisibility(0);
            this.mPictureThumbnail.setVisibility(0);
            this.mMessageTextView.setTextColor(MesiboConfiguration.TOPIC_TEXT_COLOR_WITH_PICTURE);
            this.hasImage = true;
        } else {
            if (this.mData.isDeleted()) {
                this.mMessageTextView.setTextColor(MesiboConfiguration.DELETEDTOPIC_TEXT_COLOR_WITHOUT_PICTURE);
            } else {
                this.mMessageTextView.setTextColor(MesiboConfiguration.TOPIC_TEXT_COLOR_WITHOUT_PICTURE);
            }
            if (this.hasImage) {
            }
            this.hasImage = false;
        }
        if (!this.mData.isForwarded() || TextUtils.isEmpty(MesiboUI.getConfig().forwardedTitle)) {
            this.mHeadingView.setVisibility(8);
        } else {
            this.mHeadingView.setVisibility(0);
            this.mHeadingView.setText(MesiboUI.getConfig().forwardedTitle);
        }
        if (this.mData.isDeleted() || TextUtils.isEmpty(title)) {
            this.mTitleView.setVisibility(8);
        } else {
            this.mTitleView.setVisibility(0);
            this.mTitleView.setText(title);
        }
        if (this.mData.isDeleted() || TextUtils.isEmpty(subtitle)) {
            this.mSubTitleView.setVisibility(8);
        } else {
            this.mSubTitleView.setVisibility(0);
            this.mSubTitleView.setText(subtitle);
        }
        if (!TextUtils.isEmpty(message)) {
            this.mMessageTextView.setVisibility(0);
            boolean incoming = data.getStatus() == 19 || data.getStatus() == 18;
            if (data.getFavourite().booleanValue()) {
                if (incoming) {
                    Log.e("1",message.toString());
                    this.mMessageTextView.setText(message + " " + "              ");
                } else {
                    Log.e("2",message.toString());
                    this.mMessageTextView.setText(message + " " + MesiboConfiguration.FAVORITED_OUTGOING_MESSAGE_DATE_View);
                }
            } else if (incoming) {
                DecodeDataHelper decodeDataHelper = new DecodeDataHelper();
                Map<String, String> decodedData = decodeDataHelper.decodeData(message);

                Map<String, String> decodedprofile = Decoder.decodeProfileData(message);
                String messageType = decodedData.get("messageType");
                this.type = decodedData.get("messageType");
                if (Objects.equals(this.type, "Post")){
                    this.mMessageTextView.setVisibility(8);
                    this.mprofile.setVisibility(8);
                    String userId = decodedData.get("userId");
                    String postId = decodedData.get("postId");
                    String profilePictureLink = decodedData.get("profilePictureLink");
                    String firstImageLink = decodedData.get("firstImageLink");
                    String username = decodedData.get("username");
                    String caption = decodedData.get("caption");
                    String fullName = decodedData.get("fullName");
                    String verication = decodedData.get("verificationStatus");

                    this.fullName.setText(fullName);
                    this.caption.setText(caption);
                    this.userName.setText(username);
                    Glide.with(this).load(profilePictureLink).into(this.profile);
                    Glide.with(this).load(firstImageLink).into(this.postImage);
                    this.postCard.setVisibility(0);
                    this.postCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PostDetailsActivityNotification.class);
                            intent.putExtra("postId", postId);
                            context.startActivity(intent);
                        }
                    });
                    Log.e("3","working");
                }else if(Objects.equals(messageType, "Profile")){
                    this.profileCard.setVisibility(0);
                    this.mMessageTextView.setVisibility(8);
                    this.postCard.setVisibility(8);
                    String userID = decodedprofile.get("userID");
                    String verificationStatus = decodedprofile.get("verificationStatus");
                    String fullName = decodedprofile.get("fullName");
                    String userName = decodedprofile.get("userName");
                    String userRole = decodedprofile.get("userRole");
                    String pic = decodedprofile.get("profilePictureLink");

                    Log.e("11",fullName);
                    Log.e("22",pic);
                    Log.e("11",userRole);

                    this.profileName.setText(fullName);
                    this.profileRole.setText(userRole);
                    Glide.with(this).load(pic).into(this.mprofile);

                    this.profileCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Objects.equals(userRole, "Business Vendor/Freelancer")) {
                                Intent intent = new Intent(context, VendorProfileActivity.class);
                                intent.putExtra("userId", userID);
                                context.startActivity(intent);
                            }else if (Objects.equals(userRole, "Hotel Owner")){
                                Intent intent = new Intent(context, OwnerProfileActivity.class);
                                intent.putExtra("userId", userID);
                                context.startActivity(intent);
                            }else {
                                Intent intent = new Intent(context, UserProfileActivity.class);
                                intent.putExtra("userId", userID);
                                context.startActivity(intent);
                            }
                        }
                    });

                }
                else {
                    this.postCard.setVisibility(8);
                    this.profileCard.setVisibility(8);
                    this.mMessageTextView.setText(message + " " + "              ");
                }
            } else {
                DecodeDataHelper decodeDataHelper = new DecodeDataHelper();
                Map<String, String> decodedData = decodeDataHelper.decodeData(message);
                Map<String, String> decodedprofile = Decoder.decodeProfileData(message);
                String messageType = decodedData.get("messageType");
                this.type = decodedData.get("messageType");
                if (Objects.equals(this.type, "Post")){
                    this.mMessageTextView.setVisibility(8);
                    this.profileCard.setVisibility(8);
                    String userId = decodedData.get("userId");
                    String postId = decodedData.get("postId");
                    String profilePictureLink = decodedData.get("profilePictureLink");
                    String firstImageLink = decodedData.get("firstImageLink");
                    String username = decodedData.get("username");
                    String caption = decodedData.get("caption");
                    String verication = decodedData.get("verificationStatus");
                    String fullName = decodedData.get("fullName");

                    this.fullName.setText(fullName);
                    this.caption.setText(caption);
                    this.userName.setText(username);
                    Glide.with(this).load(profilePictureLink).into(this.profile);
                    Glide.with(this).load(firstImageLink).into(this.postImage);
                    this.postCard.setVisibility(0);
                    Log.e("4","working");

                    this.postCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PostDetailsActivityNotification.class);
                            intent.putExtra("postId", postId);
                            context.startActivity(intent);
                        }
                    });
                }else if(Objects.equals(messageType, "Profile")){
                    this.profileCard.setVisibility(0);
                    this.mMessageTextView.setVisibility(8);
                    this.postCard.setVisibility(8);
                    String userID = decodedprofile.get("userID");
                    String verificationStatus = decodedprofile.get("verificationStatus");
                    String fullName = decodedprofile.get("fullName");
                    String userName = decodedprofile.get("userName");
                    String userRole = decodedprofile.get("userRole");
                    String pic = decodedprofile.get("profilePictureLink");


                    this.profileName.setText(fullName);
                    this.profileRole.setText(userRole);
                    Glide.with(this).load(pic).into(this.mprofile);
                    this.profileCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Objects.equals(userRole, "Business Vendor/Freelancer")) {
                                Intent intent = new Intent(context, VendorProfileActivity.class);
                                intent.putExtra("userId", userID);
                                context.startActivity(intent);
                            }else if (Objects.equals(userRole, "Hotel Owner")){
                                Intent intent = new Intent(context, OwnerProfileActivity.class);
                                intent.putExtra("userId", userID);
                                context.startActivity(intent);
                            }else {
                                Intent intent = new Intent(context, UserProfileActivity.class);
                                intent.putExtra("userId", userID);
                                context.startActivity(intent);
                            }
                        }
                    });

                } else {
                    this.postCard.setVisibility(8);
                    this.profileCard.setVisibility(8);
                    this.mMessageTextView.setText(message + " " + MesiboConfiguration.NORMAL_OUTGOING_MESSAGE_DATE_View);
                }

            }
        } else {
            this.mMessageTextView.setVisibility(8);
            this.postCard.setVisibility(8);
            this.profileCard.setVisibility(8);
        }
        if (thumbnail == null) {
            PTTParams.width = -2;
        }
        setLayoutParams(PTTParams);
        ViewGroup.LayoutParams PTTParams2 = this.mMessageTextView.getLayoutParams();
        if (thumbnail != null) {
            PTTParams2.width = -1;
        } else {
            PTTParams2.width = -2;
        }
        this.mMessageTextView.setLayoutParams(PTTParams2);
    }

    public void setImage(Bitmap image) {
//        loadImageView();
        this.mPictureThumbnail.setImage(image);
    }

    public static class DecodeDataHelper {
        public static String decodeString(String input, int shift) {
            StringBuilder decodedData = new StringBuilder();
            for (char ch : input.toCharArray()) {
                int asciiValue = (int) ch;
                if (Character.isLetter(ch)) {
                    boolean isUpperCase = Character.isUpperCase(ch);
                    int base = isUpperCase ? 65 : 97;
                    int decodedAscii = (asciiValue - base - shift + 26) % 26;
                    char decodedChar = (char) (decodedAscii + base);
                    decodedData.append(decodedChar);
                } else {
                    decodedData.append(ch);
                }
            }
            return decodedData.toString();
        }

        public static Map<String, String> decodeData(String encodedData) {
            List<String> values = Arrays.asList(encodedData.split("\\|"));
            List<String> keys = Arrays.asList(
                    "messageType",
                    "userId",
                    "postId",
                    "profilePictureLink",
                    "firstImageLink",
                    "username",
                    "caption",
                    "verificationStatus",
                    "fullName"
            );

            List<String> decodedValues = new ArrayList<>();
            for (int index = 0; index < values.size(); index++) {
                String value = values.get(index);
                int shift;
                switch (index) {
                    case 0:
                        shift = 3;
                        break;
                    case 1:
                        shift = 5;
                        break;
                    case 2:
                        shift = 2;
                        break;
                    case 3:
                        shift = 4;
                        break;
                    case 4:
                        shift = 1;
                        break;
                    case 5:
                        shift = 6;
                        break;
                    case 6:
                        shift = 7;
                        break;
                    case 7:
                        shift = 8;
                        break;
                    case 8:
                        shift = 9;
                        break;
                    default:
                        shift = 0;
                }
                decodedValues.add(decodeString(value, shift));
            }

            Map<String, String> decodedData = new HashMap<>();
            int minSize = Math.min(keys.size(), decodedValues.size()); // Ensure iterating over the smaller size

            try {
                for (int i = 0; i < minSize; i++) {
                    String key = keys.get(i);
                    String value = decodedValues.get(i);
                    decodedData.put(key, value);
                }
            } catch (IndexOutOfBoundsException e) {
                // Handle the exception here
                // You can log an error message, provide a default value, or take appropriate action
                System.err.println("Index out of bounds: " + e.getMessage());
                // Perform fallback behavior or recovery steps if needed
            }

            return decodedData;
        }
    }


    public static class Decoder {
        public static Map<String, String> decodeProfileData(String encodedData) {
            List<String> keys = Arrays.asList(
                    "messageType",
                    "userID",
                    "verificationStatus",
                    "fullName",
                    "userName",
                    "userRole",
                    "profilePictureLink"
            );
            List<String> values = new ArrayList<>(Arrays.asList(encodedData.split("\\|")));

            List<String> decodedValues = new ArrayList<>();
            for (int index = 0; index < values.size(); index++) {
                String value = values.get(index);
                int shift;
                switch (index) {
                    case 0:
                        shift = 3;
                        break;
                    case 1:
                        shift = 5;
                        break;
                    case 2:
                        shift = 2;
                        break;
                    case 3:
                        shift = 4;
                        break;
                    case 4:
                        shift = 1;
                        break;
                    case 5:
                        shift = 6;
                        break;
                    case 6:
                        shift = 7;
                        break;
                    default:
                        shift = 0;
                }
                decodedValues.add(decodeString(value, shift));
            }

            Map<String, String> decodedData = new HashMap<>();
            int minSize = Math.min(keys.size(), decodedValues.size()); // Ensure iterating over the smaller size

            try {
                for (int i = 0; i < minSize; i++) {
                    String key = keys.get(i);
                    String value = decodedValues.get(i);
                    decodedData.put(key, value);
                }
            } catch (IndexOutOfBoundsException e) {
                // Handle the exception here
                // You can log an error message, provide a default value, or take appropriate action
                System.err.println("Index out of bounds: " + e.getMessage());
                // Perform fallback behavior or recovery steps if needed
            }

            return decodedData;
        }

        public static String decodeString(String input, int shift) {
            StringBuilder decodedData = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isLetter(ch)) {
                    char base = Character.isLowerCase(ch) ? 'a' : 'A';
                    int decodedAscii = (ch - base - shift + 26) % 26;
                    char decodedChar = (char) (decodedAscii + base);
                    decodedData.append(decodedChar);
                } else {
                    decodedData.append(ch);
                }
            }
            return decodedData.toString();
        }
    }








}
