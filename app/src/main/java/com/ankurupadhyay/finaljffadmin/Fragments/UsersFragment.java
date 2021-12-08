package com.ankurupadhyay.finaljffadmin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ViewHolders.UserViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    public UsersFragment() {
        // Required empty public constructor
    }




    ProgressBar progressBar;
    private FirestorePagingAdapter<UserModel, UserViewHolder> adapter;


    FirebaseFirestore db;

    RecyclerView recyclerView;

    UserModel model;

    TextView total_users;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = view.findViewById(R.id.progressbar);


        total_users = view.findViewById(R.id.total_users12);
       // total_users = view.findViewById(R.id.total);
        db = FirebaseFirestore.getInstance();



        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty())
                {
                    List<UserModel> list = new ArrayList<>();
                    for (DocumentSnapshot document:queryDocumentSnapshots)
                    {
                        UserModel model = document.toObject(UserModel.class);
                        model.setId(document.getId());
                        list.add(model);
                    }
                    total_users.setText("Total Users : "+list.size());
                }
            }
        });




        Query query = db.collection("Users").orderBy("timestamp", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(6)
                .setPageSize(6)
                .build();



        FirestorePagingOptions<UserModel> options = new FirestorePagingOptions.Builder<UserModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, snapshot -> {

                    model = snapshot.toObject(UserModel.class);
                    model.setId(snapshot.getId());


                    return model;

                })
                .build();




        adapter = new FirestorePagingAdapter<UserModel, UserViewHolder>(options) {

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }


            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserModel model) {




                holder.name.setText(model.getName());

                if (model.getTimestamp()!=null)
                {
                    holder.mobile.setText("Contact : "+model.getId()+"\nEmail : "+model.getEmail()+"\n"+Common.gethours(model.getTimestamp().getSeconds()));
                }
                else
                {
                    holder.mobile.setText("Contact : "+model.getId()+"\nEmail : "+model.getEmail());
                }


                try {
                    Picasso.get().load(model.getImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

                }catch (IllegalArgumentException e)
                {

                }

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.listview, parent, false);



                return new UserViewHolder(view);
            }





            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:

                        notifyDataSetChanged();
                        break;

                    case LOADING_MORE:
                        notifyDataSetChanged();
                        break;

                    case LOADED:
                        progressBar.setVisibility(View.GONE);
                        notifyDataSetChanged();
                        adapter.stopListening();

                        break;

                    case ERROR:

                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        //logic here
                        break;

                    case FINISHED:
                        //logic here
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }


        };




        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();





        return view;
    }
}