package com.shmtu.proxy.leetcode;

import com.shmtu.bean.TreeNode;
import com.shmtu.leetcode.Problem297;
import org.junit.jupiter.api.Test;

public class ProblemTest {

    @Test
    public void TestProblem297(){
        Problem297 problem297 = new Problem297();

        String data = "1,2,3,null,null,4,5";

        TreeNode root = problem297.deserialize(data);


        String serialize = problem297.serialize(root);


    }


}
