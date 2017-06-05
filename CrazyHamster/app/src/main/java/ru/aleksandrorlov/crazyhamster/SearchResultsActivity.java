package ru.aleksandrorlov.crazyhamster;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ru.aleksandrorlov.crazyhamster.adapter.RecyclerViewAllHamsterAdapter;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 03.06.17.
 */

public class SearchResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = getClass().getSimpleName();
    private RecyclerView recyclerViewAllHamster;
    private RecyclerViewAllHamsterAdapter adapter;
    private int height, width;
    private String query = "";

    private int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initToolbar();
        initImageSize();
        handleIntent(getIntent());
        initLoader();
        initViews();
        initRecyclerView();
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
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    private void initImageSize(){
        DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.heightPixels;
        height = metrics.widthPixels;

        boolean isLandscape = width > height;

        if (!isLandscape) {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
    }

    private void initViews(){
        recyclerViewAllHamster = (RecyclerView)findViewById(R.id.recycler_view_all_hamster);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem share = menu.findItem(R.id.menu_item_share);
        share.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                startActivity(new Intent(SearchResultsActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RecyclerViewAllHamsterAdapter(this, null, width, height);
        recyclerViewAllHamster.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerViewAllHamster.setAdapter(adapter);
        recyclerViewAllHamster.setLayoutManager(layoutManager);
    }

    private void initLoader() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = Contract.Hamster.COLUMN_TITLE
                + " LIKE ? OR "
                + Contract.Hamster.COLUMN_DESCRIPTION
                + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        return new CursorLoader(this, Contract.Hamster.CONTENT_URI, null,
                selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
