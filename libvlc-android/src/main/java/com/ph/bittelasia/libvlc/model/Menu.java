package com.ph.bittelasia.libvlc.model;

import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Menu implements Serializable,Comparable<Menu> {

    private int order;
    private String name;
    private Uri iconFile;
    private Class aClass;

    public Menu(Builder builder){
        this.order = builder.orderNo;
        this.name =builder.name;
        this.iconFile =builder.iconFile;
        this.aClass = builder.aClass;
    }

    @NotNull
    @Override
    public String toString() {
        return "Menu{" +
                "order=" + order +
                ", name='" + name + '\'' +
                ", icon='" + iconFile + '\'' +
                ", aClass=" + aClass +
                '}';
    }

    @Override
    public int compareTo(Menu o) {
        return this.order - o.order;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public Uri getIconFile() {
        return iconFile;
    }

    public Class getaClass() {
        return aClass;
    }


    public static class Builder{

        private int orderNo;
        private String name;
        private Uri iconFile;
        private Class aClass;

        public Builder orderNo(int orderNo){
            this.orderNo =orderNo;
            return this;
        }

        public Builder name(String name){
            this.name=name;
            return this;
        }

        public Builder iconFile(Uri iconFile){
            this.iconFile=iconFile;
            return this;
        }

        public Builder aClass(Class c){
            this.aClass=c;
            return this;
        }

        public Menu buildMenu(){
            return new Menu(this);
        }

    }

}
