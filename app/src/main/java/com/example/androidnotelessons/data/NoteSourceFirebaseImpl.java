package com.example.androidnotelessons.data;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

ublic class NoteSourceFirebaseImpl implements NoteSource {
    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NoteSoursel]";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private CollectionReference collection = firestore.collection(NOTES_COLLECTION);

    private List<Note> notes = new ArrayList<>();

    @Override
    public NoteSource init(final NoteSourceResponse noteSourceResponse) {
        collection.orderBy(NotesMapping.Fields.CREATION_DATE, Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notes = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> doc = document.getData();
                    String id = document.getId();
                    Note note = NotesMapping.toNote(id, doc);
                    notes.add(note);
                }
                Log.d(TAG, String.format("getting %d quantity", notes.size()));
                noteSourceResponse.initialized(NoteSourceFirebaseImpl.this);
            } else {
                Log.d(TAG, "get failed with", task.getException());
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "get failed with", e);
        });

        return this;
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

    @Override
    public void addNote(final Note note, NoteSourceResponseAdded noteSourceResponse) {
        collection.add(NotesMapping.toDocument(note)).addOnSuccessListener(documentReference -> {
            note.setId(documentReference.getId());
            notes.add(note);
            noteSourceResponse.added();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "get failed with", e);
            }
        });
    }

    @Override
    public void updateNote(int position, Note note, NoteSourceResponseChanged noteSourceResponse) {
        String id = note.getId();
        collection.document(id).set(NotesMapping.toDocument(note)).addOnSuccessListener(aVoid -> {
            notes.set(position, note);
            noteSourceResponse.changed();
        });
    }

    @Override
    public void deleteNote(int position,NoteSourceResponseDelete noteSourceResponse) {
        collection.document(notes.get(position).getId()).delete().addOnSuccessListener(aVoid -> {
            notes.remove(position);
            noteSourceResponse.deleted();
        });
    }

    @Override
    public void clearNotes(NoteSourceResponseDelete noteSourceResponse) {
        int notesQuantity = notes.size() - 1;
        for (int i = notesQuantity; i >= 0; i--) {
            final int fi = i;
            collection.document(notes.get(i).getId()).delete().addOnSuccessListener(aVoid -> {
                notes.remove(fi);
                noteSourceResponse.deleted();
            });
        }
    }
}