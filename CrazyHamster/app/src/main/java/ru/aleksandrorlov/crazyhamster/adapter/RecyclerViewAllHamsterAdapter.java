package ru.aleksandrorlov.crazyhamster.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 20.05.17.
 */

public class RecyclerViewAllHamsterAdapter extends
        RecyclerView.Adapter<RecyclerViewAllHamsterAdapter.ViewHolder>{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context context;

    private Cursor dataCursor;

    public RecyclerViewAllHamsterAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.dataCursor = cursor;
    }

    public Cursor swapCursor (Cursor cursor){
        if (dataCursor == cursor){
            return null;
        }

        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "Start onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item_all_hamster_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(LOG_TAG, "Start onBindViewHolder");

        dataCursor.moveToPosition(position);

        int idColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_ID);
        int titleColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_TITLE);
        int likeHamsterColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_FAVORITE);

        int id = dataCursor.getInt(idColIndex);
        String titleFromCursor = dataCursor.getString(titleColIndex);
        int likeHamstere = dataCursor.getInt(likeHamsterColIndex);
        boolean likeHamsterFromCursor = castIntToBoolean(likeHamstere);

        holder.textViewTitle.setText(titleFromCursor);
        if (likeHamsterFromCursor){
            holder.textViewLikeHamster.setText(context.getString(R.string.select_like_hamster));
        } else {
            holder.textViewLikeHamster.setText(context.getString(R.string.un_select_like_hamster));
        }
        holder.textViewLikeHamster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textViewLikeHamster.getText().toString().equals
                        (context.getString(R.string.un_select_like_hamster))){
                    holder.textViewLikeHamster.setText(context.getString(R.string.select_like_hamster));
                } else {
                    holder.textViewLikeHamster.setText(context.getString(R.string.un_select_like_hamster));
                }
                selectLikeHamsterToHamsterTable(id, likeHamsterFromCursor);
            }
        });
    }

    private void selectLikeHamsterToHamsterTable(int id, boolean likeHamster){
        int intLikeHamster = castBooleanToInt(likeHamster);
        ContentValues cv = new ContentValues();
        cv.put(Contract.Hamster.COLUMN_FAVORITE, intLikeHamster);

        String selection = Contract.Hamster.COLUMN_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        context.getContentResolver().update(Contract.Hamster.CONTENT_URI, cv, selection,
                selectionArgs);
    }

    private int castBooleanToInt(boolean likeHamster) {
        if (likeHamster) {
            return 0;
        } else {
            return 1;
        }
    }

    private boolean castIntToBoolean(int likeHamsterFromCursor){
        if (likeHamsterFromCursor == 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "Start getItemCount");
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewLikeHamster;


        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView){
            Log.d(LOG_TAG, "Start initView");
            textViewTitle = (TextView)itemView.findViewById(R.id.text_view_title);
            textViewLikeHamster = (TextView)itemView.findViewById(R.id.text_view_like_hamster);

        }
    }
}
