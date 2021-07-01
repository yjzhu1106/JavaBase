package com.base.annotation.bean;

import java.lang.reflect.Field;

public class FruitInfoUtil {

    public static void getFruitInfo(Class<?> clazz) {
        String providerInfo = "ProviderInfo: ";
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(FruitProvider.class)) {
                FruitProvider fruitProvider = f.getAnnotation(FruitProvider.class);
                // Deal with the information of Annotation
                providerInfo = "ProviderId: " + fruitProvider.id() + ", ProviderName: " + fruitProvider.name()
                        + ", ProviderAddress: " + fruitProvider.address();
                System.out.println(providerInfo);
            }
        }

    }

}
