package net.archeryc.qiniushortvideodemo.record;

import android.opengl.GLSurfaceView;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.qiniu.pili.droid.shortvideo.PLAudioEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting;
import com.qiniu.pili.droid.shortvideo.PLFaceBeautySetting;
import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordStateListener;
import com.qiniu.pili.droid.shortvideo.PLShortVideoRecorder;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import net.archeryc.qiniushortvideodemo.R;
import net.archeryc.qiniushortvideodemo.bright.BrightnessDetector;
import net.archeryc.qiniushortvideodemo.ui.focus.FocusView;
import net.archeryc.qiniushortvideodemo.ui.progress.SectionProgressBar;

import java.io.File;

public class RecordActivity extends AppCompatActivity implements PLRecordStateListener {

    private final static String Tag = "qiniu Record";

    private final static String CACHE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "qiniu_demo" + File.separator;
    private final static String TEST_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "qiniu_demo" + File.separator + "test.mp4";

    private final static int MAX_TIME = 15 * 1000;
    private ConstraintLayout clRoot;
    private ConstraintLayout clSetting;
    private ToggleButton tbtnRecord;
    private ToggleButton tbtnFlash;
    private ToggleButton tlBtnBeauty;
    private Button btnSwitchCamera;
    private TextView tvDesc;
    private SectionProgressBar sectionProgressBar;
    private Button btnDelete;
    private Button btnConcat;
    private Button btnSpeedSlow;
    private Button btnSpeedNormal;
    private Button btnSpeedFast;
    private Button btnClock;

    private GLSurfaceView glSurfaceView;
    private PLShortVideoRecorder mRecorder = new PLShortVideoRecorder();
    private float mCurrentSpeed = 1.0f;
    PLFaceBeautySetting faceBeautySetting;
    private BrightnessDetector brightnessDetector;
    private int mExposureCompensation = 0;
    private int mStartExposureCompensation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        clRoot = findViewById(R.id.cl_root);
        clSetting = findViewById(R.id.cl_setting);
        sectionProgressBar = findViewById(R.id.progressBar);
        tvDesc = findViewById(R.id.tv_desc);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        tbtnRecord = findViewById(R.id.btn_record);
        tbtnFlash = findViewById(R.id.tbtn_flash);
        btnSwitchCamera = findViewById(R.id.btn_switch_camera);
        btnDelete = findViewById(R.id.btn_delete);
        btnConcat = findViewById(R.id.btn_concat);
        btnSpeedSlow = findViewById(R.id.btn_speed_slow);
        btnSpeedNormal = findViewById(R.id.btn_speed_normal);
        btnSpeedFast = findViewById(R.id.btn_speed_fast);
        tlBtnBeauty = findViewById(R.id.tl_btn_beauty);
        btnClock = findViewById(R.id.btn_clock);

        initParams();

        initView();


        initListener();
    }

    private void initView() {


        final FocusView focusView = new FocusView(this);
        focusView.setImageResource(R.mipmap.icon_focus_video);
        clRoot.addView(focusView);
        focusView.attach(new FocusView.OnFocusListener() {
            @Override
            public void onFocus(int centerX, int centerY, int width, int height) {
                if (mRecorder != null) {
                    mRecorder.manualFocus(width, height, centerX, centerY);
                }
            }
        });
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                focusView.handleEvent(event);
                brightnessDetector.handleEvent(event);
                return true;
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
        faceBeautySetting = new PLFaceBeautySetting(0.5f, 0.5f, 0.5f);
        faceBeautySetting.setEnable(false);

        PLRecordSetting recordSetting = new PLRecordSetting();
        recordSetting.setMaxRecordDuration(MAX_TIME);
        recordSetting.setVideoCacheDir(CACHE_PATH);
        recordSetting.setVideoFilepath(TEST_PATH);

        mRecorder.setRecordStateListener(this);
        mRecorder.prepare(glSurfaceView, cameraSetting, microphoneSetting, videoEncodeSetting, audioEncodeSetting,
                faceBeautySetting, recordSetting);
//        mRecorder.setRecordSpeed(2);
//        mRecorder.setBuiltinFilter(mRecorder.getBuiltinFilterList()[2].getName());
    }

    private void initListener() {
        tbtnRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!sectionProgressBar.isRunning()) {
                        mRecorder.beginSection();
                    }
                } else {
                    if (sectionProgressBar.isRunning()) {
                        mRecorder.endSection();
                    }
                }
            }
        });
        tbtnFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRecorder.setFlashEnabled(true);
                } else {
                    mRecorder.setFlashEnabled(false);
                }
            }
        });

        tlBtnBeauty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    faceBeautySetting.setEnable(true);
                } else {
                    faceBeautySetting.setEnable(false);
                }
                mRecorder.updateFaceBeautySetting(faceBeautySetting);
            }
        });
        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.switchCamera();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sectionProgressBar.isRunning()) {
                    mRecorder.deleteLastSection();
                }
            }
        });

        btnConcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concat();
            }
        });

        sectionProgressBar.setSectionProgressListener(new SectionProgressBar.SectionProgressListener() {
            @Override
            public void onProgressEnd() {
            }
        });

        btnSpeedSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpeed(0.5f);
            }
        });

        btnSpeedNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpeed(1.0f);
            }
        });

        btnSpeedFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpeed(2.0f);
            }
        });

        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountDownTimer timer = new CountDownTimer(3 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Toast.makeText(RecordActivity.this, millisUntilFinished / 1000 + "秒", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                Toast.makeText(RecordActivity.this, "3秒", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpeed(float speed) {
        Toast.makeText(this, "设置速度--->" + speed, Toast.LENGTH_SHORT).show();
        mRecorder.setRecordSpeed(speed);
        mCurrentSpeed = speed;
    }

    private void concat() {
        mRecorder.concatSections(new PLVideoSaveListener() {
            @Override
            public void onSaveVideoSuccess(String s) {
                mRecorder.deleteAllSections();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecordActivity.this, "合成成功", Toast.LENGTH_SHORT).show();
                        VideoInfo videoInfo = VideoSizeUtils.getVideoSize(TEST_PATH);
                        tvDesc.setText(String.format("width--->%s\nheight--->%s\nrotation-->%s\nbitrate--->%s",
                                videoInfo.getWidth(),
                                videoInfo.getHeight(),
                                videoInfo.getRotation(),
                                videoInfo.getBitRate()));
                        reset();
                    }
                });
            }

            @Override
            public void onSaveVideoFailed(int i) {

            }

            @Override
            public void onSaveVideoCanceled() {
                clSetting.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressUpdate(float v) {

            }
        });
    }

    private void reset() {
        sectionProgressBar.reset();
        tbtnRecord.setChecked(false);
    }

    @Override
    public void onReady() {
        Log.d(Tag, "-----onReady-----");
        tbtnRecord.setVisibility(View.VISIBLE);
        brightnessDetector = new BrightnessDetector(this, mRecorder.getMinExposureCompensation(),
                mRecorder.getMaxExposureCompensation());
        brightnessDetector.setOnBrightnessChangedListener(new BrightnessDetector.OnBrightnessChangedListener() {
            @Override
            public void onBrightnessChanged(int exposureCompensation) {
                mRecorder.setExposureCompensation(exposureCompensation);
            }
        });
    }

    @Override
    public void onError(int i) {
        Log.d(Tag, "-----onError-----");
    }

    @Override
    public void onDurationTooShort() {
        Log.d(Tag, "-----onDurationTooShort-----");
    }

    @Override
    public void onRecordStarted() {
        Log.d(Tag, "-----onRecordStarted-----");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clSetting.setVisibility(View.GONE);
                sectionProgressBar.beginSection((int) ((1 / mCurrentSpeed) * MAX_TIME * (1 - sectionProgressBar.getProgress())));
            }
        });
    }

    @Override
    public void onRecordStopped() {
        Log.d(Tag, "-----onRecordStopped-----");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tbtnRecord.setChecked(false);
                clSetting.setVisibility(View.VISIBLE);
                sectionProgressBar.endSection();
            }
        });
    }

    @Override
    public void onSectionIncreased(long l, long l1, int i) {
        Log.d(Tag, "-----onSectionIncreased-----");
    }

    @Override
    public void onSectionDecreased(long l, long l1, int i) {
        Log.d(Tag, "-----onSectionDecreased-----");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sectionProgressBar.deleteLastSection();
            }
        });
    }

    @Override
    public void onRecordCompleted() {
        Log.d(Tag, "-----onRecordCompleted-----");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecorder.resume();
        ThemeUtils.hideSystemUI(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecorder.destroy();
    }

}
