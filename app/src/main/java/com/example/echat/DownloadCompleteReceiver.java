package com.example.echat;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            // Get the download status
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId != -1) {

                Toast.makeText(context, "Check Download Directory", Toast.LENGTH_SHORT).show();
                
                // The download is complete, you can perform additional actions if needed
                // For example, check the downloaded file in the specified directory
            }
        }
    }
}
