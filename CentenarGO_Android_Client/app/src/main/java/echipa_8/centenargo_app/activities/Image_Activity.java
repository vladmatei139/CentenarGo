package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.ImageService;
import echipa_8.centenargo_app.utilities.Image;

public class Image_Activity extends AppCompatActivity {

    private ArrayList<Image> images;
    private int position;
    private Swipe swipe;
    private ImageService imageService;

    private FrameLayout layout;
    private ImageView imageView;
    private LinearLayout textLayout;
    private TextView titleView;
    private TextView authorView;
    private TextView likesView;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_);
        Intent intent = getIntent();
        images = intent.getParcelableArrayListExtra("images");
        position = intent.getIntExtra("position", 0);
        Image image = images.get(position);

        imageService = new ImageService(this, image.getId());
        layout = findViewById(R.id.image_frame_layout);
        imageView = findViewById(R.id.image_image);
        textLayout = findViewById(R.id.image_text_layout);
        titleView = findViewById(R.id.image_title);
        authorView = findViewById(R.id.image_author);
        likesView = findViewById(R.id.image_likes);

        loadImage();

        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {
            @Override
            public boolean onSwipedLeft(final MotionEvent event) {
                position = (position + 1) % images.size();
                loadImage();
                return false;
            }
            @Override
            public boolean onSwipedRight(final MotionEvent event) {
                position = position - 1;
                if (position < 0) {
                    position = images.size() - 1;
                }
                loadImage();
                return false;
            }
        });

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                textLayout.setVisibility(textLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                return true;
            }
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                imageService.like(image.getId(), o ->
                    imageService.getLikes(image.getId(), obj ->
                            likesView.setText(getString(R.string.imageLikesSuffix, obj.optInt("likes", 0)))));
                return true;
            }
        });
    }

    private void loadImage() {
        Image image = images.get(position);
        Picasso.get()
                .load("http://10.0.2.2:8080/" + image.getPath())
                .error(R.drawable.placeholder_image_square)
                .placeholder(R.drawable.placeholder_image_square)
                .into(imageView);
        imageService.setLiked(image.getId());
        titleView.setText(image.getTitle());
        authorView.setText(getString(R.string.imageAuthorPrefix, image.getUsername()));
        likesView.setText("");
        imageService.getLikes(image.getId(), obj ->
                likesView.setText(getString(R.string.imageLikesSuffix, obj.optInt("likes", 0))));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}
