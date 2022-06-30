package com.nttdata.client.personal.dto;

import com.nttdata.client.general.entity.GenericProduct;
import lombok.Data;

import java.util.List;

@Data
public class CreatePersonalClientDto {

    /*@Id
    private String id;*/

    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String residenceAddress;
    private List<GenericProduct> accounts;
    private String profile;

    //private short registrationStatus;
    //private Date insertionDate;
    private String fk_insertionUser;
    private String insertionTerminal;

}