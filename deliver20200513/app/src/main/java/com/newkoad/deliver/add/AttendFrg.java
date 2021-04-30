package com.newkoad.deliver.add;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;
import com.newkoad.deliver.common.LoadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class AttendFrg extends Fragment {

    private static final String TAG ="MYPAGE" ;
    private MainApp mainApp ;
    private Context context;
    View v;




    ImageView driverImgview;
    Bitmap driverBitmap, newBitmap;
    Button chooseBtn, uploadBtn, imageDelBtn;
    RequestQueue queue;

    String URL = "";
    final int CODE_GALLERY_REQUEST = 999;
    Handler handler = new Handler();



    String imageUrl;
    TextView nameTv, idTv, phoneTv, emailTv, joinDateTv;



    public AttendFrg() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getActivity().getApplication();
        URL = mainApp.getRootURL() + "/image/_post2.php";
        queue= Volley.newRequestQueue(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_my_page,container,false);





        driverImgview= (ImageView) v.findViewById(R.id.driver_imgview);
        nameTv =(TextView) v.findViewById(R.id.name_tv);
        idTv =(TextView) v.findViewById(R.id.id_tv);
        phoneTv =(TextView) v.findViewById(R.id.phone_tv);
        emailTv =(TextView) v.findViewById(R.id.email_tv);
        joinDateTv =(TextView) v.findViewById(R.id.join_date_tv);


        getData();


        return v;
    }





    public void getData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject json = new JSONObject(response);

                    imageUrl = json.getString("image_url");

                    nameTv.setText(json.getString("name"));
                    idTv.setText(json.getString("id"));
                    phoneTv.setText(json.getString("phone"));
                    emailTv.setText(json.getString("email"));
                    joinDateTv.setText(json.getString("join_date"));


                    String imageUrl = mainApp.getRootURL() + "/_images/rider/rider2.jpg";
                    LoadImage loadImage = new LoadImage(imageUrl);
                    Bitmap bitmap = loadImage.getBitmap();
                    driverImgview.setImageBitmap(bitmap);

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        DataListRequest dataListRequest = new DataListRequest(responseListener);
        dataListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(dataListRequest);
    }


    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/my/mypage.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST) {

            if (resultCode == RESULT_OK) {
                try {

                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    newBitmap = BitmapFactory.decodeStream(in);

                    in.close();

                    driverBitmap = resizeBitmapImage(newBitmap, 200);

                    driverImgview.setImageBitmap(driverBitmap);

                    uploadImages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
    {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height){
            if(maxResolution < width){
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }



    private String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        final String temp = Base64.encodeToString(b, Base64.DEFAULT);

        return temp;

    }



    private void uploadImages(){
        final String imageString = getStringImage(driverBitmap);
        final ProgressDialog progressDialog = new ProgressDialog( getActivity());
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject json = new JSONObject(response);

                            progressDialog.hide();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.hide();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "rider1");
                params.put("image",imageString);

                return params;
            }

        };
        stringRequest.setRetryPolicy( new DefaultRetryPolicy(200*30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);


    }
}
