package com.example.myapplication.network;

import com.example.myapplication.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RetrofitClient {

    public static final String IMAGE_URL = "http://192.168.0.88/";
    private static final String MAIN_URL = "http://192.168.0.88:84/RecipeDetails/RecipeList.asmx/";

    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //want to avoid custom url encoding , here is solution
        /*httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String string = request.url().toString();
                string = string.replace("%2C", ",");
                //string = string.replace("%3D", "=");

                Request newRequest = new Request.Builder()
                        .url(string)
                        .build();

                return chain.proceed(newRequest);
            }
        });*/

        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.writeTimeout(1, TimeUnit.MINUTES);

        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(logging);
        }

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(MAIN_URL);
        return builder.client(httpClient.build()).build();
    }

    public interface getRecipeList {

        @POST("getRecipeList")
        @FormUrlEncoded
        Call<ResponseBody> getRecipe(
                @Field("category") String category,
                @Field("subcategory") String subcategory
        );
    }
}