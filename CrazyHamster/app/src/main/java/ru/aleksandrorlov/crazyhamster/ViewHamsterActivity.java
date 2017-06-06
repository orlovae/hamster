package ru.aleksandrorlov.crazyhamster;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 22.05.17.
 */

public class ViewHamsterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = getClass().getSimpleName();

    private TextView textViewTitle, textViewDiscription;
    private ImageView imageViewPhotoHamster, imageViewLikeHamster;

    private ShareActionProvider provider;

    private String title, description, imagePath;
    private int id;
    private boolean likeHamster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hamster);
        initToolbar();
        initIntent();
        initView();
        setView();
        onClickBehavior();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initIntent(){
        id = getIntent().getExtras().getInt("id");
        title = getIntent().getExtras().getString("title");
        description = getIntent().getExtras().getString("description");
        imagePath = getIntent().getExtras().getString("imagePath");
        likeHamster = getIntent().getExtras().getBoolean("likeHamster");
    }

    private void initView(){
        textViewTitle = (TextView)findViewById(R.id.text_view_title);
        textViewDiscription = (TextView)findViewById(R.id.text_view_description);
        imageViewPhotoHamster = (ImageView)findViewById(R.id.image_view_photo_hamster);
        imageViewLikeHamster = (ImageView)findViewById(R.id.image_view_like_hamster);
    }

    private void setView(){
        textViewTitle.setText(title);
        textViewDiscription.setText(description);

        if (likeHamster){
            imageViewLikeHamster.setImageResource(R.drawable.ic_favorite_black_18dp);
        } else {
            imageViewLikeHamster.setImageResource(R.drawable.ic_favorite_border_black_18dp);
        }

        if (imagePath != null) {
            Picasso.with(this)
                    .load(Uri.fromFile(new File(imagePath)))
                    .resize(getWidth(), 0)
                    .placeholder(R.drawable.progress_animation)
                    .into(imageViewPhotoHamster);
        }
    }

    private int getWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void selectLikeHamsterToHamsterTable(int id, boolean likeHamster){
        int intLikeHamster = castBooleanToInt(likeHamster);
        ContentValues cv = new ContentValues();
        cv.put(Contract.Hamster.COLUMN_FAVORITE, intLikeHamster);

        String selection = Contract.Hamster.COLUMN_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        getContentResolver().update(Contract.Hamster.CONTENT_URI, cv, selection, selectionArgs);
    }

    private int castBooleanToInt(boolean likeHamster) {
        if (likeHamster) {
            return 0;
        } else {
            return 1;
        }
    }

    private void  onClickBehavior(){
        imageViewLikeHamster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_like_hamster:
                Log.d(LOG_TAG, "imageViewLikeHamster = " + imageViewLikeHamster.getDrawable());
                Log.d(LOG_TAG, "ic_favorite_black_18dp = " + R.drawable.ic_favorite_black_18dp);
                Log.d(LOG_TAG, "ic_favorite_border_black_18dp = " + R.drawable.ic_favorite_border_black_18dp);

                if (imageViewLikeHamster.getDrawable().getConstantState().equals
                        (ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_favorite_border_black_18dp).getConstantState())){
                    imageViewLikeHamster.setImageResource(R.drawable.ic_favorite_black_18dp);
                } else {
                    imageViewLikeHamster.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                }
                selectLikeHamsterToHamsterTable(id, likeHamster);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        provider.setShareIntent(createShareIntent());

        return true;
    }

    private Intent createShareIntent() {
        Log.d(LOG_TAG, "createShareIntent");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uriImage = Uri.parse(imagePath);
        Log.d(LOG_TAG, "uriImage = " + uriImage.toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_about:
                Intent intent = new Intent(ViewHamsterActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
