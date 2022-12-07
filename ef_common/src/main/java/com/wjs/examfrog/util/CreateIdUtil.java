package com.wjs.examfrog.util;

import java.util.Random;

public class CreateIdUtil {
    public static Long creatId(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }

        return Long.parseLong(sb.toString());
    }
}
