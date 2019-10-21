package mn.zaal.zaal.tuslah;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

public class UiHelper {

    public static MaterialDialog showAlwaysCircularProgressDialog(Context callingClassContext, String content) {
        return new MaterialDialog.Builder(callingClassContext)
                .progress(true, 100)
                .cancelable(false)
                .show();
    }
}
