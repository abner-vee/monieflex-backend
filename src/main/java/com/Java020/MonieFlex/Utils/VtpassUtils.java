package com.Java020.MonieFlex.Utils;

public class VtpassUtils {

    public static final String PURCHASE_PRODUCT = "pay";
    public static final String QUERY_TRANSACTION_STATUS = "requery";
    public static final String VERIFY_METER_NUMBER = "merchant-verify";
    public static String VARIATION_URL(String id) {
        return "/service-variations?serviceID=%s".formatted(id);
    };
}
