package com.shmtu.proxy.KeyAggregateByFIBE;

import com.shmtu.KeyAggregateByFIBE.KACByFIBE;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class KacByFIBETest {


    @Test
    public void test() throws NoSuchAlgorithmException {
        KACByFIBE kacByFIBE = new KACByFIBE();

        int U = 20;
        int d = 5;
        System.out.println("系统解密门限为：" + d);

        int[] userAttList = {1, 5, 3, 6, 10, 11};
        int[] messageAttList = {1, 3, 5, 7, 9, 10, 11};
        int[] sk = new int[]{1, 2, 4, 5, 6, 9, 11, 15};

        String dir = "kac_data/";
        String pairingParametersFileName = "a.properties";
        String mpkFileName = dir + "mpk.properties";
        String mskFileName = dir + "msk.properties";
        String skFileName = dir + "sk.properties";
        String ctFileName = dir + "ct.properties";
        String paramsFileName = dir + "params.properties";
        String pubFileName = dir + "pub.properties";
        String kskFileName = dir + "Ksk.properties";

        kacByFIBE.setup(pairingParametersFileName, U, paramsFileName, mpkFileName, mskFileName);

        kacByFIBE.keygen(pairingParametersFileName, paramsFileName, mskFileName, U, pubFileName);

        Element message = PairingFactory.getPairing(pairingParametersFileName).getGT().newRandomElement().getImmutable();

        System.out.println("明文消息:" + message);
        kacByFIBE.encrypt(pairingParametersFileName, message, 11, mpkFileName, pubFileName, paramsFileName, ctFileName);

        kacByFIBE.extract(pairingParametersFileName, sk, paramsFileName, mpkFileName, mskFileName, pubFileName, kskFileName);

//        Element res = kacByFIBE.decrypt2(pairingParametersFileName, kskFileName, paramsFileName, mpkFileName, pubFileName, sk, ctFileName, 11, mskFileName);
        Element res = kacByFIBE.decrypt(pairingParametersFileName, kskFileName, paramsFileName, mpkFileName, pubFileName, sk, ctFileName, 11);

        System.out.println("解密结果:" + res);
        if (message.isEqual(res)) {
            System.out.println("成功解密！");
        }
    }
}
