package net.archeryc.qiniushortvideodemo.record;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

/**
 * @author ArcherYc
 * @date on 2018/6/26  上午9:57
 * @mail 247067345@qq.com
 */
public class ThemeUtils {

    /**
     * 在theme中要加入windowFullscreen为true
     * 在onResume中调用
     * 隐藏状态栏和底部虚拟按键
     *
     * @param activity
     */
    public static void hideSystemUI(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;//0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(Color.parseColor("#00000000"));
        }
        try {
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
