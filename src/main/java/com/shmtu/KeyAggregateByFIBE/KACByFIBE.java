package com.shmtu.KeyAggregateByFIBE;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import javax.swing.plaf.basic.BasicHTML;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

public class KACByFIBE {


    public static void setup(String pairingParametersFileName, int n, String paramsFileName, String mpkFileName, String mskFileName) {
        Pairing bp = PairingFactory.getPairing(pairingParametersFileName);
        Element g = bp.getG1().newRandomElement().getImmutable();
        Properties mskProp = new Properties();
        Properties mpkProp = new Properties();
        Properties paramsProp = new Properties();
        // U is n, the number of file class
        // compute lamada
        Properties curveProp = loadPropFromFile(pairingParametersFileName);
        BigInteger q = new BigInteger(curveProp.getProperty("q"));
        int lamada = 0;
        while (q.compareTo(BigInteger.valueOf(2)) == 0 || q.compareTo(BigInteger.valueOf(2)) == 1) {
            q = q.divide(BigInteger.valueOf(2));
            lamada++;
        }
        // compute m, the ploy(lamada) = 2 * lamada
        int m = lamada + 1;
        // the security number a
        Element a = bp.getZr().newRandomElement().getImmutable();
        paramsProp.setProperty("g", Base64.getEncoder().withoutPadding().encodeToString(g.toBytes()));

        for (int i = 1; i <= 2 * m; i++) {
            Element a_pow_i = a.pow(BigInteger.valueOf(i));
            Element g_i = g.powZn(a_pow_i);
            paramsProp.setProperty("g" + i, Base64.getEncoder().withoutPadding().encodeToString(g_i.toBytes()));
        }

        Element r = bp.getZr().newRandomElement().getImmutable();
        Element y = bp.getZr().newRandomElement().getImmutable();
        mskProp.setProperty("r", Base64.getEncoder().withoutPadding().encodeToString(r.toBytes()));
        mskProp.setProperty("y", Base64.getEncoder().withoutPadding().encodeToString(y.toBytes()));

        Element mpk = g.powZn(r);
        mpkProp.setProperty("mpk", Base64.getEncoder().withoutPadding().encodeToString(mpk.toBytes()));
        mpkProp.setProperty("g", Base64.getEncoder().withoutPadding().encodeToString(g.toBytes()));
        mpkProp.setProperty("r", Base64.getEncoder().withoutPadding().encodeToString(r.toBytes()));
        mpkProp.setProperty("m", Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(m).getBytes()));

        storePropToFile(mskProp, mskFileName);
        storePropToFile(mpkProp, mpkFileName);
        storePropToFile(paramsProp, paramsFileName);
    }

    public static void keygen(String pairingParametersFileName, String paramsFileName, String mskFileName, int n, String pubFileName) {
        Pairing bp = PairingFactory.getPairing(pairingParametersFileName);
        Properties paramsProp = loadPropFromFile(paramsFileName);
        Properties mskProp = loadPropFromFile(mskFileName);
        Element y = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(mskProp.getProperty("y"))).getImmutable();
        Element r = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(mskProp.getProperty("r"))).getImmutable();
        // output file
        Properties pubProp = new Properties();

        for (int i = 1; i <= n; i++) {
            byte[] pk1 = String.valueOf(i).getBytes(StandardCharsets.UTF_8);
            Element pk2 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + i))).getImmutable();
            Element pk3 = pk2.powZn(y);
            Element pk4 = pk2.powZn(r.pow(BigInteger.valueOf(-1)));

            pubProp.setProperty("pk" + i + "_1", Base64.getEncoder().withoutPadding().encodeToString(pk1));
            pubProp.setProperty("pk" + i + "_2", Base64.getEncoder().withoutPadding().encodeToString(pk2.toBytes()));
            pubProp.setProperty("pk" + i + "_3", Base64.getEncoder().withoutPadding().encodeToString(pk3.toBytes()));
            pubProp.setProperty("pk" + i + "_4", Base64.getEncoder().withoutPadding().encodeToString(pk4.toBytes()));
        }
        storePropToFile(pubProp, pubFileName);
    }

    public static void encrypt(String pairingParametersFileName, Element message, int i, String mpkFileName, String pubFileName, String paramsFileName, String ctFileName) {
        Pairing bp = PairingFactory.getPairing(pairingParametersFileName);
        Properties pubProp = loadPropFromFile(pubFileName);
        Properties mpkProp = loadPropFromFile(mpkFileName);
        Properties paramsProp = loadPropFromFile(paramsFileName);

        int m = Integer.valueOf(new String(Base64.getDecoder().decode(mpkProp.getProperty("m")))).intValue();
        Element t = bp.getZr().newRandomElement().getImmutable();
        Element g = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g"))).getImmutable();
        Element g1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g1"))).getImmutable();
        Element gm = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + m))).getImmutable();
        Element gm1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + (m + 1)))).getImmutable();
        Element mpk = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(mpkProp.getProperty("mpk"))).getImmutable();
        Element pki_3 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(pubProp.getProperty("pk" + i + "_3"))).getImmutable();
        Element pki_4 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(pubProp.getProperty("pk" + i + "_4"))).getImmutable();
        // output
        Properties ctProp = new Properties();

        Element c1 = g.powZn(t);
        Element c2 = (mpk.mul(pki_3)).powZn(t);
        Element c3 = message.mul(bp.pairing(g1.powZn(t), gm));
        Element c4 = pki_4.powZn(t);

        ctProp.setProperty("c1", Base64.getEncoder().withoutPadding().encodeToString(c1.toBytes()));
        ctProp.setProperty("c2", Base64.getEncoder().withoutPadding().encodeToString(c2.toBytes()));
        ctProp.setProperty("c3", Base64.getEncoder().withoutPadding().encodeToString(c3.toBytes()));
        ctProp.setProperty("c4", Base64.getEncoder().withoutPadding().encodeToString(c4.toBytes()));
        ctProp.setProperty("t", Base64.getEncoder().withoutPadding().encodeToString(t.toBytes()));

        // verify M*e(g1^t, gm)*e(g1^-t, gm) = e(g1^t, gm)*e(g1^-t, gm)*M
        Element plainText2 = c3.mul(bp.pairing(g1.powZn(t.mul(-1)), gm).getImmutable());
        Element plainText3 = bp.pairing(g1.powZn(t.mul(-1)), gm).getImmutable().mul(c3);
        Element plainTest4 = message.mul((bp.pairing(g1.powZn(t), gm).getImmutable().mul(bp.pairing(g1.powZn(t.mul(-1)), gm).getImmutable())));
        // verify  M*e(g1^t, gm) =  M*e(g^t, gm+1), compare c3 and M*e(g^t, gm+1)
        Element ciptxText = message.mul(bp.pairing(g.powZn(t), gm1).getImmutable());


        storePropToFile(ctProp, ctFileName);
    }

    public static void extract(String pairingParametersFileName, int[] sk, String paramsFileName, String mpkFileName, String mskFileName, String pubFileName, String kskFileName) {
        // sk = {1, 2, 3, ... ,.. n} sk is the subset of all set
        Pairing bp = PairingFactory.getPairing(pairingParametersFileName);
        Properties pubProp = loadPropFromFile(pubFileName);
        Properties mpkProp = loadPropFromFile(mpkFileName);
        Properties mskProp = loadPropFromFile(mskFileName);
        Properties paramsProp = loadPropFromFile(paramsFileName);
        int m = Integer.valueOf(new String(Base64.getDecoder().decode(mpkProp.getProperty("m")))).intValue();
        Element rk = bp.getZr().newRandomElement().getImmutable();
        Element y = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(mskProp.getProperty("y"))).getImmutable();
        Element r = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(mskProp.getProperty("r"))).getImmutable();

        Element result = null;

        for (int j : sk) {
            int pkj_1 = Integer.valueOf(new String(Base64.getDecoder().decode(pubProp.getProperty("pk" + j + "_1")))).intValue();
            int index = m + 1 - pkj_1;
            if (result == null) {
                result = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index)));
                continue;
            }
            result.mul(bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))));
        }

        Properties KskProp = new Properties();
        result = result.getImmutable();
        Element Ksk1 = result.powZn(rk);
        Element Ksk2 = result.powZn(rk.div(r));

        Element Ksk3 = result.powZn(r.sub(y.mulZn(rk)));
        // verify correct of Ksk3
        Element Ksk321 = result.powZn(r).div(result.powZn(y.div(rk)));

        KskProp.setProperty("Ksk1", Base64.getEncoder().withoutPadding().encodeToString(Ksk1.toBytes()));
        KskProp.setProperty("Ksk2", Base64.getEncoder().withoutPadding().encodeToString(Ksk2.toBytes()));
        KskProp.setProperty("Ksk3", Base64.getEncoder().withoutPadding().encodeToString(Ksk3.toBytes()));
        KskProp.setProperty("rk", Base64.getEncoder().withoutPadding().encodeToString(rk.toBytes()));
        storePropToFile(KskProp, kskFileName);
    }

    public static Element decrypt(String pairingParametersFileName, String kskFileName, String paramsFileName, String mpkFileName, String pubFileName, int[] sk, String ctFileName, int i) {
        Pairing bp = PairingFactory.getPairing(pairingParametersFileName);
        Properties pubProp = loadPropFromFile(pubFileName);
        Properties mpkProp = loadPropFromFile(mpkFileName);
        Properties paramsProp = loadPropFromFile(paramsFileName);
        Properties kskProp = loadPropFromFile(kskFileName);
        Properties ctProp = loadPropFromFile(ctFileName);

        int m = Integer.valueOf(new String(Base64.getDecoder().decode(mpkProp.getProperty("m")))).intValue();
        Element Ksk1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("Ksk1"))).getImmutable();
        Element Ksk2 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("Ksk2"))).getImmutable();
        Element Ksk3 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("Ksk3"))).getImmutable();

        Element c1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c1"))).getImmutable();
        Element c2 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c2"))).getImmutable();
        Element c3 = bp.getGT().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c3"))).getImmutable();
        Element c4 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c4"))).getImmutable();
        Element res = null;
        for (int j : sk) {
            int pkj_1 = Integer.valueOf(new String(Base64.getDecoder().decode(pubProp.getProperty("pk" + j + "_1")))).intValue();
            int index = m + 1 - pkj_1 + i;
            if (i == j) {
                continue;
            }
            if (res == null) {
                res = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index)));
                continue;
            }
            res.mul(bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))));
        }
        res = res.getImmutable();

        // way1
        Element plainText = c3.mul(bp.pairing(c1, Ksk1.mul(res)).getImmutable()).div(bp.pairing(Ksk2, c2).getImmutable().mul(bp.pairing(c4, Ksk3).getImmutable()));
        // way2
        Element t = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("t"))).getImmutable();
        Element g1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g1"))).getImmutable();
        Element gm = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + m))).getImmutable();
        Element plainText2 = c3.mulZn(bp.pairing(g1.powZn(t.mul(-1)), gm).getImmutable());
        return plainText;

    }

    public static Element decrypt2(String pairingParametersFileName, String kskFileName, String paramsFileName, String mpkFileName, String pubFileName, int[] sk, String ctFileName, int i,
                                   String mskFileName) {
        Pairing bp = PairingFactory.getPairing(pairingParametersFileName);
        Properties pubProp = loadPropFromFile(pubFileName);
        Properties mpkProp = loadPropFromFile(mpkFileName);
        Properties paramsProp = loadPropFromFile(paramsFileName);
        Properties kskProp = loadPropFromFile(kskFileName);
        Properties ctProp = loadPropFromFile(ctFileName);
        Properties mskProp = loadPropFromFile(mskFileName);

        int m = Integer.valueOf(new String(Base64.getDecoder().decode(mpkProp.getProperty("m")))).intValue();
        Element Ksk1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("Ksk1"))).getImmutable();
        Element Ksk2 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("Ksk2"))).getImmutable();
        Element Ksk3 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("Ksk3"))).getImmutable();

        Element c1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c1"))).getImmutable();
        Element c2 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c2"))).getImmutable();
        Element c3 = bp.getGT().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c3"))).getImmutable();
        Element c4 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("c4"))).getImmutable();

        // according paper to computer p1
        Element p1_1_pairing_mul = null;
        for (int j : sk) {
            if (i == j) {
                continue;
            }
            int index = m + 1 - j + i;
            Element g_index = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))).getImmutable();
            if (p1_1_pairing_mul == null) {
                p1_1_pairing_mul = g_index;
                continue;
            }
            p1_1_pairing_mul = p1_1_pairing_mul.mul(g_index);
        }
        Element p1_1_pairing2 = Ksk1.mul(p1_1_pairing_mul);
        Element p1_1 = bp.pairing(c1, p1_1_pairing2).getImmutable();

        // according verify way to computer the p1
        Element t = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(ctProp.getProperty("t"))).getImmutable();
        Element rk = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(kskProp.getProperty("rk"))).getImmutable();
        Element g = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g"))).getImmutable();
        Element g_t = g.powZn(t);

        Element p1_2_pairing1_mul = null; // the fist pairing mul params of p1
        for (int j : sk) {
            int index = m + 1 - j;
            Element g_index = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))).getImmutable();
            Element g_index_1 = g_index.powZn(rk);
            if (p1_2_pairing1_mul == null) {
                p1_2_pairing1_mul = g_index_1;
                continue;
            }
            p1_2_pairing1_mul = p1_2_pairing1_mul.mul(g_index_1);
        }
        Element p1_2_pairing1 = bp.pairing(g_t, p1_2_pairing1_mul).getImmutable();

        Element p1_2_pairing2_mul = null; // the second pairing mul params of p1
        for (int j : sk) {
            if (i == j) {
                continue;
            }
            int index = m + 1 - j + i;
            Element g_index = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))).getImmutable();
            if (p1_2_pairing2_mul == null) {
                p1_2_pairing2_mul = g_index;
                continue;
            }
            p1_2_pairing2_mul = p1_2_pairing2_mul.mul(g_index);
        }
        Element p1_2_pairing2 = bp.pairing(g_t, p1_2_pairing2_mul).getImmutable();
        Element p1_2 = p1_2_pairing1.mul(p1_2_pairing2);
        if (p1_1.equals(p1_2)) {
            System.out.println("p1 verify success...");
        }


        // according paper to computer p2
        Element p2_1 = bp.pairing(Ksk2, c2).getImmutable();

        // according verify way to computer the p2
        Element y = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(mskProp.getProperty("y"))).getImmutable();
        Element r = bp.getZr().newElementFromBytes(Base64.getDecoder().decode(mskProp.getProperty("r"))).getImmutable();
        Element gi = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + i))).getImmutable();
        Element gi_t = gi.powZn(t);
        Element p2_2_pairing2_mul = null;
        Element pow_num = y.mulZn(rk).mulZn(r.pow(BigInteger.valueOf(-1)));
        for (int j : sk) {
            int index = m + 1 - j;
            Element g_index = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))).getImmutable();
            Element g_index_pow = g_index.powZn(pow_num);
            if (p2_2_pairing2_mul == null) {
                p2_2_pairing2_mul = g_index_pow;
                continue;
            }
            p2_2_pairing2_mul = p2_2_pairing2_mul.mul(g_index_pow);
        }
        Element p2_2_pairing1 = bp.pairing(g_t, p1_2_pairing1_mul).getImmutable();
        Element p2_2_pairing2 = bp.pairing(gi_t, p2_2_pairing2_mul).getImmutable();
        Element p2_2 = p2_2_pairing1.mul(p2_2_pairing2);
        if (p2_1.equals(p2_2)) {
            System.out.println("p2 verify success...");
        }


        // according paper to computer p3
        Element p3_1 = bp.pairing(Ksk3, c4).getImmutable(); // e(x, y) == e(y, x) --> true


        // according verify way to computer the p3
        Element p3_2_pairing1_mul = null;
        for(int j : sk){
            int index = m + 1 -j;
            Element g_index = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))).getImmutable();
            if(p3_2_pairing1_mul == null){
                p3_2_pairing1_mul = g_index;
                continue;
            }
            p3_2_pairing1_mul = p3_2_pairing1_mul.mul(g_index);
        }
        Element p3_2_pairing1 = bp.pairing(gi_t, p3_2_pairing1_mul).getImmutable();

        Element p3_2_pairing2_mul = null;
//        Element pow_num_3 = y.mulZn(rk).mulZn(r.pow(BigInteger.valueOf(-1))).negate();
        Element pow_num_3 = y.mulZn(rk).div(r).negate();
        for (int j : sk) {
            int index = m + 1 - j;
            Element g_index = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + index))).getImmutable();
            Element g_index_pow = g_index.powZn(pow_num_3);
            if (p3_2_pairing2_mul == null) {
                p3_2_pairing2_mul = g_index_pow;
                continue;
            }
            p3_2_pairing2_mul = p3_2_pairing2_mul.mul(g_index_pow);
        }
        Element p3_2_pairing2 = bp.pairing(gi_t, p3_2_pairing2_mul).getImmutable();

        Element p3_2 = p3_2_pairing1.mul(p3_2_pairing2);
        if (p3_1.equals(p3_2)) {
            System.out.println("p3 verify success...");
        }

        // get message
        Element res_secondParam = p1_1.div(p2_1.mul(p3_2));
        // c3*(ewq) = M, ewq == p1/(p2*p3) == res_secondParam
        Element g1 = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g1"))).getImmutable();
        Element gm = bp.getG1().newElementFromBytes(Base64.getDecoder().decode(paramsProp.getProperty("g" + m))).getImmutable();
        Element ewq = bp.pairing(g1.powZn(t.mul(-1)), gm).getImmutable();


        Element res = c3.mul(res_secondParam);
        Element res_2 = c3.mul(ewq);

        return res_2;

    }








    //计算由coef为系数确定的多项式qx在点x处的值，注意多项式计算在群Zr上进行
    public static Element qx(Element x, Element[] coef, Field Zr) {
        Element res = coef[0];
        for (int i = 1; i < coef.length; i++) {
            Element exp = Zr.newElement(i).getImmutable();
            //x一定要使用duplicate复制使用，因为x在每一次循环中都要使用，如果不加duplicte，x的值会发生变化
            res = res.add(coef[i].mul(x.duplicate().powZn(exp)));
        }
        return res;
    }

    //求两个数组的交集
    public static int[] intersection(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int length1 = nums1.length, length2 = nums2.length;
        int[] intersection = new int[length1 + length2];
        int index = 0, index1 = 0, index2 = 0;
        while (index1 < length1 && index2 < length2) {
            int num1 = nums1[index1], num2 = nums2[index2];
            if (num1 == num2) {
                // 保证加入元素的唯一性
                if (index == 0 || num1 != intersection[index - 1]) {
                    intersection[index++] = num1;
                }
                index1++;
                index2++;
            } else if (num1 < num2) {
                index1++;
            } else {
                index2++;
            }
        }
        return Arrays.copyOfRange(intersection, 0, index);
    }

    //拉格朗日因子计算 i是集合S中的某个元素，x是目标点的值
    public static Element lagrange(int i, int[] S, int x, Field Zr) {
        Element res = Zr.newOneElement().getImmutable();
        Element iElement = Zr.newElement(i).getImmutable();
        Element xElement = Zr.newElement(x).getImmutable();
        for (int j : S) {
            if (i != j) {
                //注意：在循环中重复使用的项一定要用duplicate复制出来使用
                //这儿xElement和iElement重复使用，但因为前面已经getImmutable所以可以不用duplicate
                Element numerator = xElement.sub(Zr.newElement(j));
                Element denominator = iElement.sub(Zr.newElement(j));
                res = res.mul(numerator.div(denominator));
            }
        }
        return res;
    }

    public static void storePropToFile(Properties prop, String fileName) {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            prop.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(fileName + " save failed!");
            System.exit(-1);
        }
    }

    public static Properties loadPropFromFile(String fileName) {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream(fileName)) {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(fileName + " load failed!");
            System.exit(-1);
        }
        return prop;
    }


}
