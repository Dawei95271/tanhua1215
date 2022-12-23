package com.tanhua.test;

import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/23 11:42
 */
public class TestDemo {

    @Test
    public void test1() {
        ObjectId tom = new ObjectId("5e82dd6164019531fc471ff0");
        System.out.println(tom.toHexString());
        System.out.println(tom.toString());
    }

    @Test
    public void test2() {
        List<Integer> list = new ArrayList<>();
        System.out.println(list);

    }
}
