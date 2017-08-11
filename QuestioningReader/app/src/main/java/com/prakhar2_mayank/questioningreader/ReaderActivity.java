package com.prakhar2_mayank.questioningreader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ReaderActivity extends AppCompatActivity implements View.OnScrollChangeListener, View.OnClickListener {
    WebView contentWV;
    ScrollView contentSV;
    String content;

    private static String TAG = "ReaderActivity";
    boolean qMode = false;
    FloatingActionButton questionFab, toggleFab;
    String contentRead = "";
    int startScroll, curScroll;
    ChatBot chatBot = null;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    private ReaderActivity currObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        currObject = this;

        setContentView(R.layout.activity_reader);
        qMode = false;
        Intent it = getIntent();
        content = it.getStringExtra(Utility.DOCUMENT_CONTENT_MESSAGE);

        contentWV = (WebView) findViewById(R.id.document_content);
        contentWV.loadData(content, "text/html; charset=utf-8", "utf-8");

        contentSV = (ScrollView) findViewById(R.id.content_scroll);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(this, "Scroll listener enabled", Toast.LENGTH_SHORT).show();
            contentSV.setOnScrollChangeListener(this);
        }
        else {
            Toast.makeText(this, "Scroll listener disabled", Toast.LENGTH_SHORT).show();
        }

        questionFab = (FloatingActionButton) findViewById(R.id.fab);
        questionFab.setOnClickListener(this);

        toggleFab = (FloatingActionButton) findViewById(R.id.toggle_menu_item);
        toggleFab.setOnClickListener(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        startReading();
    }

    void handleQuestionResponse(JSONObject response) {
        if (chatBot == null) {
            chatBot = new ChatBot(currObject);
        }
        chatBot.addQuestionsToQueue(response);
    }

    void startReading() {
        Log.d(TAG, "Start Reading in qMode!");
        startScroll = curScroll;
        qMode = true;
    }

    void getQuestions(String text) {
        RequestParams params = new RequestParams();
//        params.add("text", text);

        String url = Utility.QUESTION_URL + "error";
        try {
            url = Utility.QUESTION_URL + URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Unable to get question", Toast.LENGTH_SHORT).show();
        }

        Log.d("SA", "Hiting question URL: " + url + " with text: " + text);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    Log.d(TAG, "Response: " + obj);
                    handleQuestionResponse(obj);
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
//                loading.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(getApplication(), "404 - Not Found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplication(), "500 - Internal Server Error!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getApplication(), "403!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), throwable.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_q_toggle:
                if(qMode){
                    item.setTitle("Q Mode off");
                    qMode = false;
                } else {
                    item.setTitle("Q Mode on");
                    qMode = true;
                    startReading();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Log.d(TAG, "scrollx: " + scrollX + " oldscrollx: " + oldScrollX);
        Log.d(TAG, "scrolly: " + scrollY + " oldscrolly: " + oldScrollY);
        curScroll = scrollY;

        Log.d(TAG, "getting read content " + qMode);
        if(qMode) {
            int totalHeight = contentSV.getChildAt(0).getHeight();
            int totalContentLength = content.length();
            Log.d(TAG, "Content length: " + totalContentLength);

            double startPct = (double) startScroll / (double) totalHeight;
            startPct += 0.0005;
            double endPct = (double) curScroll / (double) totalHeight;
            endPct -= 0.0005;

            Log.d(TAG, "start: " + startPct + "end: " + endPct);
            if(startPct >= 1) startPct = 0.96;
            if(endPct < startPct) endPct = startPct;

            contentRead = content.substring((int) (startPct * totalContentLength),
                    (int) (endPct * totalContentLength));

            contentRead = Html.fromHtml(contentRead).toString();
            Log.d(TAG, "Content Read: " + contentRead);
            if(contentRead.length() > 500) {
                getQuestions(contentRead);
                startScroll = curScroll;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab:
                if (chatBot == null)
                    chatBot = new ChatBot(currObject);
                zoomImageFromThumb(questionFab, R.id.chat_expanded);
                break;
            case R.id.toggle_menu_item:
                if(qMode){
                    toggleFab.setLabelText("Turn On");
                    qMode = false;
                } else {
                    toggleFab.setLabelText("Turn Off");
                    qMode = true;
                    startReading();
                }
                break;
        }
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final View expandedImageView = findViewById(
                imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
