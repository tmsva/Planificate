package tmsva.org.free.planificate.utilities;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class Toaster {
    public static void show(Context context, int stringResId, boolean isLong, String extraMsge) {
        int toastLength = Toast.LENGTH_SHORT;
        if (isLong) toastLength = Toast.LENGTH_LONG;
        if(TextUtils.isEmpty(extraMsge))
            Toast.makeText(context, stringResId, toastLength).show();
        else
            Toast.makeText(context,
                    context.getResources().getString(stringResId)+" "+extraMsge, toastLength).show();
    }

    public static void show(Context context, int stringResId) {
        show(context, stringResId, false, null);
    }

    public static void show(Context context, int stringResId, boolean isLong) {
        show(context, stringResId, isLong, null);
    }

    public static void show(Context context, int stringResId, String addedMsg) {
        show(context, stringResId, false, addedMsg);
    }
}