package com.appmux.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appmux.R;
import com.appmux.ui.adapters.ActionItemAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ActionBottomSheet extends BottomSheetDialogFragment implements ActionItemAdapter.OnActionClickListener {

    private static final String ARG_MODE = "mode";

    public enum Mode { APPS, SETTINGS, CONTACTS, FOLDERS, FILES, WEBSITES }

    public static void showApps(@NonNull androidx.fragment.app.FragmentManager fm) {
        show(fm, Mode.APPS);
    }
    public static void showSettings(@NonNull androidx.fragment.app.FragmentManager fm) {
        show(fm, Mode.SETTINGS);
    }
    public static void showContacts(@NonNull androidx.fragment.app.FragmentManager fm) {
        show(fm, Mode.CONTACTS);
    }
    public static void showFolders(@NonNull androidx.fragment.app.FragmentManager fm) { show(fm, Mode.FOLDERS); }
    public static void showFiles(@NonNull androidx.fragment.app.FragmentManager fm) { show(fm, Mode.FILES); }
    public static void showWebsites(@NonNull androidx.fragment.app.FragmentManager fm) { show(fm, Mode.WEBSITES); }

    private static void show(@NonNull androidx.fragment.app.FragmentManager fm, @NonNull Mode mode) {
        ActionBottomSheet sheet = new ActionBottomSheet();
        Bundle b = new Bundle();
        b.putString(ARG_MODE, mode.name());
        sheet.setArguments(b);
        sheet.show(fm, "ActionBottomSheet:" + mode.name());
    }

    private Mode mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String m = getArguments() != null ? getArguments().getString(ARG_MODE) : Mode.APPS.name();
        mode = Mode.valueOf(m);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mode == Mode.WEBSITES) {
            View v = inflater.inflate(R.layout.sheet_websites, container, false);
            MaterialButton btn = v.findViewById(R.id.btn_create);
            EditText input = v.findViewById(R.id.input_url);
            btn.setOnClickListener(view -> {
                String url = input.getText() != null ? input.getText().toString().trim() : "";
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(requireContext(), R.string.tile_websites, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!url.startsWith("http")) {
                    url = "https://" + url;
                }
                ShortcutHelper.createPinnedWebsiteShortcut(requireContext(), url);
                dismiss();
            });
            return v;
        } else {
            View v = inflater.inflate(R.layout.sheet_list, container, false);
            RecyclerView rv = v.findViewById(R.id.recycler);
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            ActionItemAdapter adapter = new ActionItemAdapter(buildActions(mode), this);
            rv.setAdapter(adapter);
            return v;
        }
    }

    private List<String> buildActions(Mode mode) {
        List<String> items = new ArrayList<>();
        switch (mode) {
            case APPS:
                items.add("Social");
                items.add("Communication");
                items.add("Tools");
                break;
            case SETTINGS:
                items.add("Internet Connectivity Panel");
                items.add("Bluetooth Settings");
                items.add("Display Settings");
                break;
            case CONTACTS:
                items.add("Dial a number");
                items.add("Pick a contact to view");
                break;
            case FOLDERS:
                items.add("Open a folder");
                break;
            case FILES:
                items.add("Open a file");
                break;
            default:
                break;
        }
        return items;
    }

    @Override
    public void onActionClicked(@NonNull String label) {
        Context ctx = requireContext();
        if (mode == Mode.SETTINGS) {
            if (label.contains("Internet")) {
                Intent panel = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                startActivity(panel);
                ShortcutHelper.createPinnedSettingsPanelShortcut(ctx);
            } else if (label.contains("Bluetooth")) {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            } else if (label.contains("Display")) {
                startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
            }
        } else if (mode == Mode.CONTACTS) {
            if (label.contains("Dial")) {
                startActivity(new Intent(Intent.ACTION_DIAL));
            } else if (label.contains("Pick")) {
                Intent pick = new Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
                startActivity(pick);
            }
        } else if (mode == Mode.APPS) {
            Toast.makeText(ctx, "Listing apps soon", Toast.LENGTH_SHORT).show();
        } else if (mode == Mode.FOLDERS) {
            Intent open = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivity(open);
        } else if (mode == Mode.FILES) {
            Intent open = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            open.addCategory(Intent.CATEGORY_OPENABLE);
            open.setType("*/*");
            startActivity(open);
        }
        dismiss();
    }
}


