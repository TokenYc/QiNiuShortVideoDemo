package net.archeryc.qiniushortvideodemo.filter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiniu.pili.droid.shortvideo.PLAudioEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLBuiltinFilter;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting;
import com.qiniu.pili.droid.shortvideo.PLFaceBeautySetting;
import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordSetting;
import com.qiniu.pili.droid.shortvideo.PLShortVideoRecorder;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;

import net.archeryc.qiniushortvideodemo.R;
import net.archeryc.qiniushortvideodemo.record.VideoSizeUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "tag_filter";

    private final static String CACHE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "qiniu_demo" + File.separator;

    private final static String TEST_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "qiniu_demo" + File.separator + "test.mp4";

    private final static int MAX_TIME = 15 * 1000;



    private TextView tvFilter;
    private RecyclerView recyclerView;

    private PLShortVideoRecorder mRecorder = new PLShortVideoRecorder();

    private GLSurfaceView glSurfaceView;

    private int mCurrentFilter = 0;

    private PLBuiltinFilter[] filters;

    private FilterDetector filterDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        tvFilter = findViewById(R.id.tv_filter);
        recyclerView = findViewById(R.id.recyclerView);

        initParams();

        initFilterGesture();

        filters = mRecorder.getBuiltinFilterList();


        ScrollSpeedLinearLayoutManager layoutManger = new ScrollSpeedLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        layoutManger.setSpeedSlow();
        recyclerView.setLayoutManager(layoutManger);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(new MyAdapter());

        glSurfaceView.setOnTouchListener(this);

    }

    private void initFilterGesture() {
        filterDetector = new FilterDetector(this);
        filterDetector.setOnFilterChangeListener(new FilterDetector.OnFilterChangeListener() {
            @Override
            public void onFilterChange(int direction) {
                if (direction > 0) {
                    if (mCurrentFilter > 0) {
                        mCurrentFilter--;
                        mRecorder.setBuiltinFilter(filters[mCurrentFilter].getName());
                        tvFilter.setText(filters[mCurrentFilter].getName());
                        recyclerView.smoothScrollToPosition(mCurrentFilter);
                    }
                } else {
                    if (mCurrentFilter + 1 < mRecorder.getBuiltinFilterList().length - 2) {
                        mCurrentFilter++;
                        Log.d(TAG, "mCurrentFilter---->" + mCurrentFilter + "  name---->" + mRecorder.getBuiltinFilterList()[mCurrentFilter].getName());
                        mRecorder.setBuiltinFilter(filters[mCurrentFilter].getName());
                        tvFilter.setText(filters[mCurrentFilter].getName());
                        recyclerView.smoothScrollToPosition(mCurrentFilter);
                    }
                }
            }
        });
    }

    private void initParams() {
        PLCameraSetting cameraSetting = new PLCameraSetting();

        // 摄像头采集选项
        cameraSetting.setCameraId(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK);
        cameraSetting.setCameraPreviewSizeRatio(PLCameraSetting.CAMERA_PREVIEW_SIZE_RATIO.RATIO_16_9);
        cameraSetting.setCameraPreviewSizeLevel(PLCameraSetting.CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_720P);

        // 麦克风采集选项
        PLMicrophoneSetting microphoneSetting = new PLMicrophoneSetting();

        // 视频编码选项
        PLVideoEncodeSetting videoEncodeSetting = new PLVideoEncodeSetting(this);
//        videoEncodeSetting.setEncodingSizeLevel(PLVideoEncodeSetting.VIDEO_ENCODING_SIZE_LEVEL
//                .VIDEO_ENCODING_SIZE_LEVEL_720P_3);
        Pair<Integer, Integer> encodeSize = VideoSizeUtils.getEncodeSize(this);
        videoEncodeSetting.setPreferredEncodingSize(encodeSize.first, encodeSize.second);
        //参考微信设置码率1650kb/s
        videoEncodeSetting.setEncodingBitrate(1650 * 1024);
        videoEncodeSetting.setEncodingFps(25);
        videoEncodeSetting.setHWCodecEnabled(true);

        // 音频编码选项
        PLAudioEncodeSetting audioEncodeSetting = new PLAudioEncodeSetting();
        audioEncodeSetting.setHWCodecEnabled(true);

        // 美颜选项
        PLFaceBeautySetting faceBeautySetting = new PLFaceBeautySetting(1.0f, 0.5f, 0.5f);
        faceBeautySetting.setEnable(false);

        PLRecordSetting recordSetting = new PLRecordSetting();
        recordSetting.setMaxRecordDuration(MAX_TIME);
        recordSetting.setVideoCacheDir(CACHE_PATH);
        recordSetting.setVideoFilepath(TEST_PATH);

        mRecorder.prepare(glSurfaceView, cameraSetting, microphoneSetting, videoEncodeSetting, audioEncodeSetting,
                faceBeautySetting, recordSetting);
//        mRecorder.setRecordSpeed(2);

    }

    public void setFilters(View view) {
        PLBuiltinFilter[] filters = mRecorder.getBuiltinFilterList();

        for (PLBuiltinFilter filter : filters) {
            Log.d(TAG, "filter name--->" + filter.getName() + "\tpath---->" + filter.getAssetFilePath());
        }
        mRecorder.setBuiltinFilter(filters[3].getName());

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        filterDetector.handleEvent(event);
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(FilterActivity.this).inflate(
                    R.layout.item_filter, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.imageView.setImageBitmap(getImageFromAssetsFile(FilterActivity.this,
                    filters[position].getAssetFilePath()));
        }

        @Override
        public int getItemCount() {
            return filters.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image);
            }
        }
    }


    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
