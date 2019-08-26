package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.model.RecipeModel;
import com.example.myapplication.network.RetrofitClient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RecipeModel> list;
    private Context context;
    private LaunchCallback launchCallback;
    private String tag = this.getClass().getSimpleName();

    public RecipeAdapter(Context context, ArrayList<RecipeModel> list, LaunchCallback launchCallback) {
        this.context = context;
        this.list = list;
        this.launchCallback = launchCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecipeViewHolder) {
            ((RecipeViewHolder) holder).bind();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface LaunchCallback {
        void onLaunchActivity(CircleImageView imageRecipe, RecipeModel recipeModel);
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageRecipe;
        private AppCompatTextView nameRecipe;
        private RequestListener<Bitmap> requestListener = new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                Log.d(tag, "(onLoadFailed) = " + e.getMessage());
                // important to return false so the error placeholder can be placed
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                return false;
            }
        };

        RecipeViewHolder(View view) {
            super(view);

            imageRecipe = view.findViewById(R.id.imageRecipe);
            nameRecipe = view.findViewById(R.id.nameRecipe);

        }

        void bind() {
            nameRecipe.setText(list.get(getAdapterPosition()).getRec_name());

            String imageName = RetrofitClient.IMAGE_URL + list.get(getAdapterPosition()).getRec_img().replace("RecipeBookImages", "RecipeBookImages/");

            Glide.with(context).asBitmap().load(list.get(getAdapterPosition()).getRec_img()).listener(requestListener).error(R.drawable.ic_broken_image_black_24dp).into(imageRecipe);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (launchCallback != null) {
                        launchCallback.onLaunchActivity(imageRecipe, list.get(getAdapterPosition()));
                    } else {
                        Log.d(tag, "callback is null");
                    }
                }
            });

            new loadBitmapFromUrl(list.get(getAdapterPosition()).getRec_img()).execute();

        }

        private class loadBitmapFromUrl extends AsyncTask<Void, Void, Bitmap> {

            private String rec_img;

            loadBitmapFromUrl(String rec_img) {
                this.rec_img = rec_img;
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL url = new URL(rec_img);
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.d(tag, "catch error = " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                super.onPostExecute(image);

                Log.d(tag, "(onPostExecute) background extract");

                try {
                    Palette.from(image).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            //work with the palette here
                            int defaultValue = 0x000000;
                            int vibrant = palette.getVibrantColor(defaultValue);
                            int vibrantLight = palette.getLightVibrantColor(defaultValue);
                            int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                            int muted = palette.getMutedColor(defaultValue);
                            int mutedLight = palette.getLightMutedColor(defaultValue);
                            int mutedDark = palette.getDarkMutedColor(defaultValue);

                            if (vibrantDark == 0) {
                                list.get(getAdapterPosition()).setBgColor(mutedDark);
                            } else {
                                list.get(getAdapterPosition()).setBgColor(vibrantDark);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.d(tag, "(onPostExecute) catch error = " + e.getMessage());
                }


            }
        }
    }
}