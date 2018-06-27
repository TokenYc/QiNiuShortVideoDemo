package net.archeryc.qiniushortvideodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import net.archeryc.qiniushortvideodemo.record.RecordActivity;
import net.archeryc.qiniushortvideodemo.ui.progress.ProgressBarActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void recordVideo(View view) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.CAMERA, Permission.Group.MICROPHONE, Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                        startActivity(intent);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(MainActivity.this, "请先打开摄像头和录音权限", Toast.LENGTH_SHORT).show();
                    }
                }).start();

    }

    public void progressTest(View view) {
        Intent intent = new Intent(this, ProgressBarActivity.class);
        startActivity(intent);
    }
}
