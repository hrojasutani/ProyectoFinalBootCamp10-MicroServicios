package com.nttdata.client.general.entity;

import com.nttdata.client.util.handler.exceptions.BadRequestException;

import java.util.Arrays;
import java.util.List;

public class ClientProfiles {

    private static List<String> enterpriseProfiles = Arrays.asList(
            "ESTANDAR",
            "PYME"
    );

    private static List<String> personalProfiles = Arrays.asList(
            "ESTANDAR",
            "VIP"
    );

    public static void verifyEnterpriseProfiles(String profile, Class context_class, String context) {

        if(!enterpriseProfiles.contains(profile)) {
            throw new BadRequestException(
                    "EPROFILE",
                    "["+context+"] An item with the profile " + profile + " was not found. >> ",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }

    }

    public static List<String> getEnterpriseProfiles() {
        return enterpriseProfiles;
    }

    public static void verifyPersonalProfiles(String profile, Class context_class, String context) {

        if(!personalProfiles.contains(profile)) {
            throw new BadRequestException(
                    "PPROFILE",
                    "["+context+"] An item with the profile " + profile + " was not found. >> ",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }

    }

    public static List<String> getPersonalProfiles() {
        return personalProfiles;
    }

}
