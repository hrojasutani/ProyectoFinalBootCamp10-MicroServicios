package com.nttdata.client.enterprise.dto;

// import com.nttdata.client.general.GenericProduct;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class UpdateEnterpriseClientDto {

    @Id
    private String id;
    private String ruc;

    private String legalResidence;
    // private List<GenericProduct> accounts;
    private String profile;


}