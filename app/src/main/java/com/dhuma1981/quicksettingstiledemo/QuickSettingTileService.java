package com.dhuma1981.quicksettingstiledemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dhrumil on 7/24/2016.
 */

public class QuickSettingTileService extends TileService {

    private static final String TAG = "QSTILE";


    @Override
    public void onTileAdded() {
        Log.i(TAG, "Method: onTileAdded()");
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.i(TAG, "Method: onTileRemoved()");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        changeTileState(getQsTile().getState());
        Log.i(TAG, "Method: onStartListening()");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.i(TAG, "Method: onStopListening()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Method: onCreate()");
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.i(TAG, "Tile State: " + getQsTile().getState());

        if (!isLocked()) {
            updateTile();
        } else {
            unlockAndRun(new Runnable() {
                @Override
                public void run() {
                    updateTile();
                }
            });
        }
    }

    private void updateTile() {
        if (Tile.STATE_ACTIVE == getQsTile().getState()) {
            Toast.makeText(QuickSettingTileService.this, "New State: INACTIVE", Toast.LENGTH_SHORT).show();
            changeTileState(Tile.STATE_INACTIVE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("Quick Setting Demo");
            builder.setMessage("Quick Setting Demo Tile deactivated!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // nop
                }
            });
            showDialog(builder.create());
        } else if (Tile.STATE_INACTIVE == getQsTile().getState()) {
            Toast.makeText(QuickSettingTileService.this, "New State: ACTIVE", Toast.LENGTH_SHORT).show();
            changeTileState(Tile.STATE_ACTIVE);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.google.com"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityAndCollapse(intent);
        }
    }

    private void changeTileState(int newState) {
        /*getQsTile().setIcon(Icon.createWithResource(QuickSettingTileService.this, newState == Tile.STATE_INACTIVE ? R.drawable.tile_disabled : R.drawable.tile_enabled));*/
        getQsTile().setState(newState);
        getQsTile().updateTile();
    }
}
