package com.shmtu.proxy.KeyAggregateCrypto2014;

import com.shmtu.KeyAggregateCrypto2014.KacShema;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

public class KacTest {

    @Test
    public void testVerify() {
        for (int i = 0; i < 50; i++) {
            System.out.println("----------" + i + " times----------------------------");
            test1(16, 183);
        }

    }

    @Test
    public void testFindRBitAndQBit() {
        for (int i = 16; i < 256; ) {

            for (int j = i; j < 512; j++) {
                System.out.println("--------rBit = " + i + ", qBit = " + j + " --------------------");
                test1(i, j);
            }
            i = i + 16;
        }

    }

    public void test1(int rBit, int qBit) {
        KacShema kacShema = new KacShema(rBit, qBit);
        // init
        kacShema.initCurveByDynamic();
        // setUp --> params = {g, g1, g2, ... , gn, gn+2, .. , g2n}
        // Suppose the file class is 3, i =>{1, 2, 3}
        kacShema.setUp(3);
        Map<String, Element> stringElementMap = kacShema.keyGen();
        Element pk = stringElementMap.get("pk");
        Element msk = stringElementMap.get("msk");
        //QmdQaC35hfGe2Xt6LxteUQLGsMXkxsW7pF4hgat8UndTkm
        //QmZULkCELmmk5XNfCgTnCyFgAVxBRBXyDHGGMVoLFLiXEN
        ArrayList<Element> C_1 = kacShema.encrypt(pk, 1, "QmdQaC35hfGe2Xt6LxteUQLGsMXkxsW7pF4hgat8UndTkm");
        Element K_s = kacShema.extract(msk, new int[]{1, 2});

        byte[] decrypt = kacShema.decrypt(K_s, new int[]{1, 2}, 1, C_1);
        System.out.println("des: " + new String(decrypt));
    }
}
