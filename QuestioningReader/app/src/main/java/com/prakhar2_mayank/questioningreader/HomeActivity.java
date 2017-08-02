package com.prakhar2_mayank.questioningreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = "HomeActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                toolbar,R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view) ;
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch(item.getItemId()) {
            case R.id.nav_item_flash_card:
                Toast.makeText(this, "Load flash card activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, FlashCardsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item_report:
                Toast.makeText(this, "Load report activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_home:
                Toast.makeText(this, "Load home activity", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplication(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

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

            String content = "error loading file...";
            if(type.equals("pdf")) {
                Toast.makeText(this, "Opened PDF", Toast.LENGTH_SHORT).show();
                InputStream iStream =   getContentResolver().openInputStream(uri);
                Log.d(TAG, "getting bytes");
                byte[] inputData = getBytes(iStream);
                Log.d(TAG, "got bytes");
                String b64 = Base64.encodeToString(inputData, Base64.DEFAULT);
                uploadPDF(b64);
            }
            else {
                content = FileReader.readTextFromUri(this, uri);
                loadReaderActivity(content);
            }


        } catch(IOException e) {
            Toast.makeText(this, "Error reading file contents :(", Toast.LENGTH_SHORT).show();
        }
    }

    void loadReaderActivity(String content) {
        Intent it = new Intent(this, ReaderActivity.class);
        it.putExtra(Utility.DOCUMENT_CONTENT_MESSAGE, content);
        Log.d(TAG, content);
        startActivity(it);
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
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "Got response success!");
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
                Toast.makeText(this, "Uri: " + uri.toString(), Toast.LENGTH_SHORT).show();
                processFile(uri);
            }
        }
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
                    return "WEB";
                case 1:
                    return "FILE";
                case 2:
                    return "SCAN";
            }
            return null;
        }
    }
}
