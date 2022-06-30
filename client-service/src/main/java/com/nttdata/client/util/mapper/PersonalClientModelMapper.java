package com.nttdata.client.util.mapper;

import com.nttdata.client.general.entity.GenericProduct;
import com.nttdata.client.personal.dto.*;
import com.nttdata.client.personal.entity.PersonalClient;
import com.nttdata.client.util.handler.exceptions.BadRequestException;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonalClientModelMapper {

    private final ModelMapper mapper = new ModelMapper();

    private static PersonalClientModelMapper instance;

    private PersonalClientModelMapper() { }

    public static PersonalClientModelMapper singleInstance() {
        if ( instance == null ) {
            instance = new PersonalClientModelMapper();
        }
        return instance;
    }

    //MAPPERS BEGIN
    public PersonalClient reverseMapCreateWithDate(CreatePersonalClientDto createDto) {
        PersonalClient o = mapper.map(createDto, PersonalClient.class);

        o.setInsertionDate(new Date());
        o.setRegistrationStatus((short) 1);

        return o;
    }

    public PersonalClient reverseMapAddAccounts(PersonalClient p, CreatePersonalClientAccountDto o) {

        GenericProduct acc = mapper.map(o, GenericProduct.class);

        acc.setAccountInsertionDate(new Date());
        acc.setAccountRegistrationStatus((short) 1);

        p.getAccounts().add(acc);

        return p;

    }

    public PersonalClient reverseMapUpdateAccounts(PersonalClient p, UpdatePersonalClientAccountDto o) {

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

    public PersonalClient reverseMapDeleteAccounts(PersonalClient p, String accountId) {

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

    public PersonalClient reverseMapUpdate(PersonalClient p, UpdatePersonalClientDto updateDto) {

        p.setProfile(updateDto.getProfile());
        p.setFirstName(updateDto.getFirstName());
        p.setLastName(updateDto.getLastName());
        p.setResidenceAddress(updateDto.getResidenceAddress());

        return p;
    }

    public PersonalClient reverseMapDelete(PersonalClient p, DeletePersonalClientDto deleteDto) {

        p.setRegistrationStatus((short) 0);

        return p;
    }

}