package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.utils;

public class Util {

    private static Util instance = null;

    public static Util getInstance(){
        if(instance==null){
            instance = new Util();
        }
        return instance;
    }

    

}
