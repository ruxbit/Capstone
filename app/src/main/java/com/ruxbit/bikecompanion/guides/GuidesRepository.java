package com.ruxbit.bikecompanion.guides;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruxbit.bikecompanion.model.Guide;

import java.util.ArrayList;
import java.util.List;

public class GuidesRepository {
    private FirebaseFirestore db;

    public GuidesRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getGuides(MutableLiveData<List<Guide>> guides) {
        db.collection("Guides").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Guide> guideList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        guideList.add(new Guide(document.getString("name"), document.getString("url")));
                    }
                    guides.postValue(guideList);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
