package com.imageselector.entity;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private int albumId;
    private String albumName;
    private int albumNum;
    private List<Thumbnail> thumbnailList=new ArrayList<Thumbnail>();

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getAlbumNum() {
        return albumNum;
    }

    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public List<Thumbnail> getThumbnailList() {
        return thumbnailList;
    }

    public void setThumbnailList(List<Thumbnail> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    public void addAlbumNum() {
        albumNum += 1;
    }

    public void addToThumbnailList(Thumbnail thumbnail) {
        thumbnailList.add(thumbnail);
    }
}
