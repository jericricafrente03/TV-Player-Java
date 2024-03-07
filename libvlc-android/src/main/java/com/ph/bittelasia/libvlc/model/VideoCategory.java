package com.ph.bittelasia.libvlc.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class VideoCategory implements Serializable, Comparable<VideoCategory> {

    private int     id;
    private String  name;
    private String  description;
    private String  icon;

    public VideoCategory(Builder builder)
    {
        this.id= builder.id;
        this.name=builder.name;
        this.description=builder.description;
        this.icon=builder.icon;
    }

    public int getCategoryID() {
        return id;
    }

    public String getName() {
        return name!=null?name:"no name";
    }

    public String getDescription() {
        return description!=null?description:"no description";
    }

    public String getIcon() {
        return icon;
    }

    @NotNull
    @Override
    public String toString() {
        return "VideoCategory{"+
                "id="+id+","+
                "name="+name+","+
                "description="+description+","+
                "icon="+icon+
                "}";
    }

    @Override
    public int compareTo(VideoCategory info) {
        return this.id - info.id;
    }

    public static class Builder
    {
        private int     id;
        private String  name;
        private String  description;
        private String  icon;

        public Builder id(int id)
        {
            this.id=id;
            return this;
        }

        public Builder name(String name)
        {
            this.name=name;
            return this;
        }

        public Builder desc(String desc)
        {
            this.description=desc;
            return this;
        }


        public Builder icon(@NonNull String icon)
        {
            this.icon=icon;
            return this;
        }
        public VideoCategory buildVideoCategory()
        {
            return new VideoCategory(this);
        }
    }

}
