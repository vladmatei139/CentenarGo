package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.CheckLike_Service;
import echipa_8.centenargo_app.services.Like_Service;

public class Image_Activity extends AppCompatActivity {

    Button mLikeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_);
        Intent intent = getIntent();
        final Integer id = intent.getIntExtra("imageId", 0);
        String title = intent.getStringExtra("imageTitle");
        String author = intent.getStringExtra("imageAuthor");
        String path = intent.getStringExtra("imagePath");
        String likes = intent.getStringExtra("likes");

        FrameLayout layout = findViewById(R.id.image_frame_layout);
        ImageView imageView = findViewById(R.id.image_image);
        Picasso.get()
                .load("http://10.0.2.2:8080/" + path)
                .placeholder(R.drawable.placeholder_image_square)
                .into(imageView);

        ((TextView) findViewById(R.id.image_title)).setText(title);
        ((TextView) findViewById(R.id.image_author)).setText(author);
        ((TextView) findViewById(R.id.image_likes)).setText(likes);

        mLikeButton = (Button) findViewById(R.id.image_like_button);
        mLikeButton.setVisibility(View.INVISIBLE);
        CheckLike_Service checkLike_service = new CheckLike_Service(this);
        checkLike_service.execute(id.toString());

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like_Service like_service = new Like_Service();
                like_service.execute(id.toString());
            }
        });
        LinearLayout textLayout = findViewById(R.id.image_text_layout);
        layout.setOnClickListener(v -> textLayout.setVisibility(textLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
    }

    public void validateCheck(Boolean o) {
        if (o!=null && !o)
            mLikeButton.setVisibility(View.VISIBLE);
    }
}
