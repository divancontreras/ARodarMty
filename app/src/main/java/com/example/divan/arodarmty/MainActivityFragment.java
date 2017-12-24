package com.example.divan.arodarmty;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.divan.arodarmty.model.Rodada;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView recyclerview;

    private FirestoreRecyclerAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerview = view.findViewById(R.id.recyclerview);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("rodadas").limit(50);

        FirestoreRecyclerOptions<Rodada> options = new FirestoreRecyclerOptions.Builder<Rodada>()
                .setQuery(query, Rodada.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Rodada, RodadaHolder>(options) {
            @Override
            public void onBindViewHolder(RodadaHolder holder, int position, Rodada rodada) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.bindData(rodada);
            }

            @Override
            public RodadaHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_rodada, group, false);

                return new RodadaHolder(view);
            }
        };

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    static class RodadaHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private TextView description;
        private TextView kilometers;

        public RodadaHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            kilometers = itemView.findViewById(R.id.date);
            date = itemView.findViewById(R.id.kilometers);


        }

        public void bindData(Rodada rodada){
            title.setText(rodada.getTitle());
            description.setText(rodada.getDescription());
            kilometers.setText(String.valueOf(rodada.getKilometers()));
            date.setText(String.valueOf(rodada.getDate()));


        }
    }
}
