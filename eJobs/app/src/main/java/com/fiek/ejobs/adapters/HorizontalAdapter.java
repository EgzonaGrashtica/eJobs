package com.fiek.ejobs.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fiek.ejobs.fragments.MainPostsFragment;
import com.fiek.ejobs.R;
import com.fiek.ejobs.models.HorizontalModel;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    private Context context;
   /* String []names = {"Doctor", "Plumber", "Hair dresser"};
    private int[] professionImg = {R.drawable.stethoscope, R.drawable.tools, R.drawable.hair_dryer};*/
   public List<HorizontalModel> horizontalModels=new ArrayList<HorizontalModel>();
   MainPostsFragment mainPostsFragment=new MainPostsFragment();

    public HorizontalAdapter(Context context ,String checkFragment,MainPostsFragment mainPostsFragment) {
        this.context = context;
        this.mainPostsFragment=mainPostsFragment;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.horizontal_row,parent,false);
        final HorizontalAdapter.HorizontalViewHolder horizontalViewHolder=new HorizontalAdapter.HorizontalViewHolder(view);
        horizontalViewHolder.horizontalRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proffesion=horizontalModels.get(horizontalViewHolder.getAdapterPosition()).getName();
               mainPostsFragment.getData(proffesion);
                final Handler handler = new Handler();
                mainPostsFragment.refreshLayout.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainPostsFragment.postAdapter.notifyDataSetChanged();
                        mainPostsFragment.horizontalAdapter.notifyDataSetChanged();
                        mainPostsFragment.refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return horizontalViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
       /* holder.tvRowName.setText(names[position]);
        holder.imgRow.setImageResource(professionImg[position]);
       // Glide.with(context).load(professionImg[position]).into(holder.imgRow);*/
       // holder.animationRow.setAnimation(AnimationUtils.loadAnimation(context,R.anim.form_right_to_left));
       holder.tvRowName.setText(horizontalModels.get(position).getName());
       holder.imgRow.setImageResource(horizontalModels.get(position).getProfessionImg());
    }

    @Override
    public int getItemCount() {
        return horizontalModels.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRow;
        TextView tvRowName;
        ConstraintLayout horizontalRow,animationRow;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);

            animationRow=itemView.findViewById(R.id.animationRow);
            horizontalRow=itemView.findViewById(R.id.horizontalRow);
            tvRowName=itemView.findViewById(R.id.tvRowName);
            imgRow=itemView.findViewById(R.id.imgRow);
        }


    }
}
