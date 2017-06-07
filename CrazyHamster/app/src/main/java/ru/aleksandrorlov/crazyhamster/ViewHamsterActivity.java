package ru.aleksandrorlov.crazyhamster;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 22.05.17.
 */

public class ViewHamsterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewTitle, textViewDescription;
    private ImageView imageViewPhotoHamster;
    private CheckBox chekBoxLikeHamster;

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
        textViewDescription = (TextView)findViewById(R.id.text_view_description);
        imageViewPhotoHamster = (ImageView)findViewById(R.id.image_view_photo_hamster);
        chekBoxLikeHamster = (CheckBox) findViewById(R.id.like_hamster);
    }

    private void setView(){
        textViewTitle.setText(title);
        textViewDescription.setText(description);

        if (likeHamster){
            chekBoxLikeHamster.setChecked(true);
        } else {
            chekBoxLikeHamster.setChecked(false);
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
            return 1;
        } else {
            return 0;
        }
    }

    private void  onClickBehavior(){
        chekBoxLikeHamster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like_hamster:
                if (chekBoxLikeHamster.isChecked()){
                    chekBoxLikeHamster.setChecked(true);
                    likeHamster = true;
                } else {
                    chekBoxLikeHamster.setChecked(false);
                    likeHamster = false;
                }
                selectLikeHamsterToHamsterTable(id, likeHamster);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        provider.setShareIntent(createShareIntent());

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uriImage = Uri.parse(imagePath);
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
