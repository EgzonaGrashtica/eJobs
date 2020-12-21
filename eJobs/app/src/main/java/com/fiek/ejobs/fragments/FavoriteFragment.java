package com.fiek.ejobs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiek.ejobs.adapters.PostAdapter;
import com.fiek.ejobs.models.PostModel;
import com.fiek.ejobs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FavoriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    private PostAdapter postAdapter;
    private SwipeRefreshLayout refreshFavoriteLayout;
    private RecyclerView rvFavorite;
    ConstraintLayout favoriteFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Item2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        postAdapter=new PostAdapter(getContext(),"FavoriteFragment");
        db= FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteFragment=view.findViewById(R.id.favoriteFragment);
        rvFavorite=view.findViewById(R.id.rvFavorite);
        refreshFavoriteLayout=view.findViewById(R.id.refreshFavoriteLayout);
        getData();
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvFavorite.setLayoutManager(linearLayoutManager);



        refreshFavoriteLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        postAdapter.notifyDataSetChanged();
                    }
                });
                refreshFavoriteLayout.setRefreshing(false);
            }
        });
        refreshFavoriteLayout.setRefreshing(true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyDataSetChanged();
                refreshFavoriteLayout.setRefreshing(false);
            }
        }, 3000);
    }
    public void getData(){
        postAdapter=new PostAdapter(getContext(),"FavoriteFragment");
        rvFavorite.setAdapter(postAdapter);
        refreshFavoriteLayout.setRefreshing(true);
        db.collection("FavoriteJobs")
                .whereEqualTo("UserId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult()!=null) {
                                for (QueryDocumentSnapshot favoriteJob : task.getResult()) {
                                    postAdapter.favoriteDataSource.add(favoriteJob.getData().get("JobsId").toString());
                                }
                                if(postAdapter.favoriteDataSource.isEmpty()){
                                    favoriteFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_background_no_favorite_post));
                                }
                                else {
                                    db.collection("Jobs")
                                            .whereIn(FieldPath.documentId(), postAdapter.favoriteDataSource)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult() != null) {
                                                            for (QueryDocumentSnapshot job : task.getResult()) {
                                                                PostModel postModel = new PostModel(job.getId(), job.getData().get("companyName").toString(),
                                                                        job.getData().get("location").toString(), job.getData().get("freeSpots").toString(),
                                                                        job.getData().get("description").toString(), job.getData().get("photoPath").toString(),
                                                                        job.getData().get("position").toString(),
                                                                        job.getDate("expirationDate"));
                                                                postAdapter.dataSource.add(postModel);
                                                            }
                                                            postAdapter.notifyDataSetChanged();
                                                            refreshFavoriteLayout.setRefreshing(false);
                                                        }
                                                    } else {
                                                        Log.e("Posts", "Posts didn't come");
                                                    } }
                                            });
                                }
                            }
                        } else {
                            Log.e("Posts","Posts didn't come");
                            favoriteFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_background_no_favorite_post));
                        }
                    }
                });
    }

}
