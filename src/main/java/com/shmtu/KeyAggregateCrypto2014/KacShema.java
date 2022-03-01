package com.shmtu.KeyAggregateCrypto2014;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KacShema {


    Pairing pairing;
    Field zr;
    Field g1;
    Field g2;
    Field gt;

    Element g;
    ArrayList<Element> param;
    int n, rBit, qBit;

    public KacShema(int rBit, int qBit){
        this.rBit = rBit;
        this.qBit = qBit;
    }

    public Pairing initCurveByDynamic() {
        /*
        rBit是 Zp 中阶数 p 的比特长度；和密钥长度有关
        qBit是 G 中阶数的比特长度，和线性群G1的大小有关
         */
//        int rBit = 8;
//        int qBit = 16;
        TypeACurveGenerator pg = new TypeACurveGenerator(rBit, qBit);
        PairingParameters typeAParams = pg.generate();
        this.pairing = PairingFactory.getPairing(typeAParams);
        this.zr = pairing.getZr();
        this.g1 = pairing.getG1();
        this.g2 = pairing.getG2();
        this.gt = pairing.getGT();
        this.g = g1.newRandomElement().getImmutable();
        return pairing;
    }


    public void setUp(int n) {
        // i = {0, 1, 2, ..., n}
        this.n = n;
        Element a = zr.newRandomElement().getImmutable();
        param = new ArrayList<>();
        for (int i = 0; i <= 2 * n; i++) {
            if (i == 0) {
                param.add(g);
            }
            if (i == n + 1) {
                continue;
            }
            Element a_pow_i = a.pow(BigInteger.valueOf(i));
            param.add(g.powZn(a_pow_i));
        }

    }

    public Map<String, Element> keyGen() {
        Element r = zr.newRandomElement().getImmutable();
        Element pk = g.powZn(r);
        Map<String, Element> res = new HashMap<>();
        res.put("pk", pk);
        res.put("msk", r);
        return res;
    }

    public ArrayList<Element> encrypt(Element pk, int i, String message) {
        byte[] message_bytes = message.getBytes(StandardCharsets.UTF_8);
//        Element m = gt.newElementFromHash(message_bytes, 0, message_bytes.length);
        Element m = gt.newElementFromBytes(message_bytes);
        System.out.println("raw: " + new String(message.getBytes(StandardCharsets.UTF_8)));
        System.out.println("ens: " + new String(m.toBytes()));
        Element t = zr.newRandomElement().getImmutable();

        ArrayList<Element> C_i = new ArrayList<>();

        Element c1 = g.powZn(t);
        Element c2 = pk.mul(param.get(i)).powZn(t);
        Element c3 = m.mul(pairing.pairing(param.get(1).powZn(t), param.get(n)));

        C_i.add(c1);
        C_i.add(c2);
        C_i.add(c3);
        return C_i;
    }

    public Element extract(Element msk, int[] S) {
        Element K_s = null;
        for (int j : S) {
            Element g_r = param.get(n + 1 - j).powZn(msk);
            if (K_s == null) {
                K_s = g_r;
            } else {
                K_s = K_s.mul(g_r);
            }
        }
        return K_s;
    }

    public byte[] decrypt(Element K_s, int[] S, int i, ArrayList<Element> C) {


        Element down_firstParams = null;
        for (int j : S) {
            Element g_r = param.get(n + 1 - j);
            if (down_firstParams == null) {
                down_firstParams = g_r;
            } else {
                down_firstParams = down_firstParams.mul(g_r);
            }
        }
        Element down = pairing.pairing(down_firstParams, C.get(1));

        Element up_firstParams_1 = null;
        for (int j : S) {
            Element g_nij = param.get(n + 1 - j - i);
            if (i == j) {
                continue;
            }
            if (up_firstParams_1 == null) {
                up_firstParams_1 = g_nij;
            } else {
                up_firstParams_1 = up_firstParams_1.mul(g_nij);
            }
        }
        Element up_firstParam = pairing.pairing(up_firstParams_1, C.get(0));
        Element m = C.get(2).mul(up_firstParam).div(down);
        byte[] bytes = m.toBytes();
        return bytes;

    }


}
