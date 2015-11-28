package com.imageselector.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imageselector.R;
import com.imageselector.adapter.AlbumListAdapter;
import com.imageselector.entity.Album;
import com.imageselector.entity.Thumbnail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlbumActivity extends BaseActivity {

    private ListView mAlbumListView;
    private AlbumListAdapter mAlbumAdapter;
    private List<Album> mAlbumList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initView();
    }

    private void initView() {
        mContext = this;
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.actionbar_title_album);
        mAlbumListView = (ListView) findViewById(R.id.album_list);
        mAlbumAdapter = new AlbumListAdapter(mContext);
        mAlbumListView.setAdapter(mAlbumAdapter);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new GetAlbumListTask().execute();
        mAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlbumActivity.this, ThumbnailActivity.class);
                Bundle bundle = new Bundle();;
                bundle.putSerializable(ThumbnailActivity.THUMBNAIL_LIST, (Serializable) mAlbumList.get(position).getThumbnailList());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class GetAlbumListTask extends AsyncTask<Void, Void, List<Album>> {
        ContentResolver contentResolver = mContext.getContentResolver();
        Map<Integer, Album> albumMap;
        SparseArray<Thumbnail> thumbnailMap;

        /**
         * get Thumbnail,Album
         * add Thumbnail to Album according picId
         * take the Camera Album to first
         *
         * @param params
         * @return
         */
        @Override
        protected List<Album> doInBackground(Void... params) {
            thumbnailMap = getThumbnail();
            albumMap = getAlbumList();
            mAlbumList = new ArrayList<Album>();
            for (Map.Entry<Integer, Album> entry : albumMap.entrySet()) {
                mAlbumList.add(entry.getValue());
            }
            return mAlbumList;
        }

        @Override
        protected void onPostExecute(List<Album> albumList) {
            super.onPostExecute(albumList);
            mAlbumAdapter.setData(albumList);
            mAlbumAdapter.notifyDataSetChanged();
        }

        /**
         * get Album,and add Thumbnail to Album according picId
         * filter 0 size picture
         *
         * @return
         */
        private Map<Integer, Album> getAlbumList() {
            albumMap = new LinkedHashMap<Integer, Album>();
            String[] projection = {Media._ID, Media.DATA, Media.SIZE, Media.BUCKET_ID, Media.BUCKET_DISPLAY_NAME, Media.DATE_MODIFIED};
            Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, null,
                    null, Media.DATE_MODIFIED + " DESC");
            if (cursor != null) {
                int picIdIndex = cursor.getColumnIndex(Media._ID);
                int albumIdIndex = cursor.getColumnIndex(Media.BUCKET_ID);
                int picPathIndex = cursor.getColumnIndex(Media.DATA);
                int albumNameIndex = cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME);
                int picSizeIndex = cursor.getColumnIndex(Media.SIZE);
                while (cursor.moveToNext()) {
                    int size = cursor.getInt(picSizeIndex);
                    if (size == 0) {
                        continue;
                    }
                    int picId = cursor.getInt(picIdIndex);
                    int albumId = cursor.getInt(albumIdIndex);
                    String picPath = cursor.getString(picPathIndex);
                    String albumName = cursor.getString(albumNameIndex);
                    Album album = albumMap.get(albumId);
                    if (album == null) {
                        album = new Album();
                        album.setAlbumId(albumId);
                        album.setAlbumName(albumName);
                    }
                    Thumbnail thumbnail = thumbnailMap.get(picId);
                    if (thumbnail == null) {
                        thumbnail = new Thumbnail();
                        thumbnail.setPicId(picId);
                    }
                    thumbnail.setPicPath(picPath);
                    album.addToThumbnailList(thumbnail);
                    album.addAlbumNum();
                    albumMap.put(albumId, album);
                }
                cursor.close();
            }
            return albumMap;
        }

        /**
         * get picture Thumbnail
         *
         * @return
         */
        private SparseArray<Thumbnail> getThumbnail() {
            thumbnailMap = new SparseArray<Thumbnail>();
            String[] projection = {Thumbnails.IMAGE_ID, Thumbnails.DATA};
            Cursor cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                    null, null, null);
            if (cursor != null) {
                int picIdIndex = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
                int thumbnailPathIndex = cursor.getColumnIndex(Thumbnails.DATA);
                while (cursor.moveToNext()) {
                    int picId = cursor.getInt(picIdIndex);
                    String thumbnailPath = cursor.getString(thumbnailPathIndex);
                    Thumbnail thumbnail = new Thumbnail();
                    thumbnail.setPicId(picId);
                    thumbnail.setThumbnailPath(thumbnailPath);
                    thumbnailMap.put(picId, thumbnail);
                }
                cursor.close();
            }
            return thumbnailMap;
        }
    }
}
