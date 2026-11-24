package es.alexvillar.clashalerter;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/** Widget grande (4×2): próximas mejoras, trabajadores libres y ayudantes listos. */
public class ClashWidgetLarge extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        WidgetData.updateAll(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Aviso programado: repintar los dos tamaños aunque solo haya uno puesto
        if (WidgetData.ACTION_TICK.equals(intent.getAction())) { WidgetData.updateAll(context); return; }
        super.onReceive(context, intent);
    }
}
