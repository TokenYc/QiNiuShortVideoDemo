package net.archeryc.qiniushortvideodemo.record;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * @author ArcherYc
 * @date on 2018/6/26  下午3:10
 * @mail 247067345@qq.com
 */
public class VideoSizeUtils {

    private static final int TARGET_HEIGHT = 1024;

    /**
     * 以高1024为标准，根据屏幕宽高比计算宽。
     *
     * @param activity
     * @return
     */
    public static Pair<Integer, Integer> getEncodeSize(Activity activity) {
        Pair<Integer, Integer> screenSize = getScreenRealSize(activity);
        int screenWidth = screenSize.first;
        int screenHeight = screenSize.second;

        int encodeHeight = TARGET_HEIGHT;

        int encodeWidth = (int) (encodeHeight * ((float) screenWidth / screenHeight));

        if (encodeWidth % 2 != 0) {
            encodeWidth++;
        }

        return new Pair<>(encodeWidth, encodeHeight);
    }

    /**
     * 获取屏幕真实高度
     *
     * @param activity
     * @return
     */
    private static Pair<Integer, Integer> getScreenRealSize(Activity activity) {
        Pair<Integer, Integer> commonSize = getScreenSize(activity);
        int realWidth = commonSize.first;
        int realHeight = commonSize.second;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            realWidth = dm.widthPixels;
            realHeight = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<>(realWidth, realHeight);
    }

    /**
     * 使用普通的方法获取宽高，高度在某些带虚拟按键的设备上可能不准确
     *
     * @param context
     * @return
     */
    private static Pair<Integer, Integer> getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * 获取视频宽高
     *
     * @param videoPath
     * @return
     */
    public static VideoInfo getVideoSize(String videoPath) {
        VideoInfo videoInfo=new VideoInfo();
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(videoPath);
        String height = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String rotation = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        String bitRate = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

        if (rotation.equals("90") || rotation.equals("270")) {
            String temp = height;
            height = width;
            width = temp;
        }
        videoInfo.setWidth(width);
        videoInfo.setHeight(height);
        videoInfo.setRotation(rotation);
        videoInfo.setBitRate(bitRate);
        return videoInfo;
    }


}
