package hhtv.com.mp3slider.helper;

import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import hhtv.com.mp3slider.R;

/**
 * Created by nienb on 12/4/16.
 */
public class CommonErrorDialogBuilder extends MaterialDialog.Builder {
    public CommonErrorDialogBuilder(Context context) {
        super(context);
        this.title(R.string.error)
                .content(R.string.error_on_action)
                .positiveText(R.string.dismiss)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }
}