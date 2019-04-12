package com.example.myapp.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapp.Common;
import com.example.myapp.Model.Level;
import com.example.myapp.Model.User;
import com.example.myapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class QuizActivity extends Fragment {

    View myFragment;
    RecyclerView listLevel;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Level, LevelViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference levels;

    public static QuizActivity newInstance() {
        QuizActivity quizActivity = new QuizActivity();
        return quizActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        levels = database.getReference("Level");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.activity_quiz,container,false);

        listLevel = (RecyclerView)myFragment.findViewById(R.id.listView);
        listLevel.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        listLevel.setLayoutManager(layoutManager);

        loadLevels();

        return myFragment;
    }

    private void loadLevels() {

        adapter = new FirebaseRecyclerAdapter<Level, LevelViewHolder>(Level.class, R.layout.level_layout,
                                                        LevelViewHolder.class, levels) {
            @Override
            protected void populateViewHolder(LevelViewHolder viewHolder, final Level model, int position) {
                viewHolder.level_name.setText(model.getName());
                Picasso.with(getActivity()).load(model.getImage()).into(viewHolder.level_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent openMenu = new Intent(getActivity(), Start.class);
                        Common.levelId = adapter.getRef(position).getKey();
                        startActivity(openMenu);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listLevel.setAdapter(adapter);
    }
}
