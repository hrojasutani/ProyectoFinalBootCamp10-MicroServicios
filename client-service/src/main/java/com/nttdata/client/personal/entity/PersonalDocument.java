package com.nttdata.client.personal.entity;

import lombok.Data;

import java.util.HashMap;

@Data
public class PersonalDocument {

    private HashMap<String, DocumentType> documentTypes = new HashMap<String, DocumentType>();

    private static PersonalDocument instance;

    private PersonalDocument() {
        documentTypes.put("LE", new DocumentType("LIBRETA ELECTORAL",8,8));
        documentTypes.put("DNI", new DocumentType("DOCUMENTO NACIONAL DE IDENTIDAD",8,8));
        documentTypes.put("CE", new DocumentType("CARNET DE EXTRANJERIA",9,9));
        documentTypes.put("CR", new DocumentType("CARNET DE REFUGIADO",12,12));
        documentTypes.put("CI", new DocumentType("CARNET DE IDENTIDAD",12,12));
        documentTypes.put("PASAPORTE", new DocumentType("PASAPORTE",12,12));
    }

    public static PersonalDocument getInstance() {
        if ( instance == null ) {
            instance = new PersonalDocument();
        }
        return instance;
    }

}
