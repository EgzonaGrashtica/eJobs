package com.fiek.ejobs.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fiek.ejobs.models.PostModel;
import com.fiek.ejobs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostsViewHolder> {
    public List<PostModel> dataSource = new ArrayList<PostModel>();
    public List<String> favoriteDataSource=new ArrayList<String>();
    private Context context;
    SimpleDateFormat formatter;
    BottomSheetDialog bottomSheetDialog;
    FirebaseFirestore db;
    private FirebaseUser user;

    private FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    public PostAdapter(Context ct, String checkFragment) {
        context=ct;

    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater =LayoutInflater.from(context);
        final View view=inflater.inflate(R.layout.vertical_row,parent,false);
        final PostsViewHolder postsViewHolder=new PostsViewHolder(view);
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        bottomSheetDialog=new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        postsViewHolder.postRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(postsViewHolder);

            }
        });
        db= FirebaseFirestore.getInstance();
        postsViewHolder.ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postsViewHolder.ibFavorite.getTag().equals("isNotFavorite")){
               String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
               String jobId=dataSource.get(postsViewHolder.getAdapterPosition()).getJobId();
               addToFavorites(userId,jobId);
               postsViewHolder.ibFavorite.setImageResource(R.drawable.ic_star_favorite);
                    postsViewHolder.ibFavorite.setTag("isFavorite");
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Do you want to delete form favorites?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()                 {

                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("FavoriteJobs")
                                            .whereEqualTo("JobsId",dataSource.get(postsViewHolder.getAdapterPosition()).getJobId())
                                            .whereEqualTo("UserId",FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult() != null) {
                                                            for (QueryDocumentSnapshot job : task.getResult()) {
                                                               db.collection("FavoriteJobs").document(job.getId()).delete()
                                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                           @Override
                                                                           public void onSuccess(Void aVoid) {
                                                                               postsViewHolder.ibFavorite.setImageResource(R.drawable.ic_star);
                                                                               postsViewHolder.ibFavorite.setTag("isNotFavorite");
                                                                           }
                                                                       })
                                                                       .addOnFailureListener(new OnFailureListener() {
                                                                           @Override
                                                                           public void onFailure(@NonNull Exception e) {
                                                                           }
                                                                       });

                                                            }
                                                        }
                                                    } else {
                                                        Log.e("Posts", "Posts didn't come");

                                                    }
                                                }
                                            });
                                }
                            }).setNegativeButton(R.string.btnCancel, null);

                    AlertDialog alert1 = alert.create();
                    alert1.show();

                }
            }
        });


        return postsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        holder.postRow.setAnimation(AnimationUtils.loadAnimation(context,R.anim.item_animation_from_bottom));
        holder.tvCompanyName.setText(dataSource.get(position).getCompanyName());
        holder.tvLocation.setText(dataSource.get(position).getLocation());
        holder.tvExpire.setText(formatter.format(dataSource.get(position).getExpirationDate()));
        if(dataSource.get(position).getPhotoPath()!=null)
        {
       Glide.with(context).load(dataSource.get(position).getPhotoPath()).into(holder.ivCompanyPhoto);
        }
        else {
            holder.ivCompanyPhoto.setImageResource(R.drawable.logo_company_background);
        }
        if(favoriteDataSource.contains(dataSource.get(position).getJobId())){
            holder.ibFavorite.setImageResource(R.drawable.ic_star_favorite);
            holder.ibFavorite.setTag("isFavorite");
        }
        else {

            holder.ibFavorite.setImageResource(R.drawable.ic_star);
            holder.ibFavorite.setTag("isNotFavorite");
        }

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    public class PostsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCompanyPhoto;
        TextView tvCompanyName,tvLocation,tvExpire;
        ImageButton ibFavorite;
        ConstraintLayout postRow;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            postRow=itemView.findViewById(R.id.postRow);
            tvCompanyName=itemView.findViewById(R.id.tvCompanyName);
            tvLocation=itemView.findViewById(R.id.tvLocation);
            tvExpire=itemView.findViewById(R.id.tvExpire);
            ivCompanyPhoto=itemView.findViewById(R.id.ivCompanyPhoto);
            ibFavorite=itemView.findViewById(R.id.ibFavorite);
        }
    }
    public void createDialog(final PostsViewHolder postsViewHolder){
        TextView tvBottomCompanyName=bottomSheetDialog.findViewById(R.id.tvBottomCompanyName);
        TextView tvBottomLocation=bottomSheetDialog.findViewById(R.id.tvBottomLocation);
        TextView tvBottomFreeSpots=bottomSheetDialog.findViewById(R.id.tvBottomFreeSpots);
        TextView tvBottomExperationDate=bottomSheetDialog.findViewById(R.id.tvBottomExperationDate);
        EditText etBottomDescription=bottomSheetDialog.findViewById(R.id.etBottomDescription);
       ImageView ivBottomPhoto=bottomSheetDialog.findViewById(R.id.ivBottomPhoto);
        etBottomDescription.setClickable(false);
        etBottomDescription.setEnabled(false);
        tvBottomCompanyName.setText(dataSource.get(postsViewHolder.getAdapterPosition()).getCompanyName());
        tvBottomLocation.setText(dataSource.get(postsViewHolder.getAdapterPosition()).getLocation());
        tvBottomFreeSpots.setText(dataSource.get(postsViewHolder.getAdapterPosition()).getFreeSpots());
        tvBottomExperationDate.setText(formatter.format(dataSource.get(postsViewHolder.getAdapterPosition()).getExpirationDate()));
        etBottomDescription.setText(dataSource.get(postsViewHolder.getAdapterPosition()).getDescription());
        Glide.with(context).load(dataSource.get(postsViewHolder.getAdapterPosition()).getPhotoPath()).into(ivBottomPhoto);

        bottomSheetDialog.show();
    }

    private void addToFavorites(String UserId, String JobsId){
        Map<String, Object> favoriteJob = new HashMap<>();
        favoriteJob.put("UserId", UserId);
        favoriteJob.put("JobsId", JobsId);

        db.collection("FavoriteJobs")
                .add(favoriteJob)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context,"This post is added to Favorites!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });



    }

}



