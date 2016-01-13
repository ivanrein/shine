
package edu.bluejack151.Shine.shine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyProfileActivity extends BaseActivity {

    public static final String EXTRA_USER_FULLNAME = "com.shineapptpa.rei.myprofileactivity.fullname";
    // public static final String EXTRA_USER_DOB = "com.shineapptpa.rei.myprofileactivity.dob";
    public static final String EXTRA_USER_GENDER = "com.shineapptpa.rei.myprofileactivity.gender";
    public static final String EXTRA_USER_BIO = "com.shineapptpa.rei.myprofileactivity.bio";
    public static final String EXTRA_USER_SCHOOL = "com.shineapptpa.rei.myprofileactivity.school";
    // public static final String EXTRA_USER_IMAGES = "com.shineapptpa.rei.myprofileactivity.images";
    public static final String EXTRA_USER_EMAIL = "com.shineapptpa.rei.myprofileactivity.email";
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    public TextView mTextViewBio, mTextViewAge, mTextViewUser, mTextViewSchool;
    public EditText editTextBio;
    int container_height, container_width;
    String new_photo_id = "";
    ImageView mImageViewGender;
    ArrayList<Photo> photoList;
    int total_loaded_images = 0;
    ArrayList<GetImages> referenceKeeper;
    JsonObjectRequest imageRequest;
    class GetImages extends AsyncTask<String, Void, Bitmap>{
        boolean running = true;
        @Override
        protected Bitmap doInBackground(String... params) {

            Log.d("size stringnya", params[0].length() + "");

            byte[] decodedString = null;
            decodedString = Base64.decode(params[0], Base64.DEFAULT);


            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
            Log.d("image log", "decode pertama");
            options.inSampleSize = ImageProcessingHelper.calculateInSampleSize(options,
                    container_width,
                    container_height);
            options.inJustDecodeBounds = false;
            Log.d("processing image", "processing image" + total_loaded_images);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
            Log.d("image log", "decode kedua");
            photoList.add(new Photo(params[1]));
            photoList.get(photoList.size()-1).setBitmap(bitmap);
            return bitmap;
        }

        @Override
        protected void onCancelled() {

            super.onCancelled();
            this.running = false;
        }

        @Override
        protected void onPostExecute(Bitmap aVoid) {
            if(!this.isCancelled() && running) {
                Log.d("changing image", total_loaded_images + "");

                ImageView temp = ((ImageView) ((PhotosPagerFragment) mFragmentManager.findFragmentByTag("PAGER"))
                        .getViewPager().findViewWithTag("POSITION" + total_loaded_images));
                if (temp != null)
                    temp.setImageBitmap(aVoid);
                total_loaded_images++;
            }
        }
    }

    public ArrayList<Photo> getPhotoList()
    {
        return this.photoList;
    }

    public Bitmap getPhoto(int position)
    {
        if(photoList.size() == 0 || position > photoList.size()-1)
            return BitmapFactory.decodeResource(getResources(), R.drawable.com_facebook_profile_picture_blank_square);
        else
            return this.photoList.get(position).getBitmap();
    }



    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        container_height = findViewById(R.id.top_container).getMeasuredHeight();
        container_width = findViewById(R.id.top_container).getMeasuredWidth();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setToolbar();
        getSupportActionBar().hide();

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.top_container);
        referenceKeeper = new ArrayList<>();
        initialize();
        bindView();

        RequestQueue q = Volley.newRequestQueue(this);

        String email = (String)getIntent().getSerializableExtra(EXTRA_USER_EMAIL);

        imageRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.laravel_API_url) + "photos?email="+email, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                        	//Jangan lupa, ganti ip address sama structure json nya sesuain sama punya lu,
                        	//ini gw bikin routes sendiri soalnya
                            JSONArray photos= response.getJSONArray("photos");

                            for (int i = 0; i < photos.length(); i++){

                                Log.d("dapet image", photos.getJSONObject(i).getString("id"));
                                GetImages baru = new GetImages();
                                baru.execute(photos.getJSONObject(i).getString("photo"), photos.getJSONObject(i).getString("id"));
                                referenceKeeper.add(baru);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("utoken", CustomPref.getUserAccessToken(getApplicationContext()));
                return map;
            }
        };
        q.add(imageRequest);

        //fetch foto done

        PhotosPagerFragment temp = PhotosPagerFragment.createInstance();
        if(mFragmentManager.getFragments() == null)
            mFragmentManager.beginTransaction()
                    .add(R.id.top_container, temp, "PAGER")
                    .addToBackStack(null)
                    .commit();

    }

    private void initialize()
    {
        mTextViewAge = (TextView) findViewById(R.id.tvAge);
        mTextViewUser = (TextView) findViewById(R.id.tvFullname);
        mTextViewSchool = (TextView) findViewById(R.id.tvSchool);
        mImageViewGender = (ImageView) findViewById(R.id.ivGender);
        mTextViewBio = (TextView) findViewById(R.id.tvBio);
        editTextBio = (EditText) findViewById(R.id.tvBioEdit);
        photoList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        Log.d("MyProfileAct", "MyProfileAct destroyed");
        photoList.clear();
        for (int i = 0; i < referenceKeeper.size(); ++i){
            Log.d("MyProfileAct", "destroying reference of getimages" + i);
            referenceKeeper.get(i).cancel(true);
        }
        imageRequest.cancel();
        super.onDestroy();
    }

    //set data di sini
    public void bindView()
    {
        String fullname =(String) getIntent().getSerializableExtra(EXTRA_USER_FULLNAME);
        //Date dob =(Date) getIntent().getSerializableExtra(EXTRA_USER_DOB);
        String gender =(String) getIntent().getSerializableExtra(EXTRA_USER_GENDER);
        String bio =(String) getIntent().getSerializableExtra(EXTRA_USER_BIO);
        String school =(String) getIntent().getSerializableExtra(EXTRA_USER_SCHOOL);

//        Calendar today = Calendar.getInstance();
//        Integer age = today.get(Calendar.YEAR) - dob.getYear();
//        mTextViewAge.setText(age.toString());

        mTextViewUser.setText(fullname);
        mTextViewSchool.setText(school);
        mImageViewGender.setImageResource(gender.equals("Male") ? R.drawable.gentleman : R.drawable.ladies);
        mTextViewBio.setText(bio);
        editTextBio.setText(bio);
    }

    public static Intent newIntent(Context context, HashMap<String, String> userMap ){
        Intent intent = new Intent(context, MyProfileActivity.class);
        intent.putExtra(EXTRA_USER_BIO, userMap.get(ShineUser.MAP_USER_BIO));
        intent.putExtra(EXTRA_USER_FULLNAME, userMap.get(ShineUser.MAP_USER_NAME));
        intent.putExtra(EXTRA_USER_GENDER, userMap.get(ShineUser.MAP_USER_GENDER));
        intent.putExtra(EXTRA_USER_SCHOOL, userMap.get(ShineUser.MAP_USER_SCHOOL));
        intent.putExtra(EXTRA_USER_EMAIL, userMap.get(ShineUser.MAP_USER_EMAIL));
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(MyProfileActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch(item.getItemId()) {
            case R.id.action_done: {
                //SAVE PROFILE HABIS EDIT DI SINI, SAVE KE API, DLL
                ArrayList<String> deletedPhotosId = new ArrayList<>();

                //ini id yg mau dihapus
                deletedPhotosId = ((EditPhotosFragment)mFragmentManager.findFragmentByTag("EDIT")).getDeletedPhotosId();

                mTextViewBio.setText(editTextBio.getText().toString());
                photoList = ((EditPhotosFragment)mFragmentManager.findFragmentByTag("EDIT")).getPhotoResources();

                HashMap<String, String> bioBaru = new HashMap<String, String>();
                bioBaru.put("bio", editTextBio.getText().toString());
                JSONObject postBio = new JSONObject(bioBaru);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest updateBioRequest = new JsonObjectRequest(Request.Method.POST,
                        getString(R.string.laravel_API_url) + "update", postBio,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(MyProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                                try {
                                    ShineUser.updateCurrentUser(ShineUser.MAP_USER_BIO,
                                            response.getJSONObject("user").getString("bio"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MyProfileActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("utoken", CustomPref.getUserAccessToken(getApplicationContext()));
                        return params;
                    }
                };
                que.add(updateBioRequest);
            }
            break;
            case R.id.action_cancel: {
                editTextBio.setText(mTextViewBio.getText().toString());

            }
            break;
        }

        mTextViewBio.setVisibility(View.VISIBLE);
        editTextBio.setVisibility(View.GONE);

        PhotosPagerFragment fragment = PhotosPagerFragment.createInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.top_container, fragment, "PAGER")
                .commit();

        return super.onOptionsItemSelected(item);
    }

    private void deletePhoto(ArrayList<String> deletedPhotosId) {

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Image", "" + requestCode + " " + resultCode);
        Log.d("Image", "" + EditPhotosFragment.GET_PHOTO + " " + RESULT_OK);
        if (resultCode == RESULT_OK && requestCode == EditPhotosFragment.GET_PHOTO) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = Bitmap.createScaledBitmap(bitmap, container_width, container_height, true);
                photoList.add(new Photo(bitmap));
                savePhoto(bitmap);

                EditPhotosFragment tempFragment = (EditPhotosFragment)mFragmentManager.findFragmentByTag("EDIT");
                tempFragment.refreshPhotos(photoList);


                for (int i = 0; i < 5; i++)
                {
                    ImageView temp = ((ImageView) ((PhotosPagerFragment) mFragmentManager.findFragmentByTag("PAGER"))
                            .getViewPager().findViewWithTag("POSITION" + i));
                    if(temp!=null)
                        temp.setImageBitmap(photoList.get(i).getBitmap());
                }

                Log.d("Image", inputStream.toString());
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        mFragmentManager.popBackStack();
        super.onBackPressed();
    }
    private void savePhoto(final Bitmap bitmap){
        Log.d("MyProfileAct", "Uploading photo");
        RequestQueue qUploadFoto = Volley.newRequestQueue(this);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOutput);
        byte[] b = byteOutput.toByteArray();
        HashMap<String, String> photo = new HashMap<>();

        photo.put("photo", Base64.encodeToString(b, Base64.DEFAULT));
        Log.d("fotonya kak", photo.get("photo"));
        JSONObject photoJson = new JSONObject(photo);
        try {
            Log.d("fotonya json", photoJson.getString("photo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                getString(R.string.laravel_API_url) + "SavePhoto", photoJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("result").equals("success")) {
                                Log.d("MyProfileAct", "Upload foto selesai");
                                Toast.makeText(MyProfileActivity.this,
                                        "Photo successfully uploaded", Toast.LENGTH_SHORT).show();
                                if(photoList != null && photoList.size() != 0) {
                                    new_photo_id = response.getString("photo");
                                    photoList.get(photoList.size() - 1).setPhotoId(new_photo_id);
                                }
                            }
                            else{
                                Toast.makeText(MyProfileActivity.this,
                                        "There were some errors", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyProfileActivity.this, error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("utoken", CustomPref.getUserAccessToken(getApplicationContext()));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(300000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        qUploadFoto.add(request);
    }


}

