package xyz.igorgee.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

public class UIUtilities {
    
    public static void makeSnackbar(View view, int stringID) {
        makeSnackbar(view, stringID, null);
    }

    public static void makeSnackbar(View view, String content) {
        makeSnackbar(view, content, null);
    }

    public static void makeSnackbar(View view, int stringID, String confirm) {
        Snackbar.make(view, stringID, Snackbar.LENGTH_SHORT)
                .setAction(confirm != null ? confirm : "Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public static void makeSnackbar(View view, String content, String confirm) {
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT)
                .setAction(confirm != null ? confirm : "Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public static void makeAlertDialog(Context context, String message, int iconID) {
        new AlertDialog.Builder(context)
                .setTitle("Image Information")
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

    public static void makeAlertDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("Image Information")
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

    public static void makeAlertDialog(Context context, String message,
                                       String positiveButton, String negativeButton,
                                       int iconID) {
        new AlertDialog.Builder(context)
                .setTitle("Image Information")
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
