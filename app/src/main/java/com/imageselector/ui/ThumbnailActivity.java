package com.imageselector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.imageselector.R;
import com.imageselector.adapter.ThumbnailAdapter;
import com.imageselector.entity.Thumbnail;

import java.util.List;


public class ThumbnailActivity extends BaseActivity {
    public static final String THUMBNAIL_LIST = "picPath";
    private static final int mColumnNum = 3;
    private List<Thumbnail> mThumbnailList;
    private GridView mGridView;
    private ThumbnailAdapter mThumbnailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);
        Intent intent = getIntent();
        mThumbnailList = (List<Thumbnail>) intent.getSerializableExtra(THUMBNAIL_LIST);
        initView();
    }

    private void initView() {
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.actionbar_title_thumbnail);
        mGridView = (GridView) findViewById(R.id.thumbnail_gridView);
        mGridView.setNumColumns(mColumnNum);
        mThumbnailAdapter = new ThumbnailAdapter(this, mThumbnailList, screenWidth / mColumnNum);
        mGridView.setAdapter(mThumbnailAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), mThumbnailList.get(position).getPicPath(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThumbnailActivity.this.finish();
            }
        });
    }
}
