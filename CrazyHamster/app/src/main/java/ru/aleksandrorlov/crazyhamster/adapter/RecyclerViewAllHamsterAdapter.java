package ru.aleksandrorlov.crazyhamster.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 20.05.17.
 */

public class RecyclerViewAllHamsterAdapter extends
        RecyclerView.Adapter<RecyclerViewAllHamsterAdapter.ViewHolder> implements Target{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context context;

    private Cursor dataCursor;

    String imageURL = "";

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
//        Log.d(LOG_TAG, "Start onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item_all_hamster_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Log.d(LOG_TAG, "Start onBindViewHolder");

        dataCursor.moveToPosition(position);

        int idColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_ID);
        int titleColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_TITLE);
        int imageURLColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_IMAGE_URL);
        int imagePathColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_IMAGE_PATH);
        int likeHamsterColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_FAVORITE);

        int id = dataCursor.getInt(idColIndex);
        String titleFromCursor = dataCursor.getString(titleColIndex);
        imageURL = dataCursor.getString(imageURLColIndex);


//        String imagePath = dataCursor.getString(imagePathColIndex);

        int likeHamster = dataCursor.getInt(likeHamsterColIndex);
        boolean likeHamsterFromCursor = castIntToBoolean(likeHamster);

//        Picasso.with(context)
//                .load(imageURL)
//                .into(this);

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
//        Log.d(LOG_TAG, "Start getItemCount");
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewLikeHamster;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView){
//            Log.d(LOG_TAG, "Start initView");
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            textViewTitle = (TextView)itemView.findViewById(R.id.text_view_title);
            textViewLikeHamster = (TextView)itemView.findViewById(R.id.text_view_like_hamster);

        }
    }
}
