package com.imageselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imageselector.R;
import com.imageselector.entity.Album;
import com.imageselector.entity.Thumbnail;
import com.imageselector.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AlbumListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Album> mAlbumList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public AlbumListAdapter(Context context) {
        mContext = context;
        mAlbumList = new ArrayList<>();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderUtils.getImageLoaderConfig(mContext));
        mOptions = ImageLoaderUtils.getDisplayImageOptions();
    }

    public void setData(List<Album> albumList) {
        mAlbumList = albumList;
    }

    @Override
    public int getCount() {
        return mAlbumList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlbumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mAlbumList.get(position).getAlbumId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false);
            viewHolder.albumThumbnail = (ImageView) convertView.findViewById(R.id.album_thumbnail);
            viewHolder.albumName = (TextView) convertView.findViewById(R.id.album_name);
            viewHolder.albumNum = (TextView) convertView.findViewById(R.id.album_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Album album = mAlbumList.get(position);
        viewHolder.albumName.setText(album.getAlbumName());
        viewHolder.albumNum.setText(String.valueOf(album.getAlbumNum()) + "张照片");
        Thumbnail thumbnail = album.getThumbnailList().get(0);
        if (thumbnail.getThumbnailPath() != null) {
            mImageLoader.displayImage("file://" + thumbnail.getThumbnailPath(), viewHolder.albumThumbnail, mOptions);
        } else {
            mImageLoader.displayImage("file://" + thumbnail.getPicPath(), viewHolder.albumThumbnail, mOptions);
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView albumThumbnail;
        private TextView albumName;
        private TextView albumNum;
    }
}
