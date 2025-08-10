package com.appmux.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.content.pm.ShortcutManagerCompat;

import com.appmux.R;

public class ShortcutHelper {

    public static void createPinnedWebsiteShortcut(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage(null);

        IconCompat icon = IconCompat.createWithResource(context, R.mipmap.ic_launcher);

        ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "web:" + url)
                .setShortLabel("Open Website")
                .setLongLabel(url)
                .setIcon(icon)
                .setIntent(intent)
                .build();

        requestPin(context, shortcut);
    }

    public static void createPinnedSettingsPanelShortcut(@NonNull Context context) {
        Intent intent = new Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
        IconCompat icon = IconCompat.createWithResource(context, R.mipmap.ic_launcher);
        ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "settings:internet")
                .setShortLabel("Internet Panel")
                .setLongLabel("Open Internet Connectivity Panel")
                .setIcon(icon)
                .setIntent(intent)
                .build();
        requestPin(context, shortcut);
    }

    private static void requestPin(@NonNull Context context, @NonNull ShortcutInfoCompat shortcut) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            boolean ok = ShortcutManagerCompat.requestPinShortcut(context, shortcut, null);
            if (!ok) {
                Toast.makeText(context, R.string.app_name, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Pinning shortcuts not supported by launcher", Toast.LENGTH_LONG).show();
        }
    }
}


