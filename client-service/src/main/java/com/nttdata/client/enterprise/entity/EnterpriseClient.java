package com.nttdata.client.enterprise.entity;

// import com.nttdata.client.general.GenericProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class EnterpriseClient {

    @Id
    private String id;
    private String ruc;
    private String companyName;
    private String legalResidence;
    // private List<GenericProduct> accounts;
    private String profile;

    private short registrationStatus;
    private Date insertionDate;
    private String fk_insertionUser;
    private String insertionTerminal;

}
