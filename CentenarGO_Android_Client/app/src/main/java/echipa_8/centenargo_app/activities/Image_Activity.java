package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import echipa_8.centenargo_app.R;

public class Image_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_);
        Intent intent = getIntent();
        String title = intent.getStringExtra("imageTitle");
        String author = intent.getStringExtra("imageAuthor");
        String path = intent.getStringExtra("imagePath");

        FrameLayout layout = findViewById(R.id.image_frame_layout);
        ImageView imageView = findViewById(R.id.image_image);
        Picasso.get()
                .load("http://10.0.2.2:8080/" + path)
                .placeholder(R.drawable.placeholder_image_square)
                .into(imageView);

        ((TextView) findViewById(R.id.image_title)).setText(title);
        ((TextView) findViewById(R.id.image_author)).setText(author);
        LinearLayout textLayout = findViewById(R.id.image_text_layout);
        layout.setOnClickListener(v -> textLayout.setVisibility(textLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
    }

}
