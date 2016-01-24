package com.cgp.saral.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by karamjeetsingh on 08/09/15.
 */
public class TypefaceUtil
{
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("TypefaceUtil "," Cannotsetcustomfont"+customFontFileNameInAssets);
            Log.e("TypefaceUtil of "," instead of"+ defaultFontNameToOverride);
        }
    }
}
