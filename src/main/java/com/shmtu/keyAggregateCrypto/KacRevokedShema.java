package com.shmtu.keyAggregateCrypto;

import edu.princeton.cs.introcs.Out;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.engines.GGHSW13KEMEngine;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;


public class KacRevokedShema {

    int fileNum, m;
    Pairing pairing;
//    Element generator_g;

    public Pairing initCurveByDynamic(int rBit, int qBit) {
        /*
        rBit是 Zp 中阶数 p 的比特长度；和密钥长度有关
        qBit是 G 中阶数的比特长度，和线性群G1的大小有关
         */
        TypeACurveGenerator pg = new TypeACurveGenerator(rBit, qBit);
        PairingParameters typeAParams = pg.generate();
        this.pairing = PairingFactory.getPairing(typeAParams);
        return pairing;
    }

    public Pairing initCurveByFile(boolean generateByFile, int rBit, int qBit) {
        if (!generateByFile) {
            TypeACurveGenerator pg = new TypeACurveGenerator(rBit, qBit);
            PairingParameters typeAParams = pg.generate();
            Out out = new Out("initCurveParams.properties");
            out.println(typeAParams);
            this.pairing = PairingFactory.getPairing(typeAParams);
            return pairing;
        }
        this.pairing = PairingFactory.getPairing("initCurveParams.properties");
        return pairing;
    }

    public Element getRandomElement(String set, Pairing pairing) {
        // set: g_1, g_2, g_t, z_p
        if (set.equals("g_1")) {
            return pairing.getG1().newRandomElement().getImmutable();
        }

        if (set.equals("g_2")) {
            return pairing.getG2().newRandomElement().getImmutable();
        }

        if (set.equals("g_t")) {
            return pairing.getGT().newRandomElement().getImmutable();
        }

        if (set.equals("z_p")) {
            return pairing.getZr().newRandomElement().getImmutable();
        }

        return null;
    }


    public HashMap<String, ArrayList<Element>> setUp(int lamada, int fileNum) {
        HashMap<String, ArrayList<Element>> res = new HashMap<>();
        ArrayList<Element> params = new ArrayList<>();
        ArrayList<Element> msk = new ArrayList<>();
        ArrayList<Element> mpk = new ArrayList<>();

        // step 1
        Pairing pairing = initCurveByDynamic(8, lamada);

        Element generator_g = getRandomElement("g_1", pairing);
        Element secretly_a = getRandomElement("z_p", pairing);

        // step 2
        this.m = 0;
        this.fileNum = fileNum;
        if (this.m < fileNum) {
            this.m = fileNum + 1;
        }

        for (int i = 0; i <= m * 2; i++) {
            if (i == m) continue;
            if (i == 0) {
                params.add(generator_g);
                continue;
            }
            Element a_pow_i = secretly_a.pow(BigInteger.valueOf(i));
            Element g_a_i = generator_g.powZn(a_pow_i);
            params.add(g_a_i);
        }

        // step 3
        Element r = getRandomElement("z_p", pairing);
        Element y = getRandomElement("z_p", pairing);
        msk.add(r);
        msk.add(y);

        Element mpk_element = generator_g.powZn(r);
        mpk.add(mpk_element);

        res.put("params", params);
        res.put("msk", msk);
        res.put("mpk", mpk);

        return res;
    }


    public HashMap<Integer, ArrayList<Element>> keyGen(int lamada, ArrayList<Element> params, ArrayList<Element> msk) {
        HashMap<Integer, ArrayList<Element>> pks = new HashMap<>();

        for (int i = 1; i <= this.fileNum; i++) {
            ArrayList<Element> pk_i = new ArrayList<>();

            Element pk_i_2 = params.get(i);
            Element r = msk.get(0);
            Element pk_i_3 = pk_i_2.powZn(msk.get(1));
            Element pk_i_4 = pk_i_2.powZn(r.pow(BigInteger.valueOf(-1)));
//            Element pk_i_42 = pk_i_2.powZn(r.negate());

            pk_i.add(pk_i_2);
            pk_i.add(pk_i_3);
            pk_i.add(pk_i_4);
            pks.put(i, pk_i);
        }
        return pks;
    }

    public ArrayList<Element> encrypt(ArrayList<Element> pk_i, BigInteger data, ArrayList<Element> mpk, ArrayList<Element> params) {
        ArrayList<Element> c_i = new ArrayList<>();
        // Step 1

        Element t = getRandomElement("z_p", this.pairing);
        Element g = params.get(0);

        Element c_1 = g.powZn(t);
        Element c_2 = (mpk.get(0).mul(pk_i.get(1))).powZn(t);
        Element pairing_res = this.pairing.pairing(params.get(1).powZn(t), params.get(m + 1));

//        Element pairing_res2 = this.pairing.pairing(params.get(0), params.get(m + 1).powZn(t));


        Element c_3 = pairing_res.mul(data);

        BigInteger multiply = data.multiply(pairing_res.toBigInteger());
        if(c_3.toBigInteger() == multiply) {
            System.out.println("true");
        }


        Element c_4 = pk_i.get(2).powZn(t);
        c_i.add(c_1);
        c_i.add(c_2);
        c_i.add(c_3);
        c_i.add(c_4);
        return c_i;
    }

    public ArrayList<Element> extract(int[] s_k, ArrayList<Element> params, ArrayList<Element> mpk, ArrayList<Element> msk,
                                      HashMap<Integer, ArrayList<Element>> pub) {
        Element r_k = getRandomElement("z_p", pairing);
        Element r = msk.get(0);
        Element y = msk.get(1);

        ArrayList<Element> k_sk = new ArrayList<>();

        Element num_mul = null;
        for (int j : s_k) {
            int index = m + 1 - j;
            if (num_mul == null) {
                num_mul = params.get(index);
            } else {
                num_mul = num_mul.mul(params.get(index));
            }
        }

        Element k_sk_1 = num_mul.powZn(r_k);
        Element k_sk_2 = num_mul.powZn((r_k.div(r)));
        Element k_sk_3 = num_mul.powZn(r.sub(y.div(r_k)));
        k_sk.add(k_sk_1);
        k_sk.add(k_sk_2);
        k_sk.add(k_sk_3);

        return k_sk;

    }

    public BigInteger decrypt(ArrayList<Element> k_sk,
                              ArrayList<Element> params,
                              ArrayList<Element> mpk,
                              HashMap<Integer, ArrayList<Element>> pub,
                              int[] s_k,
                              ArrayList<Element> encrypt_1,
                              int i) {
        Element k_sk_1 = k_sk.get(0);


        Element num_mul = null;
        for (int j : s_k) {
            int index = m + 1 - j + i;
            if (num_mul == null) {
                num_mul = params.get(index - 1);
            } else {
                num_mul = num_mul.mul(params.get(index - 1));
            }
        }
        Element e_secondParams = k_sk_1.mul(num_mul);
        Element e_res = this.pairing.pairing(encrypt_1.get(0), e_secondParams);
        Element numerator = encrypt_1.get(2).mul(e_res);
        Element denominator_firstParam = this.pairing.pairing(k_sk.get(1), encrypt_1.get(1));
        Element denominator_secondParam = this.pairing.pairing(encrypt_1.get(3), k_sk.get(2));
        Element denominator = denominator_firstParam.mul(denominator_secondParam);

        Element plain_text_element = numerator.div(denominator);

        BigInteger plain_text = plain_text_element.toBigInteger();

        return plain_text;


    }


}
