package com.fiek.ejobs.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fiek.ejobs.R;
import com.fiek.ejobs.activities.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Button btnLogOut;
    private EditText etUserName,etProfileEmail;
     FirebaseAuth.AuthStateListener authStateListener;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
           authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null){
                        mAuth.removeAuthStateListener(authStateListener);
                        Intent intent=new Intent(getActivity(), LogInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }
                }
            };
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUserName=view.findViewById(R.id.etUserName);
        etProfileEmail=view.findViewById(R.id.etProfileEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();

        etUserName.setClickable(false);
        etUserName.setEnabled(false);
        etProfileEmail.setClickable(false);
        etProfileEmail.setEnabled(false);

        etUserName.setText(user.getDisplayName());
        etProfileEmail.setText(user.getEmail());


        btnLogOut=view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(R.string.alertLogOut)
                        .setPositiveButton(R.string.tvLogOut, new DialogInterface.OnClickListener()                 {

                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                            }
                        }).setNegativeButton(R.string.btnCancel, null);

                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });
    }


}
