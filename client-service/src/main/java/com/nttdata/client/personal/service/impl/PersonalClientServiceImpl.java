package com.nttdata.client.personal.service.impl;

import com.nttdata.client.general.entity.ClientProfiles;
import com.nttdata.client.personal.dto.*;
import com.nttdata.client.personal.entity.PersonalClient;
import com.nttdata.client.personal.repository.PersonalClientRepository;
import com.nttdata.client.personal.service.PersonalClientService;
import com.nttdata.client.util.Util;
import com.nttdata.client.util.handler.exceptions.BadRequestException;
import com.nttdata.client.util.handler.exceptions.NotFoundException;
import com.nttdata.client.util.mapper.PersonalClientModelMapper;
import com.nttdata.client.util.web.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PersonalClientServiceImpl implements PersonalClientService {

    static PersonalClientModelMapper modelMapper = PersonalClientModelMapper.singleInstance();

    public final PersonalClientRepository repository;

    public final WebClientService webClient;

    @Override
    public Flux<PersonalClient> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<PersonalClient> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<PersonalClient> getByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to get an item.",
                        "The personal client with document number "+documentNumber+" does not exists.",
                        getClass(),
                        "getByDocumentNumber.switchIfEmpty"
                )))
                .flatMap( client -> {

                    return Flux.fromIterable(client.getAccounts())
                            .flatMap( product -> {

                                return webClient
                                        .getWebClient("searching transactions for product with id "+product.getAccountId())
                                        .get()
                                        .uri("transactions/" + product.getAccountId() )
                                        .retrieve()
                                        .bodyToMono(BigDecimal.class)
                                        .switchIfEmpty(Mono.error(new NotFoundException(
                                                "ID",
                                                "Account with id "+ product.getAccountId() +" does not have transactions.",
                                                "An error occurred while trying to get personal client account information.",
                                                getClass(),
                                                "getByDocumentNumber.map.switchIfEmpty"
                                        )))
                                        .flatMap( balance -> {
                                            product.setBalance(balance);
                                            return Mono.just(product);
                                        })
                                        .thenReturn(product);
                            })
                            .then(Mono.just(client));
                } )
                .onErrorResume( e -> Mono.error(e))
                .cast(PersonalClient.class);

    }

    @Override
    public Mono<PersonalClient> save(CreatePersonalClientDto o) {

        return repository.findByDocumentNumber(o.getDocumentNumber())
                .map( p -> {
                    throw new BadRequestException(
                            "DocumentNumber",
                            "[save] The document number "+o.getDocumentNumber()+ " is already in use.",
                            "An error occurred while trying to create an item.",
                            getClass(),
                            "save"
                    );
                } )
                .switchIfEmpty(Mono.defer(() -> {

                    Util.isNumber(o.getDocumentNumber(), "DocumentNumber", getClass(), "save");

                    Util.verifyDocumentNumber(
                            o.getDocumentType(),
                            o.getDocumentNumber(),
                            getClass(),
                            "save"
                    );

                    ClientProfiles.verifyPersonalProfiles(o.getProfile(), getClass(), "create.verifyProfile");

                    o.getAccounts().forEach( acc -> Util.verifyCurrency(acc.getAccountIsoCurrencyCode(), getClass()));

                    return repository.save(modelMapper.reverseMapCreateWithDate(o));

                }))
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ERROR",
                        "An error occurred while trying to create an item.",
                        e.getMessage(),
                        getClass(),
                        "save.onErrorResume"
                )))
                .cast(PersonalClient.class);


    }

    @Override
    public Mono<PersonalClient> addAccount(String documentNumber, CreatePersonalClientAccountDto o) {

        return repository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new Exception("An item with the document number " + documentNumber + " was not found. >> switchIfEmpty")))
                .flatMap( p -> {

                    Util.verifyCurrency(o.getAccountIsoCurrencyCode(), getClass());

                    return repository.save(modelMapper.reverseMapAddAccounts(p, o));
                } )
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to update an item.",
                        e.getMessage(),
                        getClass(),
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<PersonalClient> updateAccount(String documentNumber, UpdatePersonalClientAccountDto o) {

        return repository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new Exception("An item with the document number " + documentNumber + " was not found. >> switchIfEmpty")))
                .flatMap( p -> {

                    Util.verifyCurrency(o.getAccountIsoCurrencyCode(), getClass());

                    return repository.save(modelMapper.reverseMapUpdateAccounts(p, o));
                } )
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to update an item.",
                        e.getMessage(),
                        getClass(),
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<PersonalClient> deleteAccount(String documentNumber, String accountId) {

        return repository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new Exception("An item with the document number " + documentNumber + " was not found. >> switchIfEmpty")))
                .flatMap( p -> repository.save(modelMapper.reverseMapDeleteAccounts(p, accountId)) )
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to delete an item.",
                        e.getMessage(),
                        getClass(),
                        "delete.onErrorResume"
                )));
    }

    @Override
    public Mono<PersonalClient> update(UpdatePersonalClientDto o) {

        return repository.findByDocumentNumber(o.getDocumentNumber())
                .switchIfEmpty(Mono.error(new Exception("An item with the document number " + o.getDocumentNumber() + " was not found. >> switchIfEmpty")))
                .flatMap( p -> {

                    ClientProfiles.verifyPersonalProfiles(o.getProfile(), getClass(), "update.verifyProfile");

                    return repository.save(modelMapper.reverseMapUpdate(p, o));
                } )
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to update an item.",
                        e.getMessage(),
                        getClass(),
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<PersonalClient> delete(DeletePersonalClientDto o) {

        return repository.findByDocumentNumber(o.getDocumentNumber())
                .switchIfEmpty(Mono.error(new Exception("An item with the document number " + o.getDocumentNumber() + " was not found. >> switchIfEmpty")))
                .flatMap( p -> repository.save(modelMapper.reverseMapDelete(p, o)))
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to delete an item.",
                        e.getMessage(),
                        getClass(),
                        "update.onErrorResume"
                )));
    }
}