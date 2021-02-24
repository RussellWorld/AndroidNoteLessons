package com.example.androidnotelessons.data;

public interface NoteSource {
    NoteSource init(NoteSourceResponse noteSourceResponse);
    Note getNote(int position);

    int size();

    void addNote(Note note, NoteSourceResponseAdded noteSourceResponse);

    void updateNote(int position, Note note, NoteSourceResponseChanged noteSourceResponse);

    void deleteNote(int position, NoteSourceResponseDelete noteSourceResponse);

    void clearNotes(NoteSourceResponseDelete noteSourceResponse);
}
