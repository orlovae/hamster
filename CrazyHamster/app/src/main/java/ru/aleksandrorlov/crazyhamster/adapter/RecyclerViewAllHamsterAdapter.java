package ru.aleksandrorlov.crazyhamster.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.ViewHamsterActivity;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 20.05.17.
 */

public class RecyclerViewAllHamsterAdapter extends RecyclerView.Adapter<RecyclerViewAllHamsterAdapter.ViewHolder>{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context context;

    private Cursor dataCursor;

    private int width, height;


    public RecyclerViewAllHamsterAdapter(Context context, Cursor cursor, int width, int height) {
        this.context = context;
        this.dataCursor = cursor;
        this.width = width;
        this.height = height;
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
        int descriptionColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_DESCRIPTION);
        int imagePathColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_IMAGE_PATH);
        int likeHamsterColIndex = dataCursor.getColumnIndex(Contract.Hamster.COLUMN_FAVORITE);

        int id = dataCursor.getInt(idColIndex);
        String titleFromCursor = dataCursor.getString(titleColIndex);
        String descriptionFromCursor = dataCursor.getString(descriptionColIndex);
        String imagePath = dataCursor.getString(imagePathColIndex);


        int likeHamster = dataCursor.getInt(likeHamsterColIndex);
        boolean likeHamsterFromCursor = castIntToBoolean(likeHamster);

        Uri imageUri = null;
        if (imagePath != null) {
            imageUri = Uri.fromFile(new File(imagePath));
        }

        Picasso.with(context)
                .load(imageUri)
                .resize((int)Math.round(width * 0.2), (int)Math.round(height * 0.2))
                //TODO вынести в константы коэффициент сжатия картинок.
                .placeholder(R.drawable.progress_animation)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewHamsterActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", titleFromCursor);
                intent.putExtra("description", descriptionFromCursor);
                intent.putExtra("imagePath", imagePath);
                intent.putExtra("likeHamster", likeHamsterFromCursor);
                context.startActivity(intent);
            }
        });

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
