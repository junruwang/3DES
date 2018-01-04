package com.guoguang.test3des;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import gybank.psam.api.Encrypt3DES;

import static com.guoguang.test3des.CHexConver.byte2HexStr;

public class MainActivity extends AppCompatActivity {

    private static final String ENCRYPTION_MANNER = "DESede/ECB/NoPadding";
    private Button test, psw;
    private TextView show;

    private Encrypt3DES encrypt3DES;
    private IdentifyPsamCard identifyPsamCard;

    private static final String TAG = "mainactivity";
    String km = "00000000000000000000000000000000";
    String x = "1111111111111222";

    String compare = "36F9266464F0236A12561FDF45914A06";
    String testData = "1234567888132412";
    String str = "60D2F75E945ED2EE";
    String encodeString_ECB = "";
    String encodeString_ECB2 = "";
    byte[] kc;
    byte[] kcl;
    byte[] kcr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (Button) findViewById(R.id.test);
        psw = (Button) findViewById(R.id.psw);
        show = (TextView) findViewById(R.id.show);
        encrypt3DES=new Encrypt3DES();
        identifyPsamCard=new IdentifyPsamCard();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    encrypt3DES=new Encrypt3DES();
                    byte[] data=encrypt3DES.encrypt(CHexConver.hexStringToBytes(testData),16,CHexConver.hexStringToBytes(compare),32,1);
                    //show.setText(Util.byteToHex(data));
                    show.setText(CHexConver.byte2HexStr(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        psw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String a=identifyPsamCard.identify(km,x,testData);
                show.setText(a);
            }
        });


    }

    /**
     * 加密
     *
     * @param data 加密数据
     * @param key  加密密码
     * @return
     * @throws Exception
     */
    public static byte[] encrypt3DESECB(byte[] data, byte[] key) throws Exception {
         DESedeKeySpec dks = new DESedeKeySpec(key);
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
         SecretKey secretKey = keyFactory.generateSecret(dks);
         Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] b = cipher.doFinal(data);
        Log.d(TAG, "3des" + b.length);
        return b;
    }

}
