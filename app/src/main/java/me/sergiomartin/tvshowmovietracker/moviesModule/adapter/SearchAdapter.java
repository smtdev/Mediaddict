package me.sergiomartin.tvshowmovietracker.moviesModule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.MovieSearch;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private ArrayList<MovieSearch> movieSearchArrayList;
    private LayoutInflater layoutInflater;

    public SearchAdapter(Context context, ArrayList<MovieSearch> data) {
        this.context = context;
        this.movieSearchArrayList = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<MovieSearch> data){
        this.movieSearchArrayList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_card_layout, parent, false);

        //return view;
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.onBindView(position);

    }

    @Override
    public int getItemCount() {
        if(movieSearchArrayList == null)
            return  0;
        return movieSearchArrayList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        public SearchViewHolder(View itemView) {
            super(itemView);
        }

        public void onBindView(int position) {
        }
    }
}
