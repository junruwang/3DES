package com.guoguang.test3des;

import android.util.Log;

import gybank.psam.api.Encrypt3DES;

/**
 * Created by 40303 on 2017/9/18.
 */

public class IdentifyPsamCard {
    private static final String TAG = "IdentifyPsamCard";
    private Encrypt3DES encrypt3DES;
    byte[] kcl;
    byte[] kcr;

    public String identify(String mainKey, String factor, String data) {
        int mainKeyLen = mainKey.length();
        int factorLen = factor.length();
        int dataLen = data.length();
        encrypt3DES=new Encrypt3DES();

        //分散因子取反
        byte[] factor2 = CHexConver.hexStringToBytes(factor);
        byte temp;
        int i;
        for (i = 0; i < factor2.length; i++) {
            temp = factor2[i];
            factor2[i] = (byte) ~temp;
        }
        Log.d(TAG, "factor2=" + CHexConver.byte2HexStr(factor2));

        //获得左子密钥
        kcl = encrypt3DES.encrypt(CHexConver.hexStringToBytes(factor), factorLen, CHexConver.hexStringToBytes(mainKey), mainKeyLen, 1);
        if (CHexConver.byte2HexStr(kcl).equals("FFFFFF")) {
            return "-1";
        }
        Log.d(TAG, "kcl=" + CHexConver.byte2HexStr(kcl));

        //获得右子密钥
        kcr = encrypt3DES.encrypt(factor2, factorLen, CHexConver.hexStringToBytes(mainKey), mainKeyLen, 1);
        if (CHexConver.byte2HexStr(kcr).equals("FFFFFF")) {
            return "-1";
        }
        Log.d(TAG, "kcr=" + CHexConver.byte2HexStr(kcr));

        //移位或，组成16字节子密钥
        String subKey = CHexConver.byte2HexStr(kcl) + CHexConver.byte2HexStr(kcr);
        Log.d(TAG, "subKey=" + subKey);

        //子密钥加密数据
        byte[] receiveData = encrypt3DES.encrypt(CHexConver.hexStringToBytes(data), dataLen, CHexConver.hexStringToBytes(subKey), subKey.length(), 1);
        if (CHexConver.byte2HexStr(receiveData).equals("FFFFFF")) {
            return "-1";
        }
        Log.d(TAG, "receiveData=" + CHexConver.byte2HexStr(receiveData));

        return CHexConver.byte2HexStr(receiveData);
    }

}
