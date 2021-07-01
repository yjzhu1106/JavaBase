package com.base.annotation.test;

import com.base.annotation.bean.Apple;
import com.base.annotation.bean.FruitInfoUtil;
import org.junit.jupiter.api.Test;

public class AnnotationTest {
    @Test
    public void test(){
        FruitInfoUtil.getFruitInfo(Apple.class);
    }

}
