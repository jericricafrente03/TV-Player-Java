package com.ph.bittelasia.libvlc.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class VideoInfo implements Serializable, Comparable<VideoInfo> {

    private int     channelNo;
    private int     categoryID;
    private String  path;
    private String  name;
    private String  description;
    private String  icon;
    private boolean enabled;

    public VideoInfo(Builder builder)
    {
        this.categoryID= builder.categoryID;
        this.channelNo= builder.channelNo;
        this.path=builder.path;
        this.name=builder.name;
        this.description=builder.description;
        this.icon=builder.icon;
        this.enabled=builder.enabled;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getChannelNo() {
        return channelNo;
    }

    public String getPath() {
        return path;
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

    public boolean isEnabled() {
        return enabled;
    }

    @NotNull
    @Override
    public String toString() {
        return  "VideoInfo [channelNo=" + channelNo
                + ", categoryID=" + categoryID
                + ", path=" + path
                + ", name=" + name
                + ", description=" + description
                + ", icon=" + icon
                + ", enabled=" + enabled
                + "]";
    }

    @Override
    public int compareTo(VideoInfo info) {
        return this.channelNo - info.channelNo;
    }

    public static class Builder
    {
        private int     channelNo;
        private int     categoryID;
        private String  name;
        private String  path;
        private String  description;
        private String  icon;
        private boolean enabled;

        public Builder no(int channelNo)
        {
            this.channelNo=channelNo;
            return this;
        }

        public Builder categoryID(int categoryID)
        {
            this.categoryID=categoryID;
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

        public Builder path(@NonNull String path)
        {
            this.path=path;
            return this;
        }

        public Builder icon(@NonNull String icon)
        {
            this.icon=icon;
            return this;
        }

        public Builder isEnabled(Boolean enabled)
        {
            this.enabled=enabled;
            return this;
        }
        public VideoInfo buildVideoInfo()
        {
            return new VideoInfo(this);
        }
    }

}
