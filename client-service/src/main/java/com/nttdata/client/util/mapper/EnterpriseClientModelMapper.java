package com.nttdata.client.util.mapper;

import com.nttdata.client.enterprise.dto.*;
import com.nttdata.client.enterprise.entity.EnterpriseClient;
import com.nttdata.client.general.entity.GenericProduct;
import com.nttdata.client.util.handler.exceptions.BadRequestException;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EnterpriseClientModelMapper {

    private final ModelMapper mapper = new ModelMapper();

    private static EnterpriseClientModelMapper instance;

    private EnterpriseClientModelMapper() { }

    public static EnterpriseClientModelMapper singleInstance() {
        if ( instance == null ) {
            instance = new EnterpriseClientModelMapper();
        }
        return instance;
    }

    //MAPPERS BEGIN
    public EnterpriseClient reverseMapCreateWithDate(CreateEnterpriseClientDto createDto) {
        EnterpriseClient o = mapper.map(createDto, EnterpriseClient.class);

        o.setInsertionDate(new Date());
        o.setRegistrationStatus((short) 1);

        return o;
    }

    public EnterpriseClient reverseMapAddAccounts(EnterpriseClient p, CreateEnterpriseClientAccountDto o) {

        GenericProduct acc = mapper.map(o, GenericProduct.class);

        acc.setAccountInsertionDate(new Date());
        acc.setAccountRegistrationStatus((short) 1);

        p.getAccounts().add(acc);

        return p;

    }

    public EnterpriseClient reverseMapUpdateAccounts(EnterpriseClient p, UpdateEnterpriseClientAccountDto o) {

        List<Integer> counter = new ArrayList<>();

        p.setAccounts(
                p.getAccounts().stream().map(a -> {
                            if( a.getAccountId().equals(o.getAccountId()) ) {
                                a.setAccountType(o.getAccountType());
                                a.setAccountUrl(o.getAccountUrl());
                                a.setAccountIsoCurrencyCode(o.getAccountIsoCurrencyCode());

                                counter.add(1);
                            }

                            return a;
                        })
                        .collect(Collectors.toList())
        );

        if(counter.isEmpty()) throw new BadRequestException(
                "ERROR",
                "The account with id " + o.getAccountId() + " does not exist",
                "An error occurred while trying to update an item.",
                getClass(),
                "update.onReverseMapping"
        );

        return p;

    }

    public EnterpriseClient reverseMapDeleteAccounts(EnterpriseClient p, String accountId) {

        List<Integer> counter = new ArrayList<>();

        p.setAccounts(
                p.getAccounts().stream().map(a -> {
                            if( a.getAccountId().equals(accountId) ) {
                                counter.add(1);
                                return null;
                            }
                            return a;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );

        if(counter.isEmpty()) {
            throw new BadRequestException(
                    "ERROR",
                    "The account with id " + accountId + " does not exist",
                    "An error occurred while trying to delete an item.",
                    getClass(),
                    "delete.onReverseMapping"
            );
        }

        return p;

    }

    public EnterpriseClient reverseMapUpdate(EnterpriseClient p, UpdateEnterpriseClientDto updateDto) {

        p.setProfile(updateDto.getProfile());
        p.setAccounts(updateDto.getAccounts());
        p.setLegalResidence(updateDto.getLegalResidence());

        return p;
    }

    public EnterpriseClient reverseMapDelete(EnterpriseClient p, DeleteEnterpriseClientDto deleteDto) {

        p.setRegistrationStatus((short) 0);

        return p;
    }

}
