package com.dolphin.zanders.Util;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class ValidationUtils {
    private static final String TAG = ValidationUtils.class.getSimpleName();

    public static Observable<Boolean> and(
            Observable<Boolean> a, Observable<Boolean> b) {
        return Observable.combineLatest(a, b,
                new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean valueA, Boolean valueB) throws Exception {
                        return valueA && valueB;
                    }
                });
    }

    public static boolean checkCardChecksum(String number) {
        Log.d(TAG, "checkCardChecksum(" + number + ")");
        final int[] digits = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            digits[i] = Integer.valueOf(number.substring(i, i + 1));
        }
        return checkCardChecksum(digits);
    }

    public static boolean checkCardChecksum(int[] digits) {
        int sum = 0;
        int length = digits.length;
        for (int i = 0; i < length; i++) {

            // Get digits in reverse order
            int digit = digits[length - i - 1];

            // Every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }

    public static boolean isValidCvc(CardType cardType, String cvc) {
        return cvc.length() == cardType.getCvcLength();
    }
}
