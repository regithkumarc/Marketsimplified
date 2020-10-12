package com.example.marketsimplified.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.marketsimplified.common.Utility;
import com.example.marketsimplified.di.BaseApi;
import com.example.marketsimplified.model.Details;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsViewModel extends ViewModel {

    public MutableLiveData<List<Details>> mutableLiveData;


    public LiveData<List<Details>> getDetails(){

        if(mutableLiveData == null){
            mutableLiveData = new MutableLiveData<>();
            loadHeroes();
        }

        return mutableLiveData;
    }

    public List<Details> loadHeroes(){

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseApi.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BaseApi baseApi = retrofit.create(BaseApi.class);

        Call<List<Details>> listCall = baseApi.getDetailsList();

        listCall.enqueue(new Callback<List<Details>>() {
            @Override
            public void onResponse(Call<List<Details>> call, Response<List<Details>> response) {
                mutableLiveData.setValue(response.body());
                Log.d("response : " , response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Details>> call, Throwable t) {
                Log.d("error : " , t.toString());
            }
        });

        return Utility.listDetails;
    }
}
