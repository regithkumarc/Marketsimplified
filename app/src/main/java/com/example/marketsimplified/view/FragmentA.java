package com.example.marketsimplified.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketsimplified.R;
import com.example.marketsimplified.common.Utility;
import com.example.marketsimplified.di.BaseApi;
import com.example.marketsimplified.model.Details;
import com.example.marketsimplified.viewmodel.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentA extends Fragment {

    View view;
    RecyclerView recyclerView;
    DetailsAdapter heroAdapter;
    TextView right_arrow, left_arrow, pageCount, pageCount_label;
    List<Details> detailsList = new ArrayList<>();

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public DetailsAdapter getHeroAdapter() {
        return heroAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_a, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        pageCount = (TextView) view.findViewById(R.id.pageCount);
        pageCount.setText("1");

        pageCount_label = (TextView) view.findViewById(R.id.pageCount_label);

           // new GetDetailsTask().execute();
        //pageCount_label.setText("1" + "/" + String.valueOf(Math.round(detailsList.size()/10)));

        DetailsViewModel detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        detailsViewModel.getDetails().observe(this, new Observer<List<Details>>() {
            @Override
            public void onChanged(List<Details> heroes) {
                System.out.println("heros : " + heroes);
                Utility.listDetail.clear();
                storeData(heroes);
                detailsList = loadData(Utility.previousPageCount);

                pageCount.setText(String.valueOf(Utility.previousPageCount));
                int curPageNo = Integer.parseInt(pageCount.getText().toString());
                Utility.previousPageCount = curPageNo;

                pageCount_label.setText(String.valueOf(curPageNo) + "/" + String.valueOf(Math.round(Utility.listDetail.size() / 10)));
                heroAdapter = new DetailsAdapter(getContext(), detailsList);
                recyclerView.setAdapter(heroAdapter);
            }
        });

        left_arrow = (TextView) view.findViewById(R.id.left_arrow);
        left_arrow.setAlpha(1.0f);
        left_arrow.setEnabled(true);

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPageNo = Integer.parseInt(pageCount.getText().toString());
                curPageNo = curPageNo - 1;
                detailsList.clear();
                detailsList = loadData(curPageNo);
                if (curPageNo >= 1) {
                    if (curPageNo == 1) {
                        left_arrow.setAlpha(0.5f);
                        left_arrow.setEnabled(false);
                    } else {
                        left_arrow.setAlpha(1.0f);
                        left_arrow.setEnabled(true);
                        right_arrow.setAlpha(1.0f);
                        right_arrow.setEnabled(true);
                    }

                    Utility.previousPageCount = curPageNo;
                    pageCount.setText(String.valueOf(Utility.previousPageCount));
                    pageCount_label.setText(String.valueOf(curPageNo) + "/" + String.valueOf(Math.round(Utility.listDetail.size() / 10)));
                    getHeroAdapter().setContext(getContext());
                    getHeroAdapter().setHeroList(detailsList);
                }
            }
        });

        right_arrow = (TextView) view.findViewById(R.id.right_arrow);
        right_arrow.setAlpha(1.0f);
        left_arrow.setEnabled(true);

        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPageNo = Integer.parseInt(pageCount.getText().toString());
                curPageNo = curPageNo + 1;
                detailsList = loadData(curPageNo);
                if (Math.round(Utility.listDetail.size() / 10) >= curPageNo) {
                    if (Math.round(Utility.listDetail.size() / 10) == curPageNo) {
                        right_arrow.setAlpha(0.5f);
                        right_arrow.setEnabled(false);
                    } else {
                        right_arrow.setAlpha(1.0f);
                        right_arrow.setEnabled(true);
                        left_arrow.setAlpha(1.0f);
                        left_arrow.setEnabled(true);
                    }

                    Utility.previousPageCount = curPageNo;
                    pageCount.setText(String.valueOf(Utility.previousPageCount));
                    pageCount_label.setText(String.valueOf(curPageNo) + "/" + String.valueOf(Math.round(Utility.listDetail.size() / 10)));
                    getHeroAdapter().setContext(getContext());
                    getHeroAdapter().setHeroList(detailsList);
                }
            }
        });
    }

    class GetDetailsTask extends AsyncTask<Void, Void, String> {         // 1) void - input  3) String - output

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... f_url) {
            loadDetails();
            return null;
        }

        // While Downloading Json File
        protected void onProgressUpdate(Void... progress) {
            // Set progress percentage
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

    public void loadDetails() {

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
                Utility.listDetail.clear();
                Utility.listDetails = response.body();

                Utility.flag = false;
                storeData(Utility.listDetails);
                detailsList = loadData(Utility.previousPageCount);
                pageCount.setText(String.valueOf(Utility.previousPageCount));
                int curPageNo = Integer.parseInt(pageCount.getText().toString());
                Utility.previousPageCount = curPageNo;
                pageCount_label.setText(String.valueOf(curPageNo) + "/" + String.valueOf(Math.round(Utility.listDetail.size() / 10)));
                heroAdapter = new DetailsAdapter(getContext(), detailsList);
                recyclerView.setAdapter(heroAdapter);
            }

            @Override
            public void onFailure(Call<List<Details>> call, Throwable t) {
                Log.d("error : ", t.toString());
            }
        });
    }

    private void storeData(List<Details> detailsList) {
        for (int i = 0; i < detailsList.size(); i++) {
            Utility.listDetail.add(detailsList.get(i));
        }
    }

    public List<Details> loadData(int selectedPage) {
        Details details = null;
        List<Details> detailsAdapter = new ArrayList<>();

        int totalPatients = Utility.listDetail.size();
        int noofPages = (int) Math.ceil(totalPatients / 8.0);   // 8/8 -> 1    16/8 -> 2

        final int selPage = selectedPage;
        if (selectedPage <= noofPages && selectedPage > 0) {
            try {
                if (getRecyclerView() != null)
                    getRecyclerView().removeAllViewsInLayout();
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

            if (selPage == 1) {
                int limit;
                if (totalPatients < 10)
                    limit = totalPatients;
                else
                    limit = 10;
                for (int i = 0; i < limit; i++) {
                    details = (Details) Utility.listDetail.get(i);
                    detailsAdapter.add(details);
                }
                Log.d("l0 : ", details.toString());
            } else {
                int limit = selPage * 10;//Page limit
                int initial = (limit - 10);

                try {
                    for (int i = initial, j = 0; i < limit; i++, j++) {
                        details = (Details) Utility.listDetail.get(i);
                        detailsAdapter.add(details);
                    }
                    Log.d("l0 : ", details.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return detailsAdapter;
    }
}
