package com.example.carlos.appcurso.UI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.appcurso.Data.User;
import com.example.carlos.appcurso.R;

import java.util.List;

/**
 * Created by Carlos on 31/01/2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingHolder> {

    private List<User> userDataList;
    LayoutInflater inflater;
    int mode;

    public RankingAdapter(List<User> userDataList, Context context,int mode) {
        this.inflater = LayoutInflater.from(context);
        this.userDataList = userDataList;
        this.mode = mode;
    }

    @Override
    public RankingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ranking_item, parent, false);
        return new RankingHolder(view);
    }

    @Override
    public void onBindViewHolder(RankingHolder holder, int position) {
        User user = userDataList.get(position);
        holder.userName.setText(user.getName());
        int aux = 0;
        if(mode == 4) aux = user.getPoints4();
        else if(mode == 6) aux = user.getPoints6();
        holder.points.setText(Integer.toString(aux));
        holder.positionNumber.setText(Integer.toString(position+1)+ ". ");
        if(position == 0){
            holder.medal.setImageResource(R.drawable.ic_gold_medal);
        }
        if(position == 1){
            holder.medal.setImageResource(R.drawable.ic_silver_medal);
        }
        if(position == 2){
            holder.medal.setImageResource(R.drawable.ic_bronze_medal);
        }
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    class RankingHolder extends RecyclerView.ViewHolder {

        private TextView positionNumber;
        private TextView userName;
        private TextView points;
        private ImageView medal;

        private View container;

        public RankingHolder(View itemView) {
            super(itemView);
            initializeHolderViews();
        }

        private void initializeHolderViews() {
            positionNumber = (TextView) itemView.findViewById(R.id.positionNumber);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            points = (TextView) itemView.findViewById(R.id.user_points);
            medal = (ImageView) itemView.findViewById(R.id.medal);
            container = (View) itemView.findViewById(R.id.itemContainer);

        }

    }

}
