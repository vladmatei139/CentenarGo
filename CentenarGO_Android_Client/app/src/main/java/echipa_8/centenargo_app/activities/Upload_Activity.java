package echipa_8.centenargo_app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Landmark_Service;
import echipa_8.centenargo_app.services.Upload_Service;

/**
 * Created by Vlad on 08-May-18.
 */

public class Upload_Activity extends AppCompatActivity {

    private Integer mLandmarkId;
    private Button imgsel,upload;
    private ImageView img;
    private String path;
    private final Integer GET_FROM_GALLERY = 1;
    private Upload_Service upload_service;
    private String b64img;
    private TextView title;

    Toolbar mActionBarToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_upload_);
        mLandmarkId = intent.getIntExtra(getString(R.string.landmark_id_key), 0);

        title = findViewById(R.id.inputTitle);
        img = findViewById(R.id.img);
        imgsel = findViewById(R.id.selimg);
        upload = findViewById(R.id.uploadimg);
        upload.setVisibility(View.INVISIBLE);
        upload_service = new Upload_Service(this);

        imgsel.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                GET_FROM_GALLERY));

        upload.setOnClickListener(v -> {
            String titleString = title.getText().toString();
            if(titleString != ""){
                upload_service.execute(mLandmarkId.toString(), b64img, titleString);
                finish();
            }
            else{
                toast("Te rog sa introduci un titlu!");
            }
        });

        mActionBarToolbar = findViewById(R.id.toolbar_upload);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);
        setNavigationBar();
    }

    private void setNavigationBar() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.upload_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.menu_gallery:
                    intent = new Intent(this.getApplicationContext(), Gallery_Activity.class);
                    this.startActivity(intent);
                    return true;

                case R.id.menu_routes:
                    intent = new Intent(this.getApplicationContext(), Routes_Activity.class);
                    this.startActivity(intent);
                    return true;

                case R.id.menu_sign_off:
                    Toast.makeText(this, "You've been logged out", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;

                case R.id.menu_tutorial:
                    // todo: Start Tutorial intent
                    return true;

                case R.id.menu_stats:
                    // todo: Start Statistics intent
                    return true;

                default:
                    return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Bitmap bitmap2 = bitmap.copy(Bitmap.Config.RGB_565, true);
                upload.setVisibility(View.VISIBLE);
                bitmap2 = Bitmap.createScaledBitmap(bitmap2, 1280, 720, true);
                //bitmap2.reconfigure(400,240,Bitmap.Config.RGB_565);
                img.setImageBitmap(bitmap2);
                b64img = getEncoded64ImageStringFromBitmap(bitmap2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void uploadComplete(String response){
        if(null != response){
            toast("Poza a fost incarcata cu succes!");
        } else {
            toast("Incarcare esuata, incearca din nou!");
        }
    }


}
