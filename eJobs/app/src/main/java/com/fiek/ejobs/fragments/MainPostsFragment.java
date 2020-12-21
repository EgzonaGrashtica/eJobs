package com.fiek.ejobs.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiek.ejobs.R;
import com.fiek.ejobs.activities.LogInActivity;
import com.fiek.ejobs.adapters.HorizontalAdapter;
import com.fiek.ejobs.adapters.PostAdapter;
import com.fiek.ejobs.models.HorizontalModel;
import com.fiek.ejobs.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPostsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvVertical,rvHorizontal;
    public HorizontalAdapter horizontalAdapter;
    public PostAdapter postAdapter;
    public SwipeRefreshLayout refreshLayout;
    SimpleDateFormat formatter;
    FirebaseFirestore db;
    FirebaseUser currentUser;

  List<HorizontalModel> horizontalModels;
    String []names = {"Doctor","Programmer","Financier","Plumber", "Hair dresser","Delivery man","Call agent","Chef/Cook"};
    private int[] professionImg = {R.drawable.stethoscope,R.drawable.ic_laptop,R.drawable.ic_finance, R.drawable.tools,
            R.drawable.hair_dryer,R.drawable.ic_delivery_man,R.drawable.ic_customer_service,R.drawable.ic_chef};
    public MainPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPosts.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPostsFragment newInstance(String param1, String param2) {
        MainPostsFragment fragment = new MainPostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        horizontalAdapter=new HorizontalAdapter(getContext(),"MainPostsFragment",MainPostsFragment.this);
        postAdapter=new PostAdapter(getContext(),"MainPostsFragment");
        db= FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser ==null){
            startActivity(new Intent(getActivity(), LogInActivity.class));
            getActivity().finish();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        horizontalModels=new ArrayList<HorizontalModel>();
        rvHorizontal=view.findViewById(R.id.rvHorizontal);
        rvVertical=view.findViewById(R.id.rvVertical);
        refreshLayout=view.findViewById(R.id.refreshLayout);
        getData("all");

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvVertical.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvHorizontal.setLayoutManager(linearLayoutManager1);


        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData("all");
                    }
                });
            }
        });


        refreshLayout.setRefreshing(true);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyDataSetChanged();
                horizontalAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }, 3000);
        addIcons();
    }
    public void addIcons() {

        for (int i = 0; i < professionImg.length; i++) {
            HorizontalModel hmodel = new HorizontalModel();
            hmodel.setProfessionImg(professionImg[i]);
            hmodel.setName(names[i]);
            horizontalAdapter.horizontalModels.add(hmodel);
        }

        rvHorizontal.setAdapter(horizontalAdapter);
    }

    public void getData(String proffesion){
        refreshLayout.setRefreshing(true);
        postAdapter=new PostAdapter(getContext(),"MainPostsFragment");
        rvVertical.setAdapter(postAdapter);
        if(proffesion.equals("all")){
        db.collection("FavoriteJobs")
                .whereEqualTo("UserId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult()!=null) {
                                for (QueryDocumentSnapshot favoriteJob : task.getResult()) {
                                   /*FavoriteModel favoriteModel= new FavoriteModel(favoriteJob.getData().get("UserId").toString(),
                                           favoriteJob.getData().get("JobsId").toString());*/
                                    postAdapter.favoriteDataSource.add(favoriteJob.getData().get("JobsId").toString());
                                }

                            }
                        } else {
                            Log.e("Posts","Posts didn't come");
                        }
                    }
                });
        db.collection("Jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult()!=null) {
                                for (QueryDocumentSnapshot job : task.getResult()) {
                                    PostModel postModel = new PostModel(job.getId(),job.getData().get("companyName").toString(),
                                            job.getData().get("location").toString(), job.getData().get("freeSpots").toString(),
                                            job.getData().get("description").toString(), job.getData().get("photoPath").toString(),
                                            job.getData().get("position").toString(),
                                            job.getDate("expirationDate"));
                                    postAdapter.dataSource.add(postModel);
                                }
                                postAdapter.notifyDataSetChanged();
                                horizontalAdapter.notifyDataSetChanged();
                                refreshLayout.setRefreshing(false);
                            }
                        } else {
                            Log.e("Posts","Posts didn't come");
                        }
                    }
                });
    }
        else{
            db.collection("FavoriteJobs")
                    .whereEqualTo("UserId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult()!=null) {
                                    for (QueryDocumentSnapshot favoriteJob : task.getResult()) {
                                   /*FavoriteModel favoriteModel= new FavoriteModel(favoriteJob.getData().get("UserId").toString(),
                                           favoriteJob.getData().get("JobsId").toString());*/
                                        postAdapter.favoriteDataSource.add(favoriteJob.getData().get("JobsId").toString());
                                    }
                                }
                            } else {
                                Log.e("Posts","Posts didn't come");
                            }
                        }
                    });
            db.collection("Jobs")
                    .whereEqualTo("position",proffesion)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult()!=null) {
                                    for (QueryDocumentSnapshot job : task.getResult()) {
                                        PostModel postModel = new PostModel(job.getId(),job.getData().get("companyName").toString(),
                                                job.getData().get("location").toString(), job.getData().get("freeSpots").toString(),
                                                job.getData().get("description").toString(), job.getData().get("photoPath").toString(),
                                                job.getData().get("position").toString(),
                                                job.getDate("expirationDate"));
                                        postAdapter.dataSource.add(postModel);
                                    }

                                }
                            } else {
                                Log.e("Posts","Posts didn't come");
                            }
                        }
                    });
        }
    }

}
