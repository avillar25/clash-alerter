package es.alexvillar.clashalerter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/** Puente con la app: refrescar los widgets y pedir al lanzador que añada uno. */
@CapacitorPlugin(name = "ClashWidget")
public class WidgetPlugin extends Plugin {

    @PluginMethod
    public void refresh(PluginCall call) {
        WidgetData.updateAll(getContext());
        call.resolve();
    }

    @PluginMethod
    public void canPin(PluginCall call) {
        JSObject r = new JSObject();
        r.put("value", pinSupported(getContext()));
        call.resolve(r);
    }

    @PluginMethod
    public void pin(PluginCall call) {
        Context ctx = getContext();
        if (!pinSupported(ctx)) { call.reject("no-pin"); return; }
        String size = call.getString("size");
        Class<?> cls = "small".equals(size) ? ClashWidgetSmall.class : ClashWidgetLarge.class;
        boolean ok = AppWidgetManager.getInstance(ctx).requestPinAppWidget(new ComponentName(ctx, cls), null, null);
        JSObject r = new JSObject();
        r.put("value", ok);
        call.resolve(r);
    }

    private static boolean pinSupported(Context ctx) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && AppWidgetManager.getInstance(ctx).isRequestPinAppWidgetSupported();
    }
}
