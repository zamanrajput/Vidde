package com.devphics.vidde.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;

import androidx.annotation.Nullable;

import com.devphics.vidde.activities.MainActivity;

import java.io.File;

public class BasicData {
    Context context;

    public BasicData(Context context) {
        this.context = context;
    }

    public static String VIDEOS_DESTINATION="VIDDE/Status Saver/SavedVideos",IMAGES_DESTINATION="VIDDE/Status Saver/Images";

//    public File STATUS_DIRECTORY_WHATSAPP = Build.VERSION.SDK_INT >= 29 ? new File(
//            (getInternalStorageDirectoryPath() +
//                    File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses")
//    ) : new File(
//            getInternalStorageDirectoryPath()+
//                    File.separator + "WhatsApp/Media/.Statuses"
//    );


//    public  File STATUS_DIRECTORY_WHATSAPP_BW = Build.VERSION.SDK_INT >= 29 ? new File(
//            (getInternalStorageDirectoryPath() +
//                    File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses")
//    ) : new File(
//            getInternalStorageDirectoryPath() +
//                    File.separator + "WhatsApp Business/Media/.Statuses"
//    );


//    public  File STATUS_DIRECTORY_WHATSAPP_GB = Build.VERSION.SDK_INT >= 29 ? new File(
//            (getInternalStorageDirectoryPath() +
//                    File.separator + "Android/media/com.whatsapp/GBWhatsApp/Media/.Statuses")
//    ) : new File(
//            getInternalStorageDirectoryPath() +
//                    File.separator + "GBWhatsApp/Media/.Statuses"
//    );

    @Nullable
    public static  String getInternalStorageDirectoryPath(Context context) {
        String storageDirectoryPath;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            if(storageManager == null) {
                storageDirectoryPath = null; //you can replace it with the Environment.getExternalStorageDirectory().getAbsolutePath()
            } else {
                storageDirectoryPath = storageManager.getPrimaryStorageVolume().getDirectory().getAbsolutePath();
            }
        } else {
            storageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        return storageDirectoryPath;
    }

}
