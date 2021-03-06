package ru.aleksandrorlov.crazyhamster.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.adapter.RecyclerViewAllHamsterAdapter;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 19.05.17.
 */

public class TabFragmentLikeHamster extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerViewLikeHamster;
    private RecyclerViewAllHamsterAdapter adapter;
    private int height, width;

    private int LOADER_ID = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.heightPixels;
        height = metrics.widthPixels;

        boolean isLandscape = width > height;

        if (!isLandscape) {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_tab_all_hamster , container, false);

        initView(view);

        initRecyclerView();

        return view;
    }

    private void initView(View view){
        recyclerViewLikeHamster = (RecyclerView)view.findViewById(R.id.recycler_view_all_hamster);
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RecyclerViewAllHamsterAdapter(getActivity(), null, width, height);
        recyclerViewLikeHamster.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerViewLikeHamster.setAdapter(adapter);
        recyclerViewLikeHamster.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = Contract.Hamster.COLUMN_FAVORITE + " = ?";
        String[] selectionArgs = {Integer.toString(1)};

        return new CursorLoader(getActivity(),
                Contract.Hamster.CONTENT_URI, null, selection, selectionArgs, null);
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
