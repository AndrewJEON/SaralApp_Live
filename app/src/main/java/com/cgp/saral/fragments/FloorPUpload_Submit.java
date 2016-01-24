package com.cgp.saral.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cgp.saral.R;
import com.cgp.saral.activity.MainActivity;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.DateProcessor;
import com.cgp.saral.myutils.FileUtility;
import com.cgp.saral.myutils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WeeSync on 17/10/15.
 */
public class FloorPUpload_Submit extends Fragment {


    private  MainActivity act;
    private final String floorUploadLog="floorPlanExp.txt";

    private static final String TAG = FloorPUpload_Submit.class.getSimpleName();
    View view;

    @Bind(R.id.imageViewUpload)
    ImageView imgImage;

    @Bind(R.id.btn_image_cancel)
    AppCompatButton btn_cancel;
    @Bind(R.id.btn_upload_plan)
    AppCompatButton btn_upload;

    @Bind(R.id.progress_bar_upload)
    ProgressBar progressBar;

    String strServerPath="";

   // String strServerPath="http://192.168.1.105/images/upload.php";

    String fileName="";


    Uri uri;

    private String filePath = null;


    private static final String ARG_PAGE_NUMBER = "page_number";

    public FloorPUpload_Submit() {

    }

    public static FloorPUpload_Submit newInstance(int page) {
        FloorPUpload_Submit fragment = new FloorPUpload_Submit();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.floor_upload_submit, container, false);
        ButterKnife.bind(this, view);

       //
        strServerPath =Constants.IMAGE_FILE_UPLOAD_URL;

        fileName=Constants.GLOBAL_USER_CONT_NO+"_" + DateProcessor.getNow() + ".png";

        Bundle b = getArguments();

        if (b != null) {
            filePath = b.getString("filePath");
        }

        Log.e("File Path",""+filePath);

        if (filePath != null && !filePath.equals("")) {
            // Displaying the image or video on the screen
            previewMedia(filePath);
        } else {
            Toast.makeText(getActivity(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utils.isConnectedToInternet(getActivity())) {
                    Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
                    return;

                }
                new UploadFileToServer().execute();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filePath="";
                Fragment ff = getFragmentManager().findFragmentByTag("plan_upload");
                fragTransaction(ff);
            }
        });

        return view;
    }


    private void fragTransaction(Fragment ff)
    {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, ff);
       // transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia(String strPath) {
        // Checking whether captured media is image or video

        Uri uri = Uri.fromFile(new File(strPath));
        File image = new File(uri.getPath());
        imgImage.setVisibility(View.VISIBLE);
        //  vidPreview.setVisibility(View.GONE);
        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 2;




        Bitmap bitmap= BitmapFactory.decodeFile(image.getPath(), options);

        // final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        imgImage.setImageBitmap(bitmap);

    }


    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
           // progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible


            // updating progress bar value
          //  progressBar.setProgress(progress[0]);

            // updating percentage value
        //    Log.e("Percentage Update", "" + String.valueOf(progress[0]));
           // txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFileEntity();
        }



        @SuppressWarnings("deprecation")
        private String uploadFileEntity() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(strServerPath);

           /* try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });*/

            try {

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
             //   entityBuilder.addBinaryBody("uploadfile", file);
               // MultipartEntity entity = new MultiPartEntity();
                File sourceFile = new File(filePath);

                // Adding file data to http body
                //entityBuilder.addPart("image", new FileBody(sourceFile));

                entityBuilder.addBinaryBody("image",sourceFile,ContentType.DEFAULT_BINARY,fileName);

                // Extra parameters if you want to pass to server

                entityBuilder.addPart("na", new StringBody(fileName));


                HttpEntity entity = entityBuilder.build();
                //totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            }catch(Exception ex) {

                Log.e("Exception",""+ex.toString());
            }

           /* } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }*/

            return responseString;

        }

      /*  @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }*/



        String getBoundaryString() {
            return "----------V2ymHFg03ehbqgZCaKO6jy";
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;



            File sourceFile = new File(filePath);
            byte[] bFile = new byte[(int) sourceFile.length()];

            try {
                //convert file into array of bytes
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                fileInputStream.read(bFile);
                fileInputStream.close();

                for (int i = 0; i < bFile.length; i++) {
                    System.out.print((char) bFile[i]);
                }

                System.out.println("Done");
            } catch (Exception e) {
                e.printStackTrace();
            }


            HttpURLConnection hc = null;

            InputStream is = null;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] res = null;

            try {



                //Constants.IMAGE_FILE_UPLOAD_URL

                URL url = new URL(strServerPath);
                hc = (HttpURLConnection) url.openConnection();
                hc.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + getBoundaryString());
                hc.setDoOutput(true);

                hc.setRequestMethod("POST");
                hc.setRequestProperty("file", fileName);
                hc.setRequestProperty("caseid", "1");

                OutputStream dout = hc.getOutputStream();
                dout.write(bFile);
                dout.close();

                int ch;
                is = hc.getInputStream();

                while ((ch = is.read()) != -1) {
                    bos.write(ch);
                }
                res = bos.toByteArray();
            } catch (Exception e) {
                FileUtility.writeexceptionInfile("send method in HttpMultipartRequest class for upload image" + e.toString(),floorUploadLog);
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                        bos = null;
                    } catch (Exception e) {
                        FileUtility.writeexceptionInfile("send method in HttpMultipartRequest class for upload image" + e.toString(),floorUploadLog);
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                        is = null;
                    } catch (Exception e) {
                        FileUtility.writeexceptionInfile("send method in HttpMultipartRequest class for upload image" + e.toString(),floorUploadLog);
                        e.printStackTrace();
                    }
                }
                if (hc != null) {
                    try {
                        hc.disconnect();
                        hc = null;
                    } catch (Exception e) {
                        FileUtility.writeexceptionInfile("send method in HttpMultipartRequest class for upload image" + e.toString(),floorUploadLog);
                        e.printStackTrace();
                    }
                }

            }
            Log.e("Image Upload", " : " + res.toString());
            return res.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            progressBar.setVisibility(View.GONE);
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thank You for uploading your plan. We will revert back to you shortly. Kindly make use of your registered mobile number for all further interactions").setTitle("Upload Response")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        act.instanceMain.selectItemFromDrawer(0);

                       /* FragmentManager fm = getFragmentManager();

                        Fragment f = fm.findFragmentById(R.id.tab);
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.show(f);

                        transaction.commit();*/

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
