package com.example.utils;

import java.util.UUID;

public class createUUID {

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid:"+uuid);
        //「-」記号(きごう)を削除（さくじょ）する
        return uuid.replaceAll("-", "");
    }
}
