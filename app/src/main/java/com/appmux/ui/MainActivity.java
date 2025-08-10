package com.appmux.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.appmux.R;
import com.appmux.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.appmux.databinding.ViewTileBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding.toolbar);
        setupTiles();
    }

    private void setupToolbar(@NonNull MaterialToolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_toggle_theme) {
                toggleTheme();
                return true;
            } else if (id == R.id.action_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            } else if (id == R.id.action_about) {
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            } else if (id == R.id.action_rate) {
                openRateUs();
                return true;
            } else if (id == R.id.action_feedback) {
                openTelegram();
                return true;
            }
            return false;
        });
    }

    private void setupTiles() {
        configureTile(binding.tileApps, getSystemIcon(android.R.drawable.ic_menu_view), getString(R.string.tile_apps), getString(R.string.content_desc_tile_apps), Tile.APPS);
        configureTile(binding.tileSettings, getSystemIcon(android.R.drawable.ic_menu_manage), getString(R.string.tile_settings), getString(R.string.content_desc_tile_settings), Tile.SETTINGS);
        configureTile(binding.tileContacts, getSystemIcon(android.R.drawable.ic_menu_myplaces), getString(R.string.tile_contacts), getString(R.string.content_desc_tile_contacts), Tile.CONTACTS);
        configureTile(binding.tileFolders, getSystemIcon(android.R.drawable.ic_menu_upload), getString(R.string.tile_folders), getString(R.string.content_desc_tile_folders), Tile.FOLDERS);
        configureTile(binding.tileFiles, getSystemIcon(android.R.drawable.ic_menu_save), getString(R.string.tile_files), getString(R.string.content_desc_tile_files), Tile.FILES);
        configureTile(binding.tileWebsites, getSystemIcon(android.R.drawable.ic_menu_share), getString(R.string.tile_websites), getString(R.string.content_desc_tile_websites), Tile.WEBSITES);
    }

    private Drawable getSystemIcon(int resId) {
        return getResources().getDrawable(resId, getTheme());
    }

    private void configureTile(@NonNull ViewTileBinding tileBinding,
                                @NonNull Drawable icon,
                                @NonNull String label,
                                @NonNull String contentDesc,
                                @NonNull Tile tile) {
        tileBinding.tileIcon.setImageDrawable(icon);
        tileBinding.tileLabel.setText(label);
        tileBinding.tileRoot.setContentDescription(contentDesc);
        tileBinding.tileRoot.setOnClickListener(v -> onTileClicked(tile));
    }

    private void onTileClicked(@NonNull Tile tile) {
        switch (tile) {
            case APPS:
                ActionBottomSheet.showApps(getSupportFragmentManager());
                break;
            case SETTINGS:
                ActionBottomSheet.showSettings(getSupportFragmentManager());
                break;
            case CONTACTS:
                ActionBottomSheet.showContacts(getSupportFragmentManager());
                break;
            case FOLDERS:
                ActionBottomSheet.showFolders(getSupportFragmentManager());
                break;
            case FILES:
                ActionBottomSheet.showFiles(getSupportFragmentManager());
                break;
            case WEBSITES:
                ActionBottomSheet.showWebsites(getSupportFragmentManager());
                break;
        }
    }

    private void toggleTheme() {
        int current = AppCompatDelegate.getDefaultNightMode();
        if (current == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void openRateUs() {
        String pkg = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
        }
    }

    private void openTelegram() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=Kumarsinhparmar")));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Kumarsinhparmar")));
        }
    }

    public enum Tile { APPS, SETTINGS, CONTACTS, FOLDERS, FILES, WEBSITES }
}


