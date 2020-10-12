package com.example.marketsimplified.di;

import com.example.marketsimplified.model.Details;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BaseApi {

    String BASEURL = "https://api.github.com";

    @GET("repositories")
    Call<List<Details>> getDetailsList();
}
