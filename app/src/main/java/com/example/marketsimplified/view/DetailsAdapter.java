package com.example.marketsimplified.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketsimplified.R;
import com.example.marketsimplified.common.Utility;
import com.example.marketsimplified.model.Details;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.HeroViewHolder> {

    Context context;
    List<Details> heroList;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setHeroList(List<Details> heroList) {
        this.heroList = heroList;
    }

    public DetailsAdapter(Context context, List<Details> heroList){
       this.context = context;
       this.heroList = heroList;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_list_item,parent,false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HeroViewHolder holder, int position) {

        Details details = heroList.get(position);

        Log.d("data : ",details.getName());
        Log.d("data : ",details.getOwner().toString());

        if(details.getOwner().getAvatarUrl() != null)
            Glide.with(context).load(details.getOwner().getAvatarUrl()).into(holder.imageView);
        holder.name.setText(String.valueOf(details.getName()));
        if(details.getPriv().equals(true)){
            holder.privateOrPublic.setText("private");
        }else{
            holder.privateOrPublic.setText("public");
        }

        holder.repoUrl.setText(details.getOwner().getReposUrl());

        holder.commends_linear.setVisibility(View.GONE);

        holder.commends_edittext.setPadding(5,0,5,0);


        holder.comments_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commends_linear.setVisibility(View.VISIBLE);
            }
        });

        holder.close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commends_linear.setVisibility(View.GONE);
                holder.commends_edittext.setText("");
            }
        });

        holder.send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commends_linear.setVisibility(View.GONE);
                holder.commends_edittext.setText("");
            }
        });

        holder.item_list_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details detail = (Details) v.getTag();
                Utility.showDetails(context,detail);
            }
        });
        holder.item_list_linear.setTag(details);

    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name,privateOrPublic,repoUrl,close_icon,send_icon;
        LinearLayout commends_linear,comments_view,item_list_linear;
        EditText commends_edittext;

        public HeroViewHolder(View view){
            super(view);

            imageView = view.findViewById(R.id.avatarUrl);
            name = view.findViewById(R.id.name);
            privateOrPublic = view.findViewById(R.id.privateOrPublic);
            repoUrl = view.findViewById(R.id.repo_url);
            commends_linear = view.findViewById(R.id.commends_linear);
            item_list_linear = view.findViewById(R.id.item_list_linear);
            commends_edittext = view.findViewById(R.id.commends_edittext);
            comments_view = view.findViewById(R.id.comments_view);
            close_icon = view.findViewById(R.id.close_icon);
            send_icon = view.findViewById(R.id.send_icon);
        }
    }
}

