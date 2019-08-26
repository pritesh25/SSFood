package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.RecipeAdapter;
import com.example.myapplication.model.RecipeModel;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.CONSTANT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.utils.CONSTANT.rec_bgcolor;
import static com.example.myapplication.utils.CONSTANT.rec_cat;
import static com.example.myapplication.utils.CONSTANT.rec_id;
import static com.example.myapplication.utils.CONSTANT.rec_img;
import static com.example.myapplication.utils.CONSTANT.rec_ingredients;
import static com.example.myapplication.utils.CONSTANT.rec_name;
import static com.example.myapplication.utils.CONSTANT.rec_steps;
import static com.example.myapplication.utils.CONSTANT.rec_subcat;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.LaunchCallback {

    ArrayList<RecipeModel> list = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private ContentLoadingProgressBar recipeListProgressBar;
    private String tag = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*list.add(new RecipeModel(1, "Apple", "https://www.gstatic.com/webp/gallery/1.jpg", "ingredient 1", "step 1", "cat 1", "subcat 1", 1));
        list.add(new RecipeModel(2, "Ball", "https://www.gstatic.com/webp/gallery/2.jpg", "ingredient 2", "step 2", "cat 2", "subcat 2", 1));
        list.add(new RecipeModel(3, "Cat", "https://www.gstatic.com/webp/gallery/3.jpg", "ingredient 3", "step 3", "cat 3", "subcat 3", 1));
        list.add(new RecipeModel(4, "Dog", "https://www.gstatic.com/webp/gallery/4.jpg", "ingredient 4", "step 4", "cat 4", "subcat 4", 1));
        list.add(new RecipeModel(5, "Egg", "https://www.gstatic.com/webp/gallery/5.jpg", "ingredient 5", "step 5", "cat 5", "subcat 5", 1));*/

        recipeListProgressBar = findViewById(R.id.recipeListProgressBar);
        recipeListProgressBar.setVisibility(View.GONE);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(getApplicationContext(), list, this);
        recyclerView.setAdapter(recipeAdapter);

        if (isConnectionAvailable()) {
            loadOfflineData();
            //getRecipeFromServer();
        } else {

            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void loadOfflineData() {

        String response = "[\n" +
                "  {\n" +
                "    \"rec_id\": \"1\",\n" +
                "    \"rec_name\": \"Khaman Dokhlaa\",\n" +
                "    \"rec_img\": \"https://www.gstatic.com/webp/gallery3/1.png\",\n" +
                "    \"rec_ingredients\": \"Gram flour (besan) sieved 2 cups,Yogurt beaten 1 cup Salt to taste,Turmeric powder 1/2 teaspoon,Green chilli-ginger paste 1 teaspoon,Oil 2 tablespoons,Lemon juice 1 tablespoon,Soda bicarbonate 1 teaspoon,Mustard seeds 1 teaspoon,Fresh coriander leaves chopped 2 tablespoons,Coconut scraped 1/2 cup\",\n" +
                "    \"rec_steps\": \"Step 1:Take gram flour in a bowl. Add yogurt and approximately one cup of warm water and mix. Avoid lumps. Add salt and mix again.,Step 2:Leave it aside to ferment for three to four hours. When gram flour mixture has fermented, add turmeric powder and green chilli-ginger paste. Mix. Heat the steamer. Grease a thali.,Step 3:In a small bowl take lemon juice, soda bicarbonate, one teaspoon of oil and mix. Add it to the batter and whisk briskly. Pour batter into the greased thali and place it in the steamer.,Step 4:Cover with the lid and steam for ten minutes. When a little cool, cut into squares and keep in a serving bowl/plate.,Step 5:Heat remaining oil in a small pan. Add mustard seeds. When the seeds begin to crackle, remove and pour over the dhoklas.,Step 6:Serve,garnished with chopped coriander leaves and scraped coconut.\",\n" +
                "    \"rec_cat\": \"Breakfast\",\n" +
                "    \"rec_subcat\": \"Veg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"rec_id\": \"2\",\n" +
                "    \"rec_name\": \"Sago Potato Rolls\",\n" +
                "    \"rec_img\": \"https://www.gstatic.com/webp/gallery3/1.png\",\n" +
                "    \"rec_ingredients\": \"Sago 3/4 cup,Potatoes boiled and mashed 3 large,Roasted peanuts crushed 1/2 cup,Green chillies chopped 3,Ginger chopped 1 inch piece,Lemon juice 1 tablespoon,Fresh coriander leaves chopped 2-3 tablespoons,Salt to taste,Oil to deep fry\",\n" +
                "    \"rec_steps\": \"Step 1:Wash sago and soak in water for at least one hour. If the rolls are to be prepared within very short time, then soak the sago in hot water,Step 2:Squeeze out and keep in a big bowl. Add potatoes, peanuts, green chillies, ginger, lemon juice, coriander leaves and salt and mix. Add a little water if necessary to make a dough. Let it stand for a few minutes.,Step 3:Divide the dough into eight equal portions and shape them into oval shaped rolls.,Step 4:Heat sufficient oil in a kadai till medium hot. Slide in four rolls and deep fry till golden. Drain and place them on an absorbent paper. Similarly prepare rest of the rolls.,Step 5:Serve hot with any chutney of your choice.\",\n" +
                "    \"rec_cat\": \"Breakfast\",\n" +
                "    \"rec_subcat\": \"Veg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"rec_id\": \"3\",\n" +
                "    \"rec_name\": \"Mini Idli Upma\",\n" +
                "    \"rec_img\": \"https://www.gstatic.com/webp/gallery3/1.png\",\n" +
                "    \"rec_ingredients\": \"5 finely chopped onion,1/2 teaspoons Mustard seeds,1/2 teaspoon White Urad Dal,1 teaspoon Green Chillies finely chopped,1 teaspoon Ginger finely chopped,4-5 Curry leaves finely chopped,1/4 teaspoon Turmeric powder,1 tablespoon Oil,Coriander Leaves finely chopped,Salt to taste\",\n" +
                "    \"rec_steps\": \"Step 1:To begin making the Mini Idli Upma Recipe (Steamed Rice Cake Stir Fry) heat oil in a heavy bottomed pan add the mustard seeds cumin seeds and urad dal. Allow them to crackle. Allow the urad to get roasted and lightly browned,Step 2:Add the onions, saute until onions are soft and tender on medium heat. Now add the curry leaves green chillies and ginger. Add the turmeric powder and the mini idlis and salt. Saute until all the ingredients are well combined. Check the salt and spice and adjust to suit your taste,Step 3:Turn the heat to low cover the pan and simmer for about 5 minutes,Step 4:Finally stir in the freshly chopped coriander leaves and serve the Mini Idli Upma for breakfast or pack it for a snack box,Step 5:Serve Mini Idli Upma Recipe (Steamed Rice Cake Stir Fry) with South Indian Coconut Chutney or Red Chilli Coconut Chutney Recipe and a glass of hot Ginger Cardamom Chai\",\n" +
                "    \"rec_cat\": \"Breakfast\",\n" +
                "    \"rec_subcat\": \"Veg\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"rec_id\": \"4\",\n" +
                "    \"rec_name\": \"Paneer Bhurji Pav \",\n" +
                "    \"rec_img\": \"https://www.gstatic.com/webp/gallery3/1.png\",\n" +
                "    \"rec_ingredients\": \"1 tablespoon Cooking oil,1 teaspoon Ginger grated,1 Green Chilli finely chopped,1 Onion finely chopped,1 Tomato finely chopped,1/4 teaspoon Turmeric powder (Haldi),1/4 teaspoon Red chilli powder,2 cups Paneer (Homemade Cottage Cheese) crumpled,1 tablespoon Coriander (Dhania) Leaves chopped,Salt to taste,4 Pav buns roasted in butter\",\n" +
                "    \"rec_steps\": \"Step 1:To begin making the Spicy Paneer Bhurji Pav Sandwich, we'll begin by making paneer bhurji,Step 2:Place a pan on the heat and warm the oil in it. To it, add ginger and green chilies and saute for a minute or two. Then add the chopped onions and continue to saute until translucent.,Step 3:Followed by chopped tomatoes, turmeric powder and red chilli powder - add them all into the pan and continue to cook, stirring continuously until the tomatoes are softened,Step 4:Continue to cook till some moisture dries out and the masalas begin to leave the sides of the pan,Step 5:At this stage, add the crumbled paneer, season with required salt and mix well,Step 6:Cook the prepared bhurji on a medium heat for another minute or two. Sprinkle chopped coriander, turn off the heat and set the bhurji aside till later use,Step 7:Apply butter on the pav buns, place them on a preheated pan and toast them on medium heat until browned and crisp,Step 8:The next step is to assemble the Pav Sandwiches. To do this, Slice each pav into half and lay them flat on a work surface,Step 9:Scoop a spoonful of paneer bhurji and serve hot,Step 10:Serve the Spicy Paneer Bhurji Pav Sandwich Recipe along a hot cup of Masala Chai for a chaat party.\",\n" +
                "    \"rec_cat\": \"Breakfast\",\n" +
                "    \"rec_subcat\": \"Veg\"\n" +
                "  }\n" +
                "]";

        try {

            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);
                int rec_id = object.getInt(CONSTANT.rec_id);
                String rec_name = object.getString(CONSTANT.rec_name);
                String rec_img = object.getString(CONSTANT.rec_img);
                String rec_ingredients = object.getString(CONSTANT.rec_ingredients);
                String rec_steps = object.getString(CONSTANT.rec_steps);
                String rec_cat = object.getString(CONSTANT.rec_cat);
                String rec_subcat = object.getString(CONSTANT.rec_subcat);

                RecipeModel model = new RecipeModel(rec_id, rec_name, rec_img, rec_ingredients, rec_steps, rec_cat, rec_subcat, 1);
                list.add(model);

            }
            recipeAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null) {
            return info.isConnected();
        }
        return false;
    }

    private void getRecipeFromServer() {

        recipeListProgressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getRecipeList recipe = RetrofitClient.getRetrofit().create(RetrofitClient.getRecipeList.class);
        Call<ResponseBody> result = recipe.getRecipe("Breakfast", "Veg");
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                recipeListProgressBar.setVisibility(View.GONE);

                try {
                    String jsonInString = response.body().string();
                    JSONArray array = new JSONArray(jsonInString);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        int rec_id = object.getInt(CONSTANT.rec_id);
                        String rec_name = object.getString(CONSTANT.rec_name);
                        String rec_img = RetrofitClient.IMAGE_URL + object.getString(CONSTANT.rec_img).replace("RecipeBookImages", "RecipeBookImages/");
                        String rec_ingredients = object.getString(CONSTANT.rec_ingredients);
                        String rec_steps = object.getString(CONSTANT.rec_steps);
                        String rec_cat = object.getString(CONSTANT.rec_cat);
                        String rec_subcat = object.getString(CONSTANT.rec_subcat);

                        RecipeModel model = new RecipeModel(rec_id, rec_name, rec_img, rec_ingredients, rec_steps, rec_cat, rec_subcat, 1);
                        list.add(model);

                    }

                    //set data and notify changes
                    recipeAdapter.notifyDataSetChanged();

                } catch (IOException | JSONException | NullPointerException e) {
                    recipeListProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(tag, "catch error = " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                recipeListProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(tag, "onFailure = " + t.getMessage());
            }
        });
    }

    @Override
    public void onLaunchActivity(CircleImageView imageRecipe, RecipeModel recipeModel) {

        Intent intent = new Intent(this, DetailedActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(rec_id, recipeModel.getRec_id());
        bundle.putString(rec_name, recipeModel.getRec_name());
        bundle.putString(rec_img, recipeModel.getRec_img());
        bundle.putString(rec_ingredients, recipeModel.getRec_ingredients());
        bundle.putString(rec_steps, recipeModel.getRec_steps());
        bundle.putString(rec_cat, recipeModel.getRec_cat());
        bundle.putString(rec_subcat, recipeModel.getRec_subcat());
        bundle.putInt(rec_bgcolor, recipeModel.getBgColor());

        ActivityOptionsCompat activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        imageRecipe,
                        "imageMain");

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, activityOptionsCompat.toBundle());
    }
}