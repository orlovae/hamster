package ru.aleksandrorlov.crazyhamster;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 22.05.17.
 */

public class ViewHamsterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = getClass().getSimpleName();

    private TextView textViewTitle, textViewDiscription, textViewLikeHamster;
    private ImageView imageViewPhotoHamster, imageViewBack, imageViewShare;

    private String title, description, imagePath;
    private int id, width;
    private boolean likeHamster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hamster);
        initIntent();
        initView();
        setView();
        onClickBehavior();
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
        imageViewBack = (ImageView)findViewById(R.id.image_view_back);
        textViewLikeHamster = (TextView)findViewById(R.id.text_view_like_hamster);
        imageViewShare = (ImageView)findViewById(R.id.image_view_share);
    }

    private void setView(){
        textViewTitle.setText(title);
        textViewDiscription.setText(description);

        if (likeHamster){
            textViewLikeHamster.setText(getString(R.string.select_like_hamster));
        } else {
            textViewLikeHamster.setText(getString(R.string.un_select_like_hamster));
        }

        if (imagePath != null) {
            Picasso.with(this)
                    .load(imagePath)
                    .resize(getWidth(), 0)
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
        imageViewBack.setOnClickListener(this);
        textViewLikeHamster.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_like_hamster:
                if (textViewLikeHamster.getText().toString().equals
                        (getString(R.string.un_select_like_hamster))){
                    textViewLikeHamster.setText(getString(R.string.select_like_hamster));
                } else {
                    textViewLikeHamster.setText(getString(R.string.un_select_like_hamster));
                }
                selectLikeHamsterToHamsterTable(id, likeHamster);
                break;
            case R.id.image_view_share:
                break;
        }
    }
}
