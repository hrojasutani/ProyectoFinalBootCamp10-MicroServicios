package com.nttdata.client.general.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class GenericProduct {

    @Id
    private String accountId;
    private String accountType;
    private String accountUrl;
    private String accountIsoCurrencyCode;
    private BigDecimal balance;

    private short accountRegistrationStatus;
    private Date accountInsertionDate;
    private String accountFk_insertionUser;
    private String accountInsertionTerminal;

}
