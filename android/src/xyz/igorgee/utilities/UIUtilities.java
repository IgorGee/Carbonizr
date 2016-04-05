package xyz.igorgee.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

public class UIUtilities {
    
    public static void makeSnackbar(View view, int stringID) {
        makeSnackbar(view, stringID, Snackbar.LENGTH_SHORT, null);
    }

    public static void makeSnackbar(View view, String content) {
        makeSnackbar(view, content, Snackbar.LENGTH_SHORT, null);
    }

    public static void makeSnackbar(View view, int stringID, int duration) {
        makeSnackbar(view, stringID, duration, null);
    }

    public static void makeSnackbar(View view, String content, int duration) {
        makeSnackbar(view, content, duration, null);
    }

    public static void makeSnackbar(View view, int stringID, int duration, String confirm) {
        Snackbar.make(view, stringID, duration)
                .setAction(confirm != null ? confirm : "Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public static void makeSnackbar(View view, String content, int duration, String confirm) {
        Snackbar.make(view, content, duration)
                .setAction(confirm != null ? confirm : "Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public static void makeAlertDialog(Context context, String title, String message, int iconID) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(iconID)
                .show();
    }

    public static void makeAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static void makeAlertDialog(Context context, String title, String message,
                                       String positiveButton, String negativeButton,
                                       int iconID) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(iconID)
                .show();
    }
}
