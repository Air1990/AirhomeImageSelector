package com.imageselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.imageselector.entity.Thumbnail;
import com.imageselector.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ThumbnailAdapter extends BaseAdapter {
    private Context mContext;
    private List<Thumbnail> mThumbnailList;
    private int mPicWidth;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public ThumbnailAdapter(Context context, List<Thumbnail> thumbnailList, int picWidth) {
        mContext = context;
        mThumbnailList = thumbnailList;
        mPicWidth = picWidth;
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderUtils.getImageLoaderConfig(mContext));
        mOptions = ImageLoaderUtils.getDisplayImageOptions();
    }

    @Override
    public int getCount() {
        return mThumbnailList.size();
    }

    @Override
    public Object getItem(int position) {
        return mThumbnailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            FrameLayout frameLayout = new FrameLayout(mContext);
            ImageView thumbnail = new ImageView(mContext);
            thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            frameLayout.addView(thumbnail, new FrameLayout.LayoutParams
                    (mPicWidth, mPicWidth));
            convertView = frameLayout;
            viewHolder.thumbnail = thumbnail;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Thumbnail thumbnail = mThumbnailList.get(position);
        if (thumbnail.getThumbnailPath() != null) {
            mImageLoader.displayImage("file://" + thumbnail.getThumbnailPath(), viewHolder.thumbnail, mOptions);
        } else {
            mImageLoader.displayImage("file://" + thumbnail.getPicPath(), viewHolder.thumbnail, mOptions);
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView thumbnail;
    }
}
