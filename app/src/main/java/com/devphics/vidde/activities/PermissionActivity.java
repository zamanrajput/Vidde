package com.devphics.vidde.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.devphics.vidde.databinding.ActivityPermissionsBinding;


public class PermissionActivity extends AppCompatActivity {
    public static final int WHATSAPP_SIMPLE = 100, WHATSAPP_BUSINESS = 101;
    private static final String preferenceName = "PATH_DATA";
    private static final int REQ_CODE1 = 111;
    static boolean check1 = false, check2 = false;

    public static String WAPath = null, BWAPath = null;

    ActivityPermissionsBinding binding;
    final int REQ_CODE_WA = 1000, REQ_CODE_BWA = 1111;
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.iv.setOnClickListener(view -> startActivity(new Intent(PermissionActivity.this, Help.class)));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (isPermissionsAvailAndroidQ(getApplicationContext())) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                this.finish();

            } else {
                binding.allowBtnSW.setVisibility(View.VISIBLE);
                binding.allowBtnSW.setOnClickListener(view -> getPermissionsAndroidQ(WHATSAPP_SIMPLE));
                binding.allowBtnBW.setVisibility(View.VISIBLE);
                binding.allowBtnBW.setOnClickListener(view -> getPermissionsAndroidQ(WHATSAPP_BUSINESS));
            }

        } else {

            binding.helpTxt.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.GONE);
            if (CheckPermissionsLessThanQ()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                this.finish();
            } else {
                binding.allowBtnSimple.setVisibility(View.VISIBLE);
                binding.allowBtnSimple.setOnClickListener(view -> RequestPermissionsLessThanQ());

            }
//
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static boolean isPermissionsAvailAndroidQ(Context context) {
        boolean Result = false;

        SharedPreferences preferences = context.getSharedPreferences(preferenceName, MODE_PRIVATE);
        String simpleWaPath = preferences.getString("WAPath", "");
        String businessWaPath = preferences.getString("BWAPath", "");
        if (!simpleWaPath.equals("") && !businessWaPath.equals("")) {
            WAPath = simpleWaPath;
            BWAPath = businessWaPath;
            Result = true;
        }
        return Result;

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getPermissionsAndroidQ(int WHATSAPP_TYPE) {

        StorageManager manager = (StorageManager) getSystemService(STORAGE_SERVICE);
        Intent intent = manager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        int REQ_CODE = 0;

        String targetDir = "";
        switch (WHATSAPP_TYPE) {
            case WHATSAPP_SIMPLE:
                targetDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
                REQ_CODE = REQ_CODE_WA;
                break;
            case WHATSAPP_BUSINESS:
                targetDir = "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses";
                REQ_CODE = REQ_CODE_BWA;
                break;
        }

        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/tree/");
        scheme += "%3A" + targetDir;


        uri = Uri.parse(scheme);


        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        intent.putExtra("android.provider.extra.SHOW_ADVANCED", true);
        startActivityForResult(intent, REQ_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            SharedPreferences preferences = getSharedPreferences(preferenceName, MODE_PRIVATE);
            Uri treeUri = data.getData();
            if (treeUri != null) {
                getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (requestCode == REQ_CODE_WA) {
                    WAPath = treeUri.toString();
                    if (WAPath.contains("com.whatsapp")) {
                        preferences.edit().putString("WAPath", WAPath).apply();
                        check1 = true;
                        binding.allowBtnSW.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Path! Locate the WhatsApp Path", Toast.LENGTH_LONG).show();
                    }

                } else if (requestCode == REQ_CODE_BWA) {
                    BWAPath = treeUri.toString();
                    if (BWAPath.contains("com.whatsapp.w4b")) {
                        check2 = true;
                        binding.allowBtnBW.setVisibility(View.GONE);
                        preferences.edit().putString("BWAPath", BWAPath).apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Path! Locate the Business WhatsApp Path", Toast.LENGTH_LONG).show();
                    }


                }


            }
            if (check2 && check1) {
                startActivity(new Intent(PermissionActivity.this, MainActivity.class));
            }
        }


    }

    private boolean CheckPermissionsLessThanQ() {
        int ReadCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int WriteCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return ReadCheck == PackageManager.PERMISSION_GRANTED && WriteCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "Permissions Not Granted", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void RequestPermissionsLessThanQ() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(PermissionActivity.this, permissions, REQ_CODE1);
        }
    }


}
