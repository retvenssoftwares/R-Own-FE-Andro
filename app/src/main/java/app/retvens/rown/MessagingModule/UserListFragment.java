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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mesibo.api.Mesibo;
import com.mesibo.api.MesiboGroupProfile;
import com.mesibo.api.MesiboMessage;
import com.mesibo.api.MesiboPresence;
import com.mesibo.api.MesiboProfile;
import com.mesibo.api.MesiboReadSession;
import com.mesibo.emojiview.EmojiconTextView;
import com.mesibo.messaging.MesiboImages;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import app.retvens.rown.ApiRequest.RetrofitBuilder;
import app.retvens.rown.ChatSection.MesiboUsers;
import app.retvens.rown.DataCollections.Count;
import app.retvens.rown.MessagingModule.AllUtils.LetterTileProvider;
import app.retvens.rown.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment implements Mesibo.MessageListener,
        Mesibo.PresenceListener, Mesibo.ConnectionListener, Mesibo.ProfileListener, Mesibo.SyncListener, Mesibo.GroupListener {
    public static MesiboGroupProfile.Member[] mExistingMembers = null;
    public static ArrayList<MesiboProfile> mMemberProfiles = new ArrayList<>();
    public static ArrayList<MesiboProfile> member = new ArrayList<>();
    public static ArrayList<MesiboProfile> mMemberGroup = new ArrayList<>();

    public static boolean isSheetOpen = false;
    /* access modifiers changed from: private */
    public boolean mCloseAfterForward = false;
    public boolean mContactView = false;
    public ImageView mEmptyView;
    public TextView count;
    public TextView name;
    public String num;
    public long mForwardId = 0;
    /* access modifiers changed from: private */
    public long[] mForwardMessageIds = null;
    /* access modifiers changed from: private */
    public String mForwardedMessage = null;
    /* access modifiers changed from: private */
    public Boolean mIsMessageSearching = false;
    /* access modifiers changed from: private */
    public String mSearchQuery = null;
    /* access modifiers changed from: private */
    public int mSelectionMode = 0;
    /* access modifiers changed from: private */
    public Handler mUiUpdateHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public long mUiUpdateTimestamp = 0;
    MessageContactAdapter mAdapter = null;
    private EditText searchBar;
    /* access modifiers changed from: private */
    public Runnable mUiUpdateRunnable = new Runnable() {
        public void run() {
            if (UserListFragment.this.mAdapter != null) {
                UserListFragment.this.mAdapter.notifyChangeInData();
            }
        }
    };
    Bundle mGroupEditBundle = null;
    long mGroupId = 0;
    Set<String> mGroupMembers = null;
    MesiboProfile mGroupProfile = null;
    LetterTileProvider mLetterTileProvider = null;
    MesiboUI.Config mMesiboUIOptions = null;
    RecyclerView mRecyclerView = null;
    LinearLayout horizontal_channel_recycler;
    ArrayList<MesiboProfile> tempmemberProfiles = new ArrayList<>();
    ArrayList<MesiboProfile> memberProfiles = new ArrayList<>();
    long mForwardIdForContactList = 0;
    String forwardMessage = MesiboUI.MESSAGE_CONTENT;
    long[] mForwardIds = {0};
    int mMode = 0;
    Bundle mEditGroupBundle = null;
    ImageView search_image;
    LinearLayout name_tite_layout, search_view;
    SearchView search_func;
    SearchView searchChats;
    private ArrayList<MesiboProfile> mAdhocUserList = null;
    private MesiboReadSession mDbSession = null;
    private boolean mFirstOnline = false;
    private WeakReference<MesiboUserListFragment.FragmentListener> mListener = null;
    private MesiboUI.Listener mMesiboUIHelperListener = null;
    private String mReadQuery = null;
    private long mRefreshTs = 0;
    private ArrayList<MesiboProfile> mSearchResultList = null;
    private boolean mSyncDone = false;
    private Timer mUiUpdateTimer = null;
    private TimerTask mUiUpdateTimerTask = null;
    private ArrayList<MesiboProfile> mUserProfiles = null;
    private LinearLayout mforwardLayout;
    private LinearLayout fabadd;
    private ImageView isOnlineDot;

    private LinearLayout add_channel;

    private CardView ChannelCardLayout;

    public void updateTitle(String title) {
        MesiboUserListFragment.FragmentListener l = getListener();
        if (l != null) {
            l.Mesibo_onUpdateTitle(title);
        }
    }

    public void updateSubTitle(String title) {
        MesiboUserListFragment.FragmentListener l = getListener();
        if (l != null) {
            l.Mesibo_onUpdateSubTitle(title);
        }
    }

    public boolean onClickUser(String address, long groupid, long forwardid) {
        MesiboUserListFragment.FragmentListener l = getListener();
        if (l == null) {
            return false;
        }
        return l.Mesibo_onClickUser(address, groupid, forwardid);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MesiboImages.init(getActivity());
        this.mMesiboUIHelperListener = MesiboUI.getListener();
        this.mMesiboUIOptions = MesiboUI.getConfig();

        this.mSelectionMode = MesiboUserListFragment.MODE_MESSAGELIST;
        this.mReadQuery = null;
        Bundle b = getArguments();
        if (b != null) {
            this.mSelectionMode = b.getInt(MesiboUserListFragment.MESSAGE_LIST_MODE, MesiboUserListFragment.MODE_MESSAGELIST);
            this.mReadQuery = b.getString("query", (String) null);
        }
        if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD) {
            this.mForwardId = getArguments().getLong("mid");
        }
        if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD) {
            this.mForwardMessageIds = getArguments().getLongArray(MesiboUI.MESSAGE_IDS);
            this.mForwardedMessage = getArguments().getString("message");
            this.mCloseAfterForward = getArguments().getBoolean(MesiboUI.FORWARD_AND_CLOSE, false);
        }
        if (this.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
            String title = this.mMesiboUIOptions.messageListTitle;
            if (TextUtils.isEmpty(title)) {
                title = Mesibo.getAppName();
            }
            updateTitle(title);
        } else if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT) {
            updateTitle(this.mMesiboUIOptions.selectContactTitle);
        } else if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD) {
            updateTitle(this.mMesiboUIOptions.forwardTitle);
        } else if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP) {
            updateTitle(getActivity().getResources().getString(R.string.select_group_members));
        } else if (this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP) {
            updateTitle(getActivity().getResources().getString(R.string.select_group_members));
            this.mGroupEditBundle = getArguments().getBundle(MesiboUI.BUNDLE);
            if (this.mGroupEditBundle != null) {
                this.mGroupId = this.mGroupEditBundle.getLong(MesiboUI.GROUP_ID);
                this.mGroupProfile = Mesibo.getProfile(this.mGroupId);
            }
        }
        if (this.mMesiboUIOptions.useLetterTitleImage) {
            this.mLetterTileProvider = new LetterTileProvider(getActivity(), 60, this.mMesiboUIOptions.mLetterTitleColors);
        }
        this.mSearchResultList = new ArrayList<>();
        this.mUserProfiles = new ArrayList<>();
        int layout = R.layout.fragment_chat;
        if (MesiboUI.getConfig().mUserListFragmentLayout != 0) {
            layout = MesiboUI.getConfig().mUserListFragmentLayout;
        }
        View view = inflater.inflate(layout, container, false);
        setHasOptionsMenu(true);

//        this.mforwardLayout.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP) {
//                    UserListFragment.this.mAdapter.createNewGroup();
//                } else if (UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP) {
//                     UserListFragment.this.mAdapter.modifyGroupDetail();
//                } else if (UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD) {
//                    UserListFragment.this.mAdapter.forwardMessageToContacts();
//                }
//            }
//        });
        this.mEmptyView = (ImageView) view.findViewById(R.id.emptyview_text);
        this.count = view.findViewById(R.id.count);
        this.name = view.findViewById(R.id.name);
        setEmptyViewText();
        this.mUserProfiles = new ArrayList<>();
        this.mRecyclerView = view.findViewById(R.id.message_contact_frag_rv);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mRecyclerView.getContext()));
        this.mAdapter = new MessageContactAdapter(getActivity(), this, this.mUserProfiles, this.mSearchResultList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.fabadd = view.findViewById(R.id.fab_add);
        this.searchBar = view.findViewById(R.id.searchBar);

        getCount();
        ArrayList<MesiboProfile> Search = this.mUserProfiles;



        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<MesiboProfile> filter = Search.stream()
                        .filter(item -> item.getName().toLowerCase().contains(s.toString().toLowerCase()))
                        .collect(Collectors.toCollection(ArrayList::new));

                mAdapter.updateData(filter);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

                this.fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSheetOpen)
                    showConatcts();
            }
        });
        this.isOnlineDot = (ImageView) view.findViewById(R.id.isOnlineDot);

        ArrayList<MesiboProfile> messagedUsers = mAdapter.getActiveUserlist();
        Log.e("mem", String.valueOf(mUserProfiles.size()));

        name_tite_layout = getActivity().findViewById(R.id.name_tite_layout);

        return view;
    }



    private void showConatcts() {

                startActivity(new Intent(requireContext(), MesiboUsers.class));

    }

    public MesiboUserListFragment.FragmentListener getListener() {
        if (this.mListener == null) {
            return null;
        }
        return (MesiboUserListFragment.FragmentListener) this.mListener.get();
    }

    public void setListener(MesiboUserListFragment.FragmentListener listener) {
        this.mListener = new WeakReference<>(listener);
    }

    public void setEmptyViewText() {
        if (this.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
            this.mEmptyView.setVisibility(0);
            this.name.setVisibility(0);
            this.count.setVisibility(0);
        } else {
            this.mEmptyView.setVisibility(8);
            this.name.setVisibility(8);
            this.count.setVisibility(8);
        }
    }

    public void filterUsersByName(String newText) {
        if (this.mAdapter != null) {
            this.mAdapter.filter(newText);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void showForwardLayout() {
        this.mforwardLayout.setVisibility(View.VISIBLE);
    }

    public void handleEmptyUserList(int userListsize) {
        if (userListsize == 0) {
            this.mRecyclerView.setVisibility(View.INVISIBLE);
            this.mEmptyView.setVisibility(View.VISIBLE);
            this.count.setVisibility(View.VISIBLE);
            this.name.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferencesName = getContext().getSharedPreferences("SaveFullName", Context.MODE_PRIVATE);
            String profileName = sharedPreferencesName.getString("full_name", "");
            this.name.setText("Hi, "+profileName);

            this.count.setText("Connect with "+num+" Hoteliers on R-Own and Engage with a Vibrant Community!");

            return;
        }
        this.mRecyclerView.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.GONE);
        this.count.setVisibility(View.GONE);
        this.name.setVisibility(View.GONE);
    }

    public void hideForwardLayout() {
        this.mforwardLayout.setVisibility(View.GONE);
    }

    private void updateNotificationBadge() {
        if (!this.mMesiboUIOptions.mEnableNotificationBadge) {
        }
    }

    private void getCount() {
        // Create a Retrofit service instance
        Call<Count> service = RetrofitBuilder.INSTANCE.getRetrofitBuilder().getCount();

        // Call the API method to get the count

        service.enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> response) {
                if (response.isSuccessful()) {
                    Count countResponse = response.body();
                    if (countResponse != null) {
                        String formattedCount = formatCount(countResponse.getCount());
                        num = formattedCount;
                    }
                } else {
                    Log.e("error", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Count> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public static String formatCount(String count) {
        try {
            int countValue = Integer.parseInt(count);
            if (countValue < 1000) {
                return count;
            } else if (countValue < 1500) {
                return "1k+";
            } else if (countValue < 2000) {
                return "1.5k+";
            } else if (countValue < 1_000_000) {
                return (countValue / 1000) + "k+";
            } else if (countValue < 1_500_000) {
                return "1M+";
            } else if (countValue < 2_000_000) {
                return "1.5M+";
            } else if (countValue < 1_000_000_000) {
                return (countValue / 1_000_000) + "M+";
            } else {
                return "1B+";
            }
        } catch (NumberFormatException e) {
            return count;
        }
    }


    public synchronized void addNewMessage(MesiboMessage params) {
        if (params.groupid <= 0 || params.groupProfile != null) {
            MesiboUserListFragment.FragmentListener l = getListener();
            if (l == null || !l.Mesibo_onUserListFilter(params)) {
                UserData userData = UserData.getUserData(params);
                if (this.mIsMessageSearching.booleanValue() && params.isDbMessage()) {
                    this.mAdhocUserList = this.mAdapter.getActiveUserlist();
                    if (this.mAdhocUserList.size() > this.mAdapter.mCountProfileMatched + 1) {
                        int i = this.mAdapter.mCountProfileMatched == 0 ? this.mAdapter.mCountProfileMatched : this.mAdapter.mCountProfileMatched + 1;
                        this.mAdhocUserList.get(i).setName(String.valueOf(this.mAdhocUserList.size() - i) + " " + MesiboConfiguration.MESSAGE_STRING_USERLIST_SEARCH);
                    } else {
                        MesiboProfile mup = new MesiboProfile();
                        mup.setName("1 Messages");
                        this.mAdhocUserList.add(this.mAdhocUserList.size(), mup);
                    }
                }
                MesiboProfile user = params.profile;
                if (params.groupProfile != null) {
                    user = params.groupProfile;
                }
                if (user == null) {
                    Log.d("error", "Should not happen");
                }
                if (this.mIsMessageSearching.booleanValue()) {
                    if (params.groupProfile != null) {
                        user = params.groupProfile.cloneProfile();
                    } else {
                        user = params.profile.cloneProfile();
                    }
                }
                if (user.other == null) {
                    user.other = new UserData(user);
                }
                ((UserData) user.other).setMessage(params);
                if (params.isRealtimeMessage()) {
                    updateNotificationBadge();
                }
                int i2 = 0;
                while (true) {
                    if (i2 < this.mAdhocUserList.size()) {
                        UserData mcd = (UserData) this.mAdhocUserList.get(i2).other;
                        if (mcd != null && params.compare(mcd.getPeer(), mcd.getGroupId())) {
                            this.mAdhocUserList.remove(i2);
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                if (params.isDbSummaryMessage() || params.isDbMessage()) {
                    this.mAdhocUserList.add(user);
                } else {
                    this.mAdhocUserList.add(0, user);
                }
                if (this.mUiUpdateTimer != null) {
                    this.mUiUpdateTimer.cancel();
                    this.mUiUpdateTimer = null;
                }
                if (params.isRealtimeMessage()) {
                    long ts = Mesibo.getTimestamp();
                    if (ts - this.mUiUpdateTimestamp > 2000) {
                        this.mAdapter.notifyChangeInData();
                    } else {
                        long timeout = 2000;
                        if (ts - params.ts < 5000) {
                            timeout = 500;
                        }
                        this.mUiUpdateTimestamp = ts;
                        this.mUiUpdateTimer = new Timer();
                        this.mUiUpdateTimerTask = new TimerTask() {
                            public void run() {
                                UserListFragment.this.mUiUpdateHandler.post(UserListFragment.this.mUiUpdateRunnable);
                            }
                        };
                        this.mUiUpdateTimer.schedule(this.mUiUpdateTimerTask, timeout);
                    }
                }
            }
        }
    }


    public void Mesibo_onPresence(MesiboPresence msg) {
        if (3L == msg.presence || 4L == msg.presence || 11L == msg.presence) {
            if (null != msg && null != msg.profile) {
                if (msg.groupid <= 0L || null != msg.groupProfile) {
                    MesiboProfile profile = msg.profile;
                    if (msg.isGroupMessage()) {
                        profile = Mesibo.getProfile(msg.groupid);
                        if (null == profile) {
                            return;
                        }
                    }

                    UserData data = UserData.getUserData(profile);
                    int position = data.getUserListPosition();
                    if (position >= 0) {
                        if (this.mAdhocUserList.size() > position) {
                            if (3L == msg.presence && msg.isGroupMessage()) {
                                data.setTypingUser(msg.profile);
                            }

                            try {
                                if (profile != this.mAdhocUserList.get(position)) {
                                    return;
                                }
                            } catch (Exception var6) {
                                return;
                            }

                            this.mAdapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        }
    }

    public void Mesibo_onPresenceRequest(MesiboPresence mesiboPresence) {
    }

    private void updateUiIfLastMessage(MesiboMessage params) {
        if (params.isLastMessage()) {
            if (!this.mIsMessageSearching.booleanValue() && !TextUtils.isEmpty(this.mSearchQuery)) {
                this.mAdapter.filter(this.mSearchQuery);
            }
            this.mAdapter.notifyChangeInData();
            updateNotificationBadge();
        }
    }

    public void Mesibo_onMessage(MesiboMessage msg) {
        if (msg.isIncomingCall() || msg.isOutgoingCall() || msg.isEndToEndEncryptionStatus()) {
            updateUiIfLastMessage(msg);
        } else if (msg.groupid <= 0 || msg.groupProfile != null) {
            addNewMessage(msg);
            updateUiIfLastMessage(msg);
        } else {
            updateUiIfLastMessage(msg);
        }
        int counter = 0;
        ArrayList<MesiboProfile> mesiboProfiles = Mesibo.getSortedUserProfiles();
        for (int i=0; i<mesiboProfiles.size(); i++){
            UserData data = UserData.getUserData(mesiboProfiles.get(i));
            data.getUnreadCount();
            if (data.getUnreadCount()>0){
                counter++;
            }
        }
        //Toast.makeText(getContext(), ""+counter, Toast.LENGTH_SHORT).show();
        Utils.unReadCount = counter;
    }

    public void Mesibo_onMessageStatus(MesiboMessage msg) {
        for (int i = 0; i < this.mUserProfiles.size(); i++) {
            UserData mcd = (UserData) this.mUserProfiles.get(i).other;
            if (mcd.getmid() != 0 && mcd.getmid() == msg.mid) {
                mcd.setMessage(msg);
                if (msg.isDeleted()) {
                    mcd.setMessage(getString(R.string.deletedMessageTitle));
                    mcd.setDeletedMessage(true);
                }
                this.mAdapter.notifyItemChanged(i);
            }
        }
       }

    public void Mesibo_onMessageUpdate(MesiboMessage msg) {
        Mesibo_onMessageStatus(msg);
    }

    public void Mesibo_onConnectionStatus(int status) {
        int status2 = Mesibo.getConnectionStatus();
        if (status2 == 1) {
            if (!this.mFirstOnline && Mesibo.getLastProfileUpdateTimestamp() > this.mRefreshTs) {
                showUserList(100);
            }
            this.mFirstOnline = true;
            if (this.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                String title = this.mMesiboUIOptions.messageListTitle;
                if (TextUtils.isEmpty(title)) {
                    title = Mesibo.getAppName();
                }
                updateTitle(title);
            }
            updateSubTitle(this.mMesiboUIOptions.onlineIndicationTitle);
        } else if (status2 == 6) {
            updateSubTitle(this.mMesiboUIOptions.connectingIndicationTitle);
        } else if (status2 == 10) {
            updateSubTitle(this.mMesiboUIOptions.suspendIndicationTitle);
            Utils.showServicesSuspendedAlert(getActivity());
        } else if (status2 == 8) {
            updateSubTitle(getActivity().getResources().
                    getString(R.string.no_network_text));
        } else if (status2 == 22) {
            getActivity().finish();
        } else {
            updateSubTitle(this.mMesiboUIOptions.offlineIndicationTitle);
        }
    }

    private String getDate(long time) {
        int days = Mesibo.daysElapsed(time);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        if (days == 0) {
            return DateFormat.format("HH:mm", cal).toString();
        }
        if (days == 1) {
            return "Yesterday";
        }
        if (days < 7) {
            return DateFormat.format("E, dd MMM", cal).toString();
        }
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }

    public void onResume() {
        UserListFragment.super.onResume();
        showUserList(100);
        Mesibo_onConnectionStatus(Mesibo.getConnectionStatus());
        Utils.showServicesSuspendedAlert(getActivity());
    }

    public void onPause() {
        UserListFragment.super.onPause();
        Mesibo.removeListener(this);
    }

    public void onStop() {
        UserListFragment.super.onStop();
        if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD || this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP || this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP) {
            Iterator<MesiboProfile> it = this.mUserProfiles.iterator();
            while (it.hasNext()) {
                it.next().uiFlags &= -16777217;
            }
        }
    }

    public void onLongClick() {
    }

    public void Mesibo_onSync(int count) {
        int i = count;
        if (count > 0) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    UserListFragment.this.showUserList(100);
                }
            });
        }
    }

    public void showUserList(int readCount) {
        Log.d("showUserList", "showUserList");
        setEmptyViewText();
        if (this.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
            this.mRefreshTs = Mesibo.getTimestamp();
            Mesibo.addListener(this);
            this.mAdhocUserList = this.mUserProfiles;
            this.mUserProfiles.clear();
            this.mAdapter.onResumeAdapter();
            MesiboReadSession.endAllSessions();
            this.mDbSession = new MesiboReadSession(this);
            this.mDbSession.setQuery(this.mReadQuery);
            this.mDbSession.enableSummary(true);
            this.mDbSession.read(readCount);
        } else {
            this.mUserProfiles.clear();
            ArrayList profiles = Mesibo.getSortedUserProfiles();
            if (profiles != null && profiles.size() > 0 && !TextUtils.isEmpty(this.mMesiboUIOptions.createGroupTitle) && this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT) {
                MesiboProfile user = new MesiboProfile();
                user.address = this.mMesiboUIOptions.createGroupTitle;
                user.setName(this.mMesiboUIOptions.createGroupTitle);
                //user.setStatus(MesiboConfiguration.CREATE_NEW_GROUP_MESSAGE_STRING);
                user.lookedup = true;
                UserData ud = new UserData(user);
                Bitmap b = MesiboImages.getDefaultGroupBitmap();
                ud.setImageThumbnail(b);
                ud.setImage(b);
                ud.setFixedImage(true);
                user.other = ud;
                ud.setMessage(user.getStatus());
                this.mUserProfiles.add(user);
            }
            if (this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD && this.mMesiboUIOptions.showRecentInForward) {
                this.mUserProfiles.addAll(Mesibo.getRecentUserProfiles());
                if (this.mUserProfiles.size() > 0) {
                    MesiboProfile tempUserProfile = new MesiboProfile();
                    tempUserProfile.setName(String.valueOf(this.mMesiboUIOptions.recentUsersTitle));
                    this.mUserProfiles.add(0, tempUserProfile);
                }
                MesiboProfile tempUserProfile1 = new MesiboProfile();
                tempUserProfile1.setName(String.valueOf(getActivity().getResources().getString(R.string.all_users)));
                this.mUserProfiles.add(tempUserProfile1);
            }
            if (this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP) {
                if (this.memberProfiles.size() == 0) {
                    Mesibo.addListener(this);
                    Mesibo.getProfile(this.mGroupId).getGroupProfile().getMembers(100, true, this);
                    return;
                }

                this.mUserProfiles.addAll(this.memberProfiles);
                MesiboProfile tempUserProfile2 = new MesiboProfile();
                tempUserProfile2.setName(String.valueOf(getActivity().getResources().getString(R.string.group_members)));
                this.mUserProfiles.add(0, tempUserProfile2);
                MesiboProfile tempUserProfile12 = new MesiboProfile();
                tempUserProfile12.setName(String.valueOf(getActivity().getResources().getString(R.string.all_users)));
                this.mUserProfiles.add(tempUserProfile12);
            }
            this.mUserProfiles.addAll(Mesibo.getSortedUserProfiles());
            for (int i = this.mUserProfiles.size() - 1; i >= 0; i--) {
                MesiboProfile user2 = this.mUserProfiles.get(i);
                if (!TextUtils.isEmpty(user2.address) || user2.groupid != 0) {
                    if (TextUtils.isEmpty(user2.getName()) && user2.groupid > 0) {
                        this.mUserProfiles.remove(i);
                    } else if (this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP || this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP) {
                        if (user2.groupid > 0) {
                            this.mUserProfiles.remove(i);
                        } else if (!TextUtils.isEmpty(user2.address) && this.mGroupMembers != null && this.mGroupMembers.contains(user2.address)) {
                            user2.uiFlags |= 16777216;
                            showForwardLayout();
                        }
                    }
                } else if (!user2.getName().equalsIgnoreCase(getActivity().getResources().getString(R.string.all_users)) && !user2.getName().equalsIgnoreCase(this.mMesiboUIOptions.recentUsersTitle) && !user2.getName().equalsIgnoreCase(getActivity().getResources().getString(R.string.group_members))) {
                    this.mUserProfiles.remove(i);
                }
            }
        }
        this.mAdapter.notifyChangeInData();
    }

    /* access modifiers changed from: private */
    public void updateContacts(MesiboProfile userProfile) {
        int position;
        if (userProfile == null) {
            showUserList(100);
        } else if (userProfile.other != null && (position = UserData.getUserData(userProfile).getUserListPosition()) >= 0) {
            if (userProfile.isDeleted()) {
                this.mUserProfiles.remove(position);
                this.mAdapter.notifyDataSetChanged();
            } else if (position >= 0) {
                this.mAdapter.notifyItemChanged(position);
            }
        }
    }

    public boolean Mesibo_onGetProfile(MesiboProfile userProfile) {
        return false;
    }

    public void Mesibo_onProfileUpdated(MesiboProfile userProfile) {
        if (userProfile != null && userProfile.other == null) {
            return;
        }
        if (Mesibo.isUiThread()) {
            updateContacts(userProfile);
            return;
        }
        final MesiboProfile profile = userProfile;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                UserListFragment.this.updateContacts(profile);
            }
        });
    }

    public void Mesibo_onGroupCreated(MesiboProfile mesiboProfile) {
    }

    public void Mesibo_onGroupJoined(MesiboProfile mesiboProfile) {
    }

    public void Mesibo_onGroupLeft(MesiboProfile mesiboProfile) {
    }
    public void Mesibo_onGroupMembers(MesiboProfile mesiboProfile, MesiboGroupProfile.Member[] members) {
        mExistingMembers = members;

        for (MesiboGroupProfile.Member m : members) {
            this.tempmemberProfiles.add(m.getProfile());
        }

        Set<MesiboProfile> s = new HashSet<MesiboProfile>(tempmemberProfiles);
     memberProfiles = new ArrayList<>(s);
        if (this.memberProfiles.size() > 0) {
            showUserList(100);
        }

    }

    public void Mesibo_onGroupMembersJoined(MesiboProfile mesiboProfile, MesiboGroupProfile.Member[] members) {
    }

    public void Mesibo_onGroupMembersRemoved(MesiboProfile mesiboProfile, MesiboGroupProfile.Member[] members) {
    }

    public void Mesibo_onGroupSettings(MesiboProfile mesiboProfile, MesiboGroupProfile.GroupSettings groupSettings, MesiboGroupProfile.MemberPermissions memberPermissions, MesiboGroupProfile.GroupPin[] groupPins) {
    }

    public void Mesibo_onGroupError(MesiboProfile mesiboProfile, long error) {
    }

    public class MessageContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int SECTION_CELLS = 300;
        public static final int SECTION_HEADER = 100;
        /* access modifiers changed from: private */
        public Context mContext = null;
        public int mCountProfileMatched = 0;
        /* access modifiers changed from: private */
        public ArrayList<MesiboProfile> mDataList = null;
        /* access modifiers changed from: private */
        public UserListFragment mHost;
        private int mBackground = 0;
        private ArrayList<MesiboProfile> mSearchResults = null;
        private SparseBooleanArray mSelectionItems;
        private ArrayList<MesiboProfile> mUsers = null;

        public MessageContactAdapter(Context context, UserListFragment host, ArrayList<MesiboProfile> list, ArrayList<MesiboProfile> searchResults) {
            this.mContext = context;
            this.mHost = host;
            this.mUsers = list;
            this.mSearchResults = searchResults;
            this.mDataList = list;
            this.mSelectionItems = new SparseBooleanArray();
        }

        public void updateData(ArrayList<MesiboProfile> newItems) {
            this.mDataList = newItems;
            Log.e("check", String.valueOf(mDataList.size()));
            notifyDataSetChanged();
        }

        public ArrayList<MesiboProfile> getActiveUserlist() {
            if (UserListFragment.this.mIsMessageSearching.booleanValue()) {
                return this.mSearchResults;
            }
            return this.mUsers;
        }

        public int getItemViewType(int position) {
            if (this.mDataList.get(position).address != null || this.mDataList.get(position).groupid > 0) {
                return SECTION_CELLS;
            }
            return 100;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 100) {
                return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_header_title, parent, false));
            }
            return new SectionCellsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder vh, @SuppressLint({"RecyclerView"}) int position) {
            int imageDrawableId;
            if (vh.getItemViewType() == 100) {
                ((SectionHeaderViewHolder) vh).mSectionTitle.setText(this.mDataList.get(position).getName());
                return;
            }
            int i = position;
            MesiboProfile user = this.mDataList.get(position);
            final SectionCellsViewHolder holder = (SectionCellsViewHolder) vh;
            holder.position = position;
            UserData userdata = UserData.getUserData(user);
            member.add(user);
            Log.e("check", String.valueOf(member.size()));
            userdata.setUser(user);
            userdata.setUserListPosition(position);
            UserData data = userdata;
            holder.mContactsName.setText(data.getUserName());
            if (this.mHost.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                holder.mContactsTime.setVisibility(View.VISIBLE);
                holder.mContactsTime.setText(data.getTime());
            } else {
                holder.mContactsTime.setVisibility(View.GONE);
            }
            if (user.isOnline()) {
                holder.isOnlineDot.setVisibility(View.VISIBLE);
            } else {
                holder.isOnlineDot.setVisibility(View.INVISIBLE);
            }
            Drawable imageDrawable = null;
            int padding = 5;
            MesiboMessage msg = data.getMessage();
            if (msg != null && msg.isDeleted()) {
                imageDrawableId = MesiboConfiguration.DELETED_DRAWABLE;
                imageDrawable = MesiboImages.getDeletedMessageDrawable();
            } else if (msg != null && msg.hasImage()) {
                imageDrawableId = MesiboConfiguration.IMAGE_ICON;
            } else if (msg != null && msg.hasVideo()) {
                imageDrawableId = MesiboConfiguration.VIDEO_ICON;
            } else if (msg != null && msg.hasFile()) {
                imageDrawableId = MesiboConfiguration.ATTACHMENT_ICON;
            } else if (msg != null && msg.isMissedCall() && msg.isVideoCall()) {
                imageDrawableId = MesiboConfiguration.MISSED_VIDEOCALL_DRAWABLE;
                imageDrawable = MesiboImages.getMissedCallDrawable(true);
            } else if (msg != null && msg.isMissedCall() && msg.isVoiceCall()) {
                imageDrawableId = MesiboConfiguration.MISSED_VOICECALL_DRAWABLE;
                imageDrawable = MesiboImages.getMissedCallDrawable(false);
            } else if (msg == null || !msg.hasLocation()) {
                imageDrawableId = 0;
                padding = 0;
            } else {
                imageDrawableId = MesiboConfiguration.LOCATION_ICON;
            }
            boolean typing = data.isTyping();
            if (typing) {
                imageDrawableId = 0;
                padding = 0;
            }
            if (this.mHost.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                if (imageDrawable != null) {
                    holder.mContactsMessage.setCompoundDrawablesWithIntrinsicBounds(imageDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
                } else {
                    holder.mContactsMessage.setCompoundDrawablesWithIntrinsicBounds(imageDrawableId, 0, 0, 0);
                }
                holder.mContactsMessage.setCompoundDrawablePadding(padding);
                if (!typing) {

                    DecodeDataHelper decodeDataHelper = new DecodeDataHelper();
                    Map<String, String> decodedData = decodeDataHelper.decodeData(userdata.getLastMessage());
                    Decoder decoder = new Decoder();
                    Map<String, String> decode = decoder.decodeProfileData(userdata.getLastMessage());

                    String messageType = decodedData.get("messageType");
                    String profileType = decode.get("messageType");

                    if (Objects.equals(messageType, "Post")){
                        holder.mContactsMessage.setText("Shared a Post");
                    }else if(Objects.equals(profileType, "Profile")){
                        holder.mContactsMessage.setText("Shared a Profile");
                    }else {
                        holder.mContactsMessage.setText(userdata.getLastMessage());
                    }
                    holder.mContactsMessage.setTextColor(UserListFragment.this.mMesiboUIOptions.mUserListStatusColor);
                } else {
                    MesiboProfile typingProfile = data.getTypingProfile();
                    String typingText = getActivity().getResources().getString(R.string.typing_text);
                    if (typingProfile != null) {
                        typingText = typingProfile.getName() + " is " + getActivity().getResources().getString(R.string.typing_text);
                    }
                    holder.mContactsMessage.setText(typingText);
                    holder.mContactsMessage.setTextColor(UserListFragment.this.mMesiboUIOptions.mUserListTypingIndicationColor);
                }
            } else {
                holder.mContactsMessage.setText(user.getStatus() != null ? user.getStatus() : "");
            }

            Bitmap b = data.getThumbnail(UserListFragment.this.mLetterTileProvider , getContext());
            new RoundImageDrawable(b);
            holder.mContactsProfile.setImageDrawable(new RoundImageDrawable(b));
            if (this.mHost.mSelectionMode != MesiboUserListFragment.MODE_MESSAGELIST || data.getUnreadCount().intValue() <= 0) {
                holder.mNewMesAlert.setVisibility(View.INVISIBLE);
                holder.mes_alertbg.setVisibility(View.INVISIBLE);
            } else {
                 holder.mNewMesAlert.setVisibility(View.VISIBLE);
                holder.mes_alertbg.setVisibility(View.VISIBLE);
                holder.mNewMesAlert.setText(String.valueOf(data.getUnreadCount()));
            }
            holder.mContactsDeliveryStatus.setVisibility(View.GONE);
            if (!typing && this.mHost.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                holder.mContactsDeliveryStatus.setVisibility(View.VISIBLE);
                int sts = data.getStatus().intValue();
                if (sts == 19 || sts == 18 || sts == 21 || sts == 32 || data.isDeletedMessage()) {
                    holder.mContactsDeliveryStatus.setVisibility(View.GONE);
                } else {
                    holder.mContactsDeliveryStatus.setImageBitmap(MesiboImages.getStatusImage(sts));
                }
            }
            if (UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD || UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP || UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP) {
                if ((this.mDataList.get(position).uiFlags & 16777216) == 16777216) {
                    holder.mHighlightView.setVisibility(View.VISIBLE);
                    this.mHost.showForwardLayout();
                } else {
                    holder.mHighlightView.setVisibility(View.GONE);
                }
            }
            final MesiboProfile mesiboProfile = user;
            holder.mView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD || UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP || UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP) {
                        if ((mesiboProfile.uiFlags & 16777216) == 16777216) {
                            mesiboProfile.uiFlags &= -16777217;
                        } else {
                            mesiboProfile.uiFlags |= 16777216;
                        }
                        MessageContactAdapter.this.notifyDataSetChanged();
                        if (MessageContactAdapter.this.isForwardContactsSelected().booleanValue()) {
                            MessageContactAdapter.this.mHost.showForwardLayout();
                        } else {
                            MessageContactAdapter.this.mHost.hideForwardLayout();
                        }
                    } else if (mesiboProfile.getName() == null || UserListFragment.this.mMesiboUIOptions.createGroupTitle == null || !mesiboProfile.getName().equals(UserListFragment.this.mMesiboUIOptions.createGroupTitle) || UserListFragment.this.mSelectionMode != MesiboUserListFragment.MODE_SELECTCONTACT) {
                        Context context = v.getContext();
                        if (!UserListFragment.this.onClickUser(mesiboProfile.address, mesiboProfile.groupid, MessageContactAdapter.this.mHost.mForwardId)) {
                            MesiboUIManager.launchMessagingActivity(UserListFragment.this.getActivity(), MessageContactAdapter.this.mHost.mForwardId, mesiboProfile.address, mesiboProfile.groupid);
                            MessageContactAdapter.this.mHost.mForwardId = 0;
                            if (UserListFragment.this.mSelectionMode != MesiboUserListFragment.MODE_MESSAGELIST) {
                                UserListFragment.this.getActivity().finish();
                                return;
                            }
                            return;
                        }
                        MessageContactAdapter.this.mHost.mForwardId = 0;
                    } else {
                        MesiboUIManager.launchContactActivity(UserListFragment.this.getActivity(), 0, MesiboUserListFragment.MODE_SELECTGROUP, 0, false, false, (Bundle) null, "");
                        UserListFragment.this.getActivity().finish();
                    }
                }
            });
            final MesiboProfile mesiboProfile2 = user;
            final int i2 = position;
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint({"RestrictedApi"})
                public boolean onLongClick(View v) {
                    if (!(UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT_FORWARD || UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_SELECTGROUP || UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_EDITGROUP || (!TextUtils.isEmpty(UserListFragment.this.mMesiboUIOptions.createGroupTitle) && mesiboProfile2.getName().equalsIgnoreCase(UserListFragment.this.mMesiboUIOptions.createGroupTitle)))) {
                        try {
                            MenuBuilder menuBuilder = new MenuBuilder(UserListFragment.this.getActivity());
                            new MenuInflater(UserListFragment.this.getActivity()).inflate(R.menu.selected_contact, menuBuilder);
                            holder.PopupMenu = new MenuPopupHelper(MessageContactAdapter.this.mContext, menuBuilder, holder.mView);
                            holder.PopupMenu.setForceShowIcon(true);
                            menuBuilder.setCallback(new MenuBuilder.Callback() {
                                public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                                    if (item.getItemId() != R.id.menu_remove) {
                                        return false;
                                    } else {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                        builder1.setMessage(getActivity().getResources().getString(R.string.chat_delete_confirmation));
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton(getActivity().getResources().getString(R.string.ok_text),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //put your code that needed to be executed when okay is clicked
                                                        if (MessageContactAdapter.this.mHost.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                                                            Mesibo.deleteMessages(mesiboProfile2.address, mesiboProfile2.groupid, 0);
                                                            UserData userData = (UserData) mesiboProfile2.other;
                                                            MessageContactAdapter.this.mDataList.remove(i2);
                                                            MessageContactAdapter.this.notifyDataSetChanged();
                                                        } else if (MessageContactAdapter.this.mHost.mSelectionMode == MesiboUserListFragment.MODE_SELECTCONTACT) {
                                                            MessageContactAdapter.this.mDataList.remove(i2);
                                                            MessageContactAdapter.this.notifyDataSetChanged();
                                                        }
                                                        dialog.cancel();
                                                    }
                                                });
                                        builder1.setNegativeButton(getActivity().getResources().getString(R.string.cancel_text),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }

                                    return true;
                                }

                                public void onMenuModeChange(MenuBuilder menu) {
                                }
                            });
                            holder.PopupMenu.show();
                            holder.PopupMenu.show();
                        } catch (Exception e) {
                        }
                    }
                    return true;
                }
            });
        }

        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            if (holder instanceof SectionCellsViewHolder) {
                SectionCellsViewHolder sectionCellsViewHolder = (SectionCellsViewHolder) holder;
            }
        }

        public int getItemCount() {
            UserListFragment.this.handleEmptyUserList(this.mDataList.size());
            return this.mDataList.size();
        }

        public void notifyChangeInData() {
            long unused = UserListFragment.this.mUiUpdateTimestamp = Mesibo.getTimestamp();
            this.mDataList = getActiveUserlist();
            Log.e("123", String.valueOf(this.mDataList.size()));
            notifyDataSetChanged();
        }

        public ArrayList<MesiboProfile> getMessagedUsers() {
            ArrayList<MesiboProfile> messagedUsers = new ArrayList<>();
            for (MesiboProfile user : mAdapter.mDataList) {
                Integer lastMessage = user.getUnreadMessageCount();
                messagedUsers.add(user);
            }

            return messagedUsers;
        }

        public void onResumeAdapter() {
            this.mSearchResults.clear();
            Boolean unused = UserListFragment.this.mIsMessageSearching = false;
            this.mUsers.clear();
            this.mDataList = this.mUsers;
        }

        public Boolean isForwardContactsSelected() {
            boolean retValue = false;
            Iterator<MesiboProfile> it = this.mDataList.iterator();
            while (it.hasNext()) {
                if ((it.next().uiFlags & 16777216) == 16777216) {
                    retValue = true;
                }
            }
            return retValue;
        }


        public  class DecodeDataHelper {
            public  String decodeString(String input, int shift) {
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

            public  Map<String, String> decodeData(String encodedData) {
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


        public  class Decoder {
            public  Map<String, String> decodeProfileData(String encodedData) {
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

            public  String decodeString(String input, int shift) {
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

        public void filter(String text) {
            String unused = UserListFragment.this.mSearchQuery = text;
            this.mCountProfileMatched = 0;
            this.mSearchResults.clear();
            Boolean unused2 = UserListFragment.this.mIsMessageSearching = false;
            if (TextUtils.isEmpty(text)) {
                this.mDataList = this.mUsers;
                return;
            }
            this.mDataList = this.mSearchResults;
            String text2 = text.toLowerCase();
            Iterator<MesiboProfile> it = this.mUsers.iterator();
            while (it.hasNext()) {
                MesiboProfile item = it.next();
                if (item.getName().toLowerCase().contains(text2) || item.getName().equals(getActivity().getResources().getString(R.string.all_users)) || item.getName().equals(UserListFragment.this.mMesiboUIOptions.recentUsersTitle) || item.getName().equals(getActivity().getResources().getString(R.string.group_members))) {
                    this.mSearchResults.add(item);
                }
            }
            if (this.mSearchResults.size() > 0 && UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                MesiboProfile tempUserProfile = new MesiboProfile();
                this.mCountProfileMatched = this.mSearchResults.size();
                tempUserProfile.setName(String.valueOf(this.mSearchResults.size()) + " " + MesiboConfiguration.USERS_STRING_USERLIST_SEARCH);
                this.mSearchResults.add(0, tempUserProfile);
            }
            this.mDataList = this.mSearchResults;
            UserListFragment.this.setEmptyViewText();
            if (UserListFragment.this.mSelectionMode == MesiboUserListFragment.MODE_MESSAGELIST) {
                if (!TextUtils.isEmpty(text2)) {
                    Boolean unused3 = UserListFragment.this.mIsMessageSearching = true;
                    MesiboReadSession rbd = new MesiboReadSession(UserListFragment.this);
                    rbd.setQuery(text2);
                    rbd.read(100);
                }
            }
        }

        public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
            public TextView mSectionTitle = null;

            public SectionHeaderViewHolder(View itemView) {
                super(itemView);
                this.mSectionTitle = (TextView) itemView.findViewById(R.id.section_header);
            }
        }

        public class SectionCellsViewHolder extends RecyclerView.ViewHolder {
            public MenuPopupHelper PopupMenu = null;
            public String mBoundString = null;
            public ImageView mContactsDeliveryStatus = null;
            public EmojiconTextView mContactsMessage = null;
            public TextView mContactsName = null;
            public ImageView mContactsProfile = null;
            public TextView mContactsTime = null;
            public RelativeLayout mHighlightView = null;
            public TextView mNewMesAlert = null;
            public LinearLayout mes_alertbg = null;
            public View mView = null;
            public int position = 0;
            public ImageView isOnlineDot;

            public SectionCellsViewHolder(View view) {
                super(view);
                this.mView = view;
                this.mContactsProfile = (ImageView) view.findViewById(R.id.mes_rv_profile);
                this.mContactsName = (TextView) view.findViewById(R.id.mes_rv_name);
                this.mContactsTime = (TextView) view.findViewById(R.id.mes_rv_date);
                this.mContactsMessage = view.findViewById(R.id.mes_cont_post_or_details);
                this.mContactsDeliveryStatus = (ImageView) view.findViewById(R.id.mes_cont_status);
                this.isOnlineDot = (ImageView) view.findViewById(R.id.isOnlineDot);
                this.mNewMesAlert = (TextView) view.findViewById(R.id.mes_alert);
                this.mes_alertbg = (LinearLayout) view.findViewById(R.id.mes_alertbg);
                this.mHighlightView = (RelativeLayout) view.findViewById(R.id.highlighted_view);
            }
        }
    }
}
