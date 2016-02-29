package xyz.igorgee.utilities;

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
}
