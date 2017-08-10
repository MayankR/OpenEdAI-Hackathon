package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class HomeScanFragment extends Fragment implements View.OnClickListener {
    Camera mCamera;
    CameraPreview mPreview;
    FrameLayout previewFL;
    static final String TAG = "HomeScanFragment";
    Button clickScanButton;

    public HomeScanFragment() {
        // Required empty public constructor
    }

    public static HomeScanFragment newInstance(String param1, String param2) {
        HomeScanFragment fragment = new HomeScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_scan, container, false);

        previewFL = (FrameLayout) v.findViewById(R.id.camera_preview);
        setUpCamera();

        clickScanButton = (Button) v.findViewById(R.id.click_scan);
        clickScanButton.setOnClickListener(this);
        return v;
    }

    void clickPicture() {
        Log.d(TAG, "Clicking pic");
        try {
            mCamera.takePicture(null, null, mPicture);
        }
        catch(RuntimeException e) {
            Log.d(TAG, "Error clicking pic");
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            long epoch = System.currentTimeMillis();
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = epoch + ".jpg";

            File folder = new File(baseDir + "/qreader");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File pictureFile = new File(baseDir + "/qreader/" + fileName);
            Log.d(TAG, "path: " + baseDir + "/qreader/" + fileName);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }


            Bitmap bm = BitmapFactory.decodeFile(baseDir + "/qreader/" + fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String userImgB64 = Base64.encodeToString(b, Base64.DEFAULT);
//            userImgB64 = userImgB64.substring(5);
            userImgB64 = "data:image/jpeg;base64," + userImgB64.trim().replaceAll("[\n\r]", "");
            sendImage(userImgB64);

            mCamera.stopPreview();
            try {
                mCamera.startPreview();
            }
            catch(RuntimeException e) {
                Log.d(TAG, "unable to start preview");
            }
        }
    };

    void sendImage(String fileB64) {
        RequestParams params = new RequestParams();
        params.add("file", fileB64);
        params.add("name", "jpeg");

        Log.d(TAG, "B64 file: " + fileB64.substring(fileB64.length() - 20) + " :done");
        Log.d(TAG, "B64 file length: " + fileB64.length());
        Log.d(TAG, "Params: " + params);

        String url = Utility.OCR_URL;

        Log.d(TAG, "Hitting OCR URL: " + url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(7000);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "Got response success!");
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    Log.d(TAG, obj.toString());
//                    loadReaderActivity(obj.getString("text"));
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
//                loading.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(getContext(), "404 - Not Found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getContext(), "500 - Internal Server Error!", Toast.LENGTH_LONG).show();
                } else if (statusCode == 403) {
                    Toast.makeText(getContext(), "403!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void setUpCamera() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                mCamera = getCameraInstance();

                Camera.Parameters params = mCamera.getParameters();

                params.setJpegQuality(10);
                params.setRotation(90);


                mCamera.setParameters(params);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPreview = new CameraPreview(getContext(), mCamera);
                        previewFL.addView(mPreview);
                    }
                });

            }
        };

        thread.start();
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            int cameraId = 0;
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i;
                    break;
                }
            }

            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.click_scan:
                clickPicture();
                break;
        }
    }
}
