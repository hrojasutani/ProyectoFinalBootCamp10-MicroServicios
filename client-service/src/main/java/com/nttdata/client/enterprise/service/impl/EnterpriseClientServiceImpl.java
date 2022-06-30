package com.nttdata.client.enterprise.service.impl;

import com.nttdata.client.enterprise.dto.*;
import com.nttdata.client.enterprise.entity.EnterpriseClient;
import com.nttdata.client.enterprise.repository.EnterpriseClientRepository;
import com.nttdata.client.enterprise.service.EnterpriseClientService;
import com.nttdata.client.general.entity.ClientProfiles;
import com.nttdata.client.util.Util;
import com.nttdata.client.util.handler.exceptions.BadRequestException;
import com.nttdata.client.util.mapper.EnterpriseClientModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EnterpriseClientServiceImpl implements EnterpriseClientService {

    // static EnterpriseClientModelMapper modelMapper = EnterpriseClientModelMapper.singleInstance();

    public final EnterpriseClientRepository repository;

    @Override
    public Flux<EnterpriseClient> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<EnterpriseClient> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<EnterpriseClient> getByRuc(String ruc) {
        return repository.findByRuc(ruc);
    }

    @Override
    public Mono<EnterpriseClient> save(CreateEnterpriseClientDto o) {

        return repository.findByRuc(o.getRuc())
                .map( p -> {
                    throw new BadRequestException(
                                "RUC",
                                "[save] The RUC number "+o.getRuc()+ " is already in use.",
                                "An error occurred while trying to create an item.",
                                getClass(),
                                "save"
                        );
                } )
                .switchIfEmpty(Mono.defer(() -> {

                    Util.verifyRuc(o.getRuc(), o.getRuc(), getClass(), "save.verifyRuc");
                    ClientProfiles.verifyEnterpriseProfiles(o.getProfile(), getClass(), "save.verifyProfile");

                    o.getAccounts().forEach( acc -> Util.verifyCurrency(acc.getAccountIsoCurrencyCode(), getClass()));

                    return repository.save(modelMapper.reverseMapCreateWithDate(o));

                }))
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to create an item.",
                        e.getMessage(),
                        getClass(),
                        "save.onErrorResume"
                )))
                .cast(EnterpriseClient.class);


    }

    @Override
    public Mono<EnterpriseClient> addAccount(String ruc, CreateEnterpriseClientAccountDto o) {

        return repository.findByRuc(ruc)
                .switchIfEmpty(Mono.error(new Exception("An item with the ruc " + ruc + " was not found. >> switchIfEmpty")))
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
    public Mono<EnterpriseClient> updateAccount(String ruc, UpdateEnterpriseClientAccountDto o) {

        return repository.findByRuc(ruc)
                .switchIfEmpty(Mono.error(new Exception("An item with the ruc " + ruc + " was not found. >> switchIfEmpty")))
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
    public Mono<EnterpriseClient> deleteAccount(String ruc, String accountId) {

        return repository.findByRuc(ruc)
                .switchIfEmpty(Mono.error(new Exception("An item with the ruc " + ruc + " was not found. >> switchIfEmpty")))
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
    public Mono<EnterpriseClient> update(UpdateEnterpriseClientDto o) {

        return repository.findById(o.getId())
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + o.getId() + " was not found. >> switchIfEmpty")))
                .flatMap( p -> {

                    Util.verifyRuc(o.getRuc(), p.getRuc(), getClass(),"update.flatMap");
                    ClientProfiles.verifyEnterpriseProfiles(o.getProfile(), getClass(), "update.verifyProfile");

                    o.getAccounts().forEach( acc -> Util.verifyCurrency(acc.getAccountIsoCurrencyCode(), getClass()));

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
    public Mono<EnterpriseClient> delete(DeleteEnterpriseClientDto o) {

        return repository.findById(o.getId())
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + o.getId() + " was not found. >> switchIfEmpty")))
                .flatMap( p -> {

                    if( !p.getRuc().equals(o.getRuc()) ) {
                        return Mono.error(new Exception("An item with the RUC " + o.getRuc() + " was not found. >> onFlatMap"));
                    }

                    return repository.save(modelMapper.reverseMapDelete(p, o));

                } )
                .onErrorResume( e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to delete an item.",
                        e.getMessage(),
                        getClass(),
                        "update.onErrorResume"
                )));
    }
}