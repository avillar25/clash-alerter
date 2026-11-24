package es.alexvillar.clashalerter;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/** Widget pequeño (2×2): la próxima mejora que acaba y si hay trabajadores libres. */
public class ClashWidgetSmall extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        WidgetData.updateAll(context);
    }
}
