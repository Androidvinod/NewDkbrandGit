package com.dolphin.zanders.Util;

import java.util.regex.Pattern;

public enum CardType {
    CREDIT_CARD_TYPE(-1),
    VISA(3),
    MASTER_CARD(3),
    AMERICAN_EXPRESS(4),
    MAESTRO(3),
    DINERS_CLUB(3),
    JCB(3),
    DISCOVER(3),
    UNION_PAY(3),
    MIR(3);

    private final int cvcLength;

    CardType(int cvcLength) {
        this.cvcLength = cvcLength;
    }

    public int getCvcLength() {
        return cvcLength;
    }

    public static CardType fromNumber(String number) {
        if (regVisa
                .matcher(number).matches()) {
            return VISA;
        } else if (regMasterCard
                .matcher(number).matches()) {
            return MASTER_CARD;
        } else if (regAmericanExpress
                .matcher(number).matches()) {
            return AMERICAN_EXPRESS;
        } else if (regMaestro
                .matcher(number).matches()) {
            return MAESTRO;
        } else if (regDiners
                .matcher(number).matches()) {
            return DINERS_CLUB;
        } else if (regJcb
                .matcher(number).matches()) {
            return JCB;
        } else if (regDiscover
                .matcher(number).matches()) {
            return DISCOVER;
        } else if (regUnionPay
                .matcher(number).matches()) {
            return UNION_PAY;
        } else if (regMir
                .matcher(number).matches()) {
            return MIR;
        }
        return CREDIT_CARD_TYPE;
    }

    private static Pattern regVisa =
            Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
    private static Pattern regMasterCard =
            Pattern.compile("^5[1-5][0-9]{14}$");
    private static Pattern regAmericanExpress =
            Pattern.compile("^3[47][0-9]{13}$");
    private static Pattern regMaestro =
            Pattern.compile("^(?:5[0678]\\\\d\\\\d|6304|6390|67\\\\d\\\\d)\\\\d{8,15}$");
    private static Pattern regDiners =
            Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{4,}$");
    private static Pattern regJcb =
            Pattern.compile("^(?:2131|1800|35[0-9]{3})[0-9]{3,}$");
    private static Pattern regDiscover =
            Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{3,}$");
    private static Pattern regUnionPay =
            Pattern.compile("^62[0-5]\\\\d{13,16}$");
    private static Pattern regMir =
            Pattern.compile("^22[0-9]{1,14}$");
}
