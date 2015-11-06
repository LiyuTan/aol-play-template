package com.aol.videonews.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aol.aolon.sdk.player.PlayerFragment;
import com.aol.aolon.sdk.player.PlayerListener;
import com.aol.videonews.Constants;
import com.aol.videonews.Utils;
import com.aol.videonews.api.VideosApi;
import com.aol.videonews.model.Playlist;
import com.aol.videonews.model.PlaylistItem;
import com.aol.videonews.model.Video;

import com.aol.videonews.R;
import com.aol.videonews.ui.swipecard.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements PlayerListener {

    private ProgressBar mProgressBar;
    private SwipeFlingAdapterView mCardsStack;
    private CardsAdapter mCardsAdapter;
    private static int[] sCardImageSize;
    private PlayerFragment mPlayerFragment;
    private FrameLayout mPlayerFragmentContainer;
    private MyCardView.UpdatePositionListener mCardUpdatePositionListener;
    private int mInitLeftMargin, mInitTopMargin;
    private View mSwipeLeftIndicatorView, mSwipeRightIndicatorView;
    private View mActivityRootView;

    private BroadcastReceiver mGetPlaylistDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressBar.setVisibility(View.GONE);

            Playlist playlist = intent.getParcelableExtra(Constants.EXTRA_PLAYLIST);

            if (playlist != null && playlist.items != null && playlist.items.length > 0) {
                ArrayList<PlaylistItem> itemsArrayList = new ArrayList<PlaylistItem>();
                for (PlaylistItem item: playlist.items) {
                    itemsArrayList.add(item);
                }
                mCardsAdapter.items = itemsArrayList;
            } else {
                mCardsAdapter.items = null;
            }

            mCardsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivityRootView = findViewById(android.R.id.content);

        mPlayerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment);
        mPlayerFragment.setIgnoreTouchEvent(true);
        mPlayerFragmentContainer = (FrameLayout) findViewById(R.id.player_fragment_container);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mCardsStack = (SwipeFlingAdapterView) findViewById(R.id.card_stack);
        mCardsAdapter = new CardsAdapter(this);
        mCardsStack.setAdapter(mCardsAdapter);
        mCardsStack.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                mCardsAdapter.items.remove(0);
                mCardsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //makeToast(MainActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //makeToast(MainActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here, for now request the same playlist,
                // when we have a smart playlist which has "infinite" items,
                // we will do proper pagination here.
                Utils.getPlaylistDetail(MainActivity.this, "PL6520");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                mSwipeLeftIndicatorView.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                mSwipeRightIndicatorView.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
            }
        });

        mSwipeLeftIndicatorView = findViewById(R.id.item_swipe_left_indicator);
        mSwipeRightIndicatorView = findViewById(R.id.item_swipe_right_indicator);


        // Optionally add an OnItemClickListener
        mCardsStack.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //makeToast(MainActivity.this, "Clicked!");

//                if (mPlayerFragment != null) {
//                    mPlayerFragment.pauseVideo();
//                }
//
//                View selectedView = mCardsStack.getSelectedView();
//                if (selectedView != null) {
//                    Object tag = selectedView.getTag();
//                    if (tag != null && tag instanceof ViewHolder) {
//                        ViewHolder holder = (ViewHolder) tag;
//                        if (mPlayerFragment != null) {
//                            mPlayerFragment.playVideoWithId(holder.videoId);
//                        }
//                    }
//                }

            }
        });

        mCardUpdatePositionListener = new MyCardView.UpdatePositionListener() {
            @Override
            public void onUpdatePosition(MyCardView view) {
                updatePositionHelper(view, false);
            }

            @Override
            public void onResetPosition(MyCardView view) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sCardImageSize[0], sCardImageSize[1]);
                params.setMargins(mInitLeftMargin, mInitTopMargin, 0, 0);
                mPlayerFragmentContainer.setLayoutParams(params);

                mSwipeLeftIndicatorView.setAlpha(0);
                mSwipeRightIndicatorView.setAlpha(0);
            }
        };

        mCardsStack.setOnActiveCardChangedListener(new SwipeFlingAdapterView.OnActiveCardChangedListener() {
            @Override
            public void onActiveCardChanged(View activeCard, View oldActiveCard) {
                mSwipeLeftIndicatorView.setAlpha(0);
                mSwipeRightIndicatorView.setAlpha(0);
                mPlayerFragmentContainer.setAlpha(0);

                if (oldActiveCard != null && oldActiveCard instanceof MyCardView) {
                    ((MyCardView)oldActiveCard).setUpdatePositionListener(null);
                }

                if (activeCard != null && activeCard instanceof MyCardView) {
                    ((MyCardView)activeCard).setUpdatePositionListener(mCardUpdatePositionListener);
                }

                if (mPlayerFragment != null) {
                    mPlayerFragment.pauseVideo();

                    mPlayerFragmentContainer.setVisibility(View.VISIBLE);

                    updatePositionHelper(activeCard, true);

                    Object tag = activeCard.getTag();
                    if (tag != null && tag instanceof ViewHolder) {

                        final ViewHolder holder = (ViewHolder) tag;

                        final String videoId = holder.videoId;

                        mProgressBar.setVisibility(View.VISIBLE);

                        mPlayerFragment.playVideoWithId(videoId);
                    }
                }
            }
        });

        // For testing, use a hard code playlist ID, this is a "top news" playlist
        Utils.getPlaylistDetail(this, "PL6520");

        mProgressBar.setVisibility(View.VISIBLE);

        Resources res = getResources();

        final int activityHorizontalMargin = res.getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        final int activityVerticalMargin = res.getDimensionPixelSize(R.dimen.activity_vertical_margin);

        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }

        Rect rect= new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        final int cardHorizontalMargin = res.getDimensionPixelSize(R.dimen.card_horizontal_margin);

        sCardImageSize = new int[2];
        if (rect.width() > rect.height()) {
            // landscape mode
            final int cardHeight = rect.height() - statusBarHeight - 2 * activityVerticalMargin;
            sCardImageSize[1] = cardHeight;
            sCardImageSize[0] = cardHeight * 16 / 9;
        } else {
            // portrait mode
            final int cardWidth = rect.width() - 2 * activityHorizontalMargin - 2 * cardHorizontalMargin;
            sCardImageSize[0] = cardWidth;
            sCardImageSize[1] = cardWidth * 9 / 16;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mGetPlaylistDetailReceiver, new IntentFilter(Constants.BROADCAST_GET_PLAYLIST_DETAIL_DONE));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGetPlaylistDetailReceiver);
    }

    public static int[] getCardImageSize() {
        return sCardImageSize;
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder {
        public FrameLayout cardImageVideoContainerView;
        public FrameLayout cardVideoContainerView;
        public TextView cardTitleView;
        public ImageView cardImageView;
        public String videoId;
        public int cardViewContainerViewId;
    }

    public static class CardsAdapter extends BaseAdapter {

        public List<PlaylistItem> items;
        private Context mContext;

        private CardsAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return items != null ? items.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            PlaylistItem item = (items != null && items.size() > position) ?
                    items.get(position) : null;
            if (item == null || !(item instanceof Video)) return null;

            Video video = (Video)item;

            ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.card, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.cardImageVideoContainerView = (FrameLayout) rowView.findViewById(R.id.card_image_video_container);
                viewHolder.cardVideoContainerView = (FrameLayout) rowView.findViewById(R.id.card_video_container);
                viewHolder.cardTitleView = (TextView) rowView.findViewById(R.id.card_title);
                viewHolder.cardImageView = (ImageView) rowView.findViewById(R.id.card_image);
                viewHolder.cardImageVideoContainerView.setLayoutParams(new LinearLayout.LayoutParams(sCardImageSize[0], sCardImageSize[1]));
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.cardViewContainerViewId = generateViewId();
            viewHolder.cardVideoContainerView.setId(viewHolder.cardViewContainerViewId);

            viewHolder.videoId = video.id;

            viewHolder.cardTitleView.setText(video.title);

            final int[] cardImageSize = MainActivity.getCardImageSize();
            final String cardImageUrl = VideosApi.getImageUrl(video.id, cardImageSize[0], cardImageSize[1]);
            Picasso.with(mContext).load(cardImageUrl).into(viewHolder.cardImageView);

            return rowView;
        }
    }

    // implements PlayerListener

    @Override
    public boolean isAdDisabled() {
        return true;
    }

    @Override
    public void onAdStarted() {
        mActivityRootView.setKeepScreenOn(true);
    }

    @Override
    public void onAdFinished() {
        mActivityRootView.setKeepScreenOn(false);
    }

    @Override
    public void onVideoStarted(String var1, int var2) {
        mActivityRootView.setKeepScreenOn(true);

        mProgressBar.setVisibility(View.GONE);

        ValueAnimator anim = ValueAnimator.ofInt(0, 100);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float fraction = animator.getAnimatedFraction();
                mPlayerFragmentContainer.setAlpha(fraction);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationDone();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationDone();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            private void onAnimationDone() {
                mPlayerFragmentContainer.setAlpha(1);
            }
        });
        anim.setDuration(500);
        anim.start();
    }

    @Override
    public void onVideoPaused(String var1, int var2) {
        mActivityRootView.setKeepScreenOn(false);
    }

    @Override
    public void onVideoFinished(String var1, int var2) {
    }

    @Override
    public int getPlayerWidth() {
        return sCardImageSize[0];
    }

    @Override
    public int getPlayerHeight() {
        return sCardImageSize[1];
    }

    @Override
    public void onShowMediaController() {
    }

    @Override
    public void onHideMediaController() {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private void updatePositionHelper(View activeCard, boolean isInit) {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        int[] location = new int[2];
        activeCard.getLocationOnScreen(location);

//        mPlayerFragmentContainer.setRotation(activeCard.getRotation());

        int leftMargin = location[0];
        int topMargin = location[1] - statusBarHeight - getSupportActionBar().getHeight();
        if (isInit) {
            mInitLeftMargin = leftMargin;
            mInitTopMargin = topMargin;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sCardImageSize[0], sCardImageSize[1]);
        params.setMargins(leftMargin, topMargin, 0, 0);
        mPlayerFragmentContainer.setLayoutParams(params);
    }
}
