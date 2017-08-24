package com.prakhar2_mayank.questioningreader;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = "HomeActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    final static int MY_PERMISSIONS_REQUEST_CAMERA = 23;
//    ArrayList<Uri> uriList;

    HashMap<Card, String> cardAnswerHashMap;

    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardAnswerHashMap = new HashMap<>();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1,false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                toolbar,R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view) ;
        mNavigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "Checking permission");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        this.getSupportActionBar().hide();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch(item.getItemId()) {
            case R.id.nav_item_flash_card:
                //Toast.makeText(this, "Load flash card activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, FlashCardsActivity.class);
                startActivity(intent);
                break;
//            case R.id.nav_item_report:
//                Toast.makeText(this, "Load report activity", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.nav_item_home:
                //Toast.makeText(this, "Load home activity", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getApplication(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void processFile(Uri uri) {
        try {
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(uri));

            SharedPreferences myPrefs = getSharedPreferences("HollaMan", 0);
            SharedPreferences.Editor myPrefsEdit = myPrefs.edit();
            Set<String> recentFileSet = myPrefs.getStringSet("recent_files", new ArraySet<String>());
            Set<String> recentFileSetCopy = new ArraySet<String>(recentFileSet);
            recentFileSetCopy.add(uri.toString());
            myPrefsEdit.remove("recent_files");
            myPrefsEdit.putStringSet("recent_files", recentFileSetCopy);
            myPrefsEdit.commit();
            //Toast.makeText(this, "Added to recent file list", Toast.LENGTH_SHORT).show();

            String content = "error loading file...";
            if(type.equals("pdf")) {
                //Toast.makeText(this, "Opened PDF", Toast.LENGTH_SHORT).show();
                InputStream iStream = getContentResolver().openInputStream(uri);
                Log.d(TAG, "getting bytes");
                byte[] inputData = getBytes(iStream);
                Log.d(TAG, "got bytes");
                String b64 = Base64.encodeToString(inputData, Base64.DEFAULT);
                uploadPDF(b64);
            }
            else {
                content = FileReader.readTextFromUri(this, uri);
                content = "<div style='text-align: justify; font-family: Verdana, Geneva, sans-serif; margin-top: 30px; margin-bottom: 200px; word-wrap: break-word; margin-right: 10px; margin-left: 10px;'>" + content + "</div>";
                loadReaderActivity(content);
            }


        } catch(IOException e) {
            Toast.makeText(this, "Error reading file contents", Toast.LENGTH_SHORT).show();
        }
    }

    void loadReaderActivity(String content) {
        Intent it = new Intent(this, ReaderActivity.class);
        it.putExtra(Utility.DOCUMENT_CONTENT_MESSAGE, content);
        Log.d(TAG, content);
        startActivity(it);
        ReaderActivity.resetChatBot();
    }

    void uploadPDF(String fileB64) {
        RequestParams params = new RequestParams();
        params.add("file", fileB64);
        params.add("name", "pdf");

        Log.d(TAG, "B64 file: " + fileB64 + " :done");
        Log.d(TAG, "B64 file length: " + fileB64.length());
        Log.d(TAG, "Params: " + params);

        String url = Utility.CONVERT_PDF_URL;

        Log.d(TAG, "Hitting pdf convert URL: " + url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);
        final ProgressDialog loading = ProgressDialog.show(this, "Reading the PDF...", "Please wait...", false, false);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "Got response success!");
                loading.dismiss();
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    Log.d(TAG, obj.toString());
                    loadReaderActivity(obj.getString("text"));
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
                loading.dismiss();
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

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == FileReader.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                //Toast.makeText(this, "Uri: " + uri.toString(), Toast.LENGTH_SHORT).show();
                processFile(uri);
            }
        }
    }


    private void rotateCard(final View thumbView, final CardView expandedImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        // TODO: BAD HACK NEED TO REMOVE
        while (mCurrentAnimator != null) {
            return;
        }

        AnimatorSet set = new AnimatorSet();

        thumbView.setAlpha(1);
        expandedImageView.setRotationY(-180);
        set.play(ObjectAnimator.ofFloat(thumbView, View.ROTATION_Y, 0, -180)).with(ObjectAnimator.ofFloat(thumbView, View.ALPHA, 1, 0)).with(ObjectAnimator.ofFloat(expandedImageView, View.ALPHA, 0, 1)).with(ObjectAnimator.ofFloat(expandedImageView, View.ROTATION_Y, -180, -360));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setRotationY(-180);
                thumbView.setAlpha(0);
                thumbView.setVisibility(View.INVISIBLE);
                expandedImageView.setRotationY(0);
                expandedImageView.setAlpha(1);
                expandedImageView.setVisibility(View.VISIBLE);
            }
        });
        set.start();
        mCurrentAnimator = set;

    }

    private void rotateCardRev(final View thumbView, final CardView expandedImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        // TODO: BAD HACK NEED TO REMOVE
        if (mCurrentAnimator != null) {
            return;
        }

        AnimatorSet set = new AnimatorSet();

        expandedImageView.setRotationY(-180);
        thumbView.setAlpha(1);
        set.play(ObjectAnimator.ofFloat(thumbView, View.ROTATION_Y, 0, 180)).with(ObjectAnimator.ofFloat(thumbView, View.ALPHA, 1, 0)).with(ObjectAnimator.ofFloat(expandedImageView, View.ALPHA, 0, 1)).with(ObjectAnimator.ofFloat(expandedImageView, View.ROTATION_Y, -180, 0));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setRotationY(180);
                thumbView.setAlpha(0);
                thumbView.setVisibility(View.INVISIBLE);
                expandedImageView.setRotationY(0);
                expandedImageView.setAlpha(1);
                expandedImageView.setVisibility(View.VISIBLE);
            }
        });
        set.start();
        mCurrentAnimator = set;

    }

    public void cardFlipper(View view) {
        CardView cardView = (CardView) view.getParent().getParent().getParent().getParent().getParent();
        CardView cardView1 = (CardView) ((FrameLayout) (cardView.getParent())).findViewById(R.id.list_cardId1);
        cardView1.setVisibility(View.VISIBLE);
        //cardView.setVisibility(View.INVISIBLE);
        if (cardView1.getCard() == null) {
            Card card = new Card(this);
            card.setTitle("\n\n"+cardAnswerHashMap.get(cardView.getCard()));
            cardView1.setCard(card);
        }
        cardView1.findViewById(R.id.color_strip).setBackgroundResource(R.color.red);
        rotateCard(cardView, cardView1);
//        cardView.setVisibility(View.INVISIBLE);
    }
    public void undoCardFlipper(View view) {
        CardView cardView = (CardView) view.getParent().getParent().getParent().getParent().getParent();
        CardView cardView1 = (CardView) ((FrameLayout) (cardView.getParent())).findViewById(R.id.list_cardId);
        cardView1.setVisibility(View.VISIBLE);
        //cardView.setVisibility(View.INVISIBLE);
        rotateCardRev(cardView, cardView1);
//        cardView.setVisibility(View.INVISIBLE);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return HomeWebFragment.newInstance();
                case 1:
                    return HomeFileFragment.newInstance();
                case 2:
                    return HomeScanFragment.newInstance("fp1", "p2");
            }
            return HomeWebFragment.newInstance();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "REVISE";
                case 1:
                    return "READ";
                case 2:
                    return "SCAN";
            }
            return null;
        }
    }
}
