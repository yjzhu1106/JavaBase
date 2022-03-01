package com.shmtu.proxy.keyAggregateCrypto;

import com.shmtu.keyAggregateCrypto.KacRevokedShema;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class KACTest {

    KacRevokedShema kacRevokedShema = new KacRevokedShema();

    @Test
    public void testSetUp() {
        int lamada = 128;
        int fileNum = 3;

        HashMap<String, ArrayList<Element>> setUp_result = kacRevokedShema.setUp(lamada, fileNum);
        ArrayList<Element> params = setUp_result.get("params");
        ArrayList<Element> msk = setUp_result.get("msk");
        ArrayList<Element> mpk = setUp_result.get("mpk");

        // 结果为pub = {pk1, pk2, ...., pkn}
        HashMap<Integer, ArrayList<Element>> pub = kacRevokedShema.keyGen(lamada, params, msk);


        // 加密class 2, i = 2;
        ArrayList<Element> encrypt_2 = kacRevokedShema.encrypt(pub.get(2), new BigInteger("2"), mpk, params);
//        ArrayList<Element> encrypt_1 = kacRevokedShema.encrypt(pub.get(1), new BigInteger("1"), mpk, params);
//        ArrayList<Element> encrypt_3 = kacRevokedShema.encrypt(pub.get(3), new BigInteger("3"), mpk, params);

        // return extract sk;
        int[] s_k = new int[]{1, 2, 3};
        ArrayList<Element> k_sk = kacRevokedShema.extract(s_k, params, mpk, msk, pub);

        byte[] decrypt = kacRevokedShema.decrypt(k_sk, params, mpk, pub, s_k, encrypt_2, 2);

        System.out.println("end...");



    }


    @Test
    public void test(){
        BigInteger bigInteger = new BigInteger("11223344556677889910111213141516");
        System.out.println(bigInteger);
    }
}
