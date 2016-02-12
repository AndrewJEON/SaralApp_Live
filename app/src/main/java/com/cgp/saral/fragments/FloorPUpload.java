package com.cgp.saral.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cgp.saral.R;
import com.cgp.saral.activity.MainActivity;
import com.cgp.saral.bus.ActivityResultBus;
import com.cgp.saral.event.ActivityResultEvent;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.RealPathUtil;
import com.cgp.saral.myutils.Utils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WeeSync on 17/10/15.
 */
public class FloorPUpload extends Fragment {


    private static final String TAG = FloorPUpload.class.getSimpleName();
    View view;

    @Bind(R.id.iv_camera)
    ImageView iv_camera;

    @Bind(R.id.iv_gallery)
    ImageView iv_gallery;


    Context ctx;

    //public static ImageView listing_image;
    public static byte[] byteArray;
    public static String mCurrentPhotoPath;


    // Activity result key for camera
    static final int REQUEST_TAKE_PHOTO = 11111;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;


    public static final int MEDIA_TYPE_IMAGE = 1;


    private static Uri fileUri; // file url to store image/video

    private static String strTag="page_str";

    private String imgDecodableString;

    public static final int RESULT_LOAD_IMG = 10111;

    private static final String ARG_PAGE_NUMBER = "page_number";

    WebView videoView;

    public FloorPUpload() {
        super();

    }
 //   static FloorPUpload fragment;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public static FloorPUpload newInstance(int page) {
        FloorPUpload fragment = new FloorPUpload();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
      //  args.putString(strTag,fileUri.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // super.onCreateView( inflater,  container,
       //          savedInstanceState);

        view = inflater.inflate(R.layout.floor_upload, container, false);
        ButterKnife.bind(this, view);
      //  setRetainInstance(true);
      //  act = (AppCompatActivity) getActivity();
        ctx=getActivity().getApplicationContext();


        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        // Checking camera availability


        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            //finish();
        }

       // setRetainInstance(true);
        videoView =(WebView) view.findViewById(R.id.vid_postvideo);
        videoView.loadUrl(Utils.getFloorPlanVideoUrl());
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.setVisibility(View.VISIBLE);
        videoView.getSettings().setPluginState(WebSettings.PluginState.ON);; //sets MediaController in the video view

        videoView.requestFocus();//give focus to a specific view

        return view;
    }


    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * Start the camera by dispatching a camera intent.
     */
    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        MainActivity activity = (MainActivity)getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub

        outState.putString("photopath", imgDecodableString);


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("photopath")) {
                imgDecodableString = savedInstanceState.getString("photopath");
            }
        }

        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };


    /**
     * The activity returns with the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainActivity activity = (MainActivity) getActivity();

        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                imgDecodableString = activity.getCurrentPhotoPath().toString();
                Log.e("Image Capture",""+imgDecodableString);
                //imgUser.setImageBitmap(decodeFile(selectedImagePath));
            } else if (requestCode == RESULT_LOAD_IMG) {

                if(data !=null)
                {

                   /* String realPath;
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());*/
                    try{
                        imgDecodableString =RealPathUtil.getPath(getActivity(),data.getData());
                        Log.e("Image Gallery",""+imgDecodableString);
                    }catch(Exception ex)
                    {
                       // imgDecodableString =RealPathUtil.getPath(getActivity(),data.getData());
                        Log.e("Image Gallery",""+imgDecodableString +"    "+ex.toString());
                    }


                }

               // imgUser.setImageBitmap(decodeFile(selectedImagePath));
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

            startTransaction();
        }

    }


    public  boolean convertImageUriToFile(Uri imageUri, Activity activity) {
        Cursor cursor = null;
        boolean flag = false;
        try {
            String[] proj = { MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION };
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                CursorLoader loader = new CursorLoader(activity, imageUri,
                        proj, null, null, null);
                cursor = loader.loadInBackground();
            } else {
                cursor = activity.managedQuery(imageUri, proj, null, null, null);
            }
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            while (cursor.moveToNext()) {
                Log.e("file Name: ", cursor.getString(file_ColumnIndex));
                ContentResolver cr = activity.getContentResolver();
                int i = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        BaseColumns._ID + "=" + file_ColumnIndex, null);
                Log.v(TAG, "Number of column deleted : " + i);
                File file = new File(cursor.getString(file_ColumnIndex));
                if (file.exists()) {
                    file.delete();
                    flag = true;
                }

            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }
    /**
     * Creates the image file to which the image must be saved.
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        MainActivity activity = (MainActivity)getActivity();
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        MainActivity activity = (MainActivity)getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }



    /**
     * Scale the photo down and fit it to our image views.
     *
     * "Drastically increases performance" to set images using this technique.
     * Read more:http://developer.android.com/training/camera/photobasics.html
     */
    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }


    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Constants.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }



    public void startTransaction() {


        // ((SaralApplication) getApplication()).trackEvent(MainActivity.this, "MainActivity", "App Flow", "fragment Switching " + str);


        Fragment ff = new FloorPUpload_Submit();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle b = new Bundle();
        b.putString("filePath", imgDecodableString);
        ff.setArguments(b);

       /* getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "File Path" + imgDecodableString, Toast.LENGTH_LONG).show();
            }
        });*/


        Log.e("FilePath", "" + imgDecodableString);
        transaction.replace(R.id.content_frame, ff);
       // transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();


    }

  /*  @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach");

        // Check if parent activity implements our callback interface
        if (activity != null) {
            try {

                Log.v(TAG, "onAttach");
              //  mParentCallback = (CameraActivity) activity;
            }
            catch (ClassCastException e) {
            }
        }else
        {
            Log.v(TAG, "Activity null");
        }
    }*/


   /* @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }*/
}
