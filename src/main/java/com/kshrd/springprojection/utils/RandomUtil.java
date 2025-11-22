package com.kshrd.springprojection.utils;

import java.util.Random;

public class RandomUtil {
    public String generateAccountNum() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("ACC%05d", number);
    }
}
