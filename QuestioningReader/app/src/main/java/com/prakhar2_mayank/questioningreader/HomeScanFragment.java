package com.prakhar2_mayank.questioningreader;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;


public class HomeScanFragment extends Fragment {
    Camera mCamera;
    CameraPreview mPreview;

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
        mCamera = getCameraInstance();

        Camera.Parameters params = mCamera.getParameters();

        params.setJpegQuality(10);
        params.setRotation(270);


        mCamera.setParameters(params);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getContext(), mCamera);
        FrameLayout preview = (FrameLayout) v.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        return v;
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

}
