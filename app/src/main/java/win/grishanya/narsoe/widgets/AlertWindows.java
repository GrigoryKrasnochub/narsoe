package win.grishanya.narsoe.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import win.grishanya.narsoe.BuildConfig;
import win.grishanya.narsoe.R;

public class AlertWindows {

    public static void showAboutProgramAlertWindow(Context context){
        View alertView = View.inflate(context,R.layout.alert_dialog_about,null);

        String verCode = BuildConfig.VERSION_NAME;
        String gitURL = context.getString(R.string.about_GIT_URL);

        TextView versionCodeTextView = alertView.findViewById(R.id.about_program_version_text_view);
        TextView gitURLTextView = alertView.findViewById(R.id.about_program_git_url);

        versionCodeTextView.setText(verCode);


        final SpannableString s =
                new SpannableString(gitURL);
        Linkify.addLinks(s, Linkify.WEB_URLS);

        gitURLTextView.setText(s);
        gitURLTextView.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_about))
                .setView(alertView)
                .setCancelable(true)
                .setNegativeButton(context.getString(R.string.about_close_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
