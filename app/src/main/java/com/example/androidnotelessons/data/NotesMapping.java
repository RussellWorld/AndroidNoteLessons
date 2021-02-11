package com.example.androidnotelessons.data;

import java.util.HashMap;
import java.util.Map;

public class NotesMapping {
    public static class Fields {
        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String CREATION_DATE = "creationDate";
        public final static String IS_IMPORTANT = "isImportant";
        public final static String CONTENT = "content";
    }

    public static Note toNote(String id, Map<String, Object> doc) {
        Note answer = new Note((String) doc.get(Fields.NAME), (String) doc.get(Fields.DESCRIPTION),
                (long) doc.get(Fields.CREATION_DATE), (boolean) doc.get(Fields.IS_IMPORTANT), (String) doc.get(Fields.CONTENT));
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(Note note) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.NAME, note.getName());
        answer.put(Fields.DESCRIPTION, note.getDescription());
        answer.put(Fields.CREATION_DATE, note.getCreationDateUnixTime());
        answer.put(Fields.IS_IMPORTANT, note.getIsImportant());
        answer.put(Fields.CONTENT, note.getContent());
        return answer;
    }
}
