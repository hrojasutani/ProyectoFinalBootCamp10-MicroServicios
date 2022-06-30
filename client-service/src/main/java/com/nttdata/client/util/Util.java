package com.nttdata.client.util;

import com.nttdata.client.personal.entity.DocumentType;
import com.nttdata.client.personal.entity.PersonalDocument;
import com.nttdata.client.util.handler.exceptions.BadRequestException;

import java.util.Currency;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static boolean isValidCurrency(String code){
        try {
            Currency c = Currency.getInstance(code);
            if (c != null) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void log(Class context_class, Level level, String message, Object[] params) {
        
        Logger logger = Logger.getLogger(String.valueOf(context_class));
        logger.log(level, message, params);
    }

    public static void log(Class context_class, Level level, String message) {
        log(context_class, level, message, null);
    }

    public static void verifyCurrency(String currencyCode, Class context_class) {
        if(!Util.isValidCurrency(currencyCode)){
            throw new BadRequestException(
                    "CURRENCY",
                    "[CURRENCY] "+currencyCode + " is an invalid currency code.",
                    "",
                    context_class,
                    "update"
            );
        }
    }

    public static boolean verifyRuc(String ruc, String ruc_to_compare, Class context_class, String context) {

        Util.isNumber(ruc, "RUC", context_class, context);

        Util.verifyLength(ruc, 11, "RUC", context_class, context);

        if(!Objects.equals(ruc, ruc_to_compare)) {
            throw new BadRequestException(
                    "RUC",
                    "["+context+"] An item with the RUC " + ruc + " was not found. >> ",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }

        return true;
    }

    public static void verifyLength(String ruc, int length, String type, Class context_class, String context) {
        if(ruc.length() != length) {
            throw new BadRequestException(
                    type.toUpperCase(),
                    "["+context+"] The "+type+" length ("+ruc.length()+ ") should be "+length+" digits.",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }
    }

    public static void isNumber(String n, String type, Class context_class, String context) {
        try {
            Long.parseLong(n);

        } catch (Exception e){
            throw new BadRequestException(
                    type.toUpperCase(),
                    "["+context+"] The "+type+" ("+n+ ") should be a number.",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }
    }

    public static void verifyDocumentNumber(
            String documentType,
            String documentNumber,
            Class context_class,
            String context
    ) {

        DocumentType doc;

        try {

            doc = PersonalDocument.getInstance().getDocumentTypes().get(documentType);
            if(doc == null) throw new Exception("DocumentType is not valid");

        } catch (Exception e) {
            throw new BadRequestException(
                    "DocumentType",
                    "["+context+"] The document type <<"+documentType+ ">> is not valid.",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }

        if(!((documentNumber.length() >= doc.getMinLength()) && (documentNumber.length() <= doc.getMaxLength()))) {
            throw new BadRequestException(
                    "DocumentNumber",
                    "["+context+"] The document number length ("+documentNumber.length()+ ") should be between "+doc.getMinLength()+" and "+doc.getMaxLength()+" digits.",
                    "An error occurred while trying to create an item.",
                    context_class,
                    context
            );
        }

    }

}
