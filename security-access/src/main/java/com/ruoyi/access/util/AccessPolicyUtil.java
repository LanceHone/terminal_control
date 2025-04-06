package com.ruoyi.access.util;

import java.math.BigInteger;

public class AccessPolicyUtil {
    public static final Integer[] rangeSplit(String rangeStr) {
//    public static final BigInteger[] rangeSplit(String rangeStr) {
        String[] range = rangeStr.split("～");
        Integer[] rangeArr = new Integer[2];
        rangeArr[0] = new Integer(range[0]);
//        rangeArr[0] = new BigInteger(range[0]);
        rangeArr[1] = new Integer(range[1]);
        return rangeArr;
    }
}
