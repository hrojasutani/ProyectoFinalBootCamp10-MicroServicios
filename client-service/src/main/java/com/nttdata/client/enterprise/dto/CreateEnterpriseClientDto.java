package com.nttdata.client.enterprise.dto;

// import com.nttdata.client.general.GenericProduct;
import lombok.Data;
import java.util.List;

@Data
public class CreateEnterpriseClientDto {

    /*@Id
    private String id;*/
    private String ruc;
    private String companyName;
    private String legalResidence;
    // private List<GenericProduct> accounts;
    private String profile;

    //private short registrationStatus;
    //private Date insertionDate;
    private String fk_insertionUser;
    private String insertionTerminal;

}