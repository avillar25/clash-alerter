package es.alexvillar.clashalerter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Pinta los widgets con los datos que deja la app (clave "widget" de Preferences, en el
 * almacén "CapacitorStorage"). Las cuentas atrás de menos de un día van con Chronometer, que
 * cuenta solo sin gastar batería; el widget se vuelve a pintar cuando acaba la próxima mejora.
 */
public class WidgetData {
    static final String SCHEME = "es.alexvillar.clashalerter";
    static final String ACTION_TICK = "es.alexvillar.clashalerter.WIDGET_TICK";
    private static final String[] WORKERS = {"b", "l", "p", "bb", "bl"};

    static class Up { String acc, accId, name; int color; long end; }
    static class Free { String acc, accId; String text; }

    public static void updateAll(Context ctx) {
        long now = System.currentTimeMillis();
        JSONObject data = read(ctx);
        List<Up> ups = new ArrayList<>();
        List<Free> free = new ArrayList<>();
        List<String> ready = new ArrayList<>();
        long next = Long.MAX_VALUE;
        if (data != null) {
            JSONArray accs = data.optJSONArray("accs");
            for (int i = 0; accs != null && i < accs.length(); i++) {
                JSONObject a = accs.optJSONObject(i); if (a == null) continue;
                String name = a.optString("name"), id = a.optString("id");
                int color = parseColor(a.optString("color"));
                JSONObject cap = a.optJSONObject("cap"), ends = a.optJSONObject("ends");
                // trabajadores libres ahora mismo
                StringBuilder sb = new StringBuilder();
                for (String w : WORKERS) {
                    int c = cap == null ? 0 : cap.optInt(w, 0); if (c <= 0) continue;
                    int busy = 0; JSONArray e = ends == null ? null : ends.optJSONArray(w);
                    for (int k = 0; e != null && k < e.length(); k++) { long t = e.optLong(k); if (t > now) { busy++; next = Math.min(next, t); } }
                    int f = c - busy;
                    if (f > 0) { if (sb.length() > 0) sb.append(", "); sb.append(workerText(w, f)); }
                }
                if (sb.length() > 0) { Free fr = new Free(); fr.acc = name; fr.accId = id; fr.text = sb.toString(); free.add(fr); }
                // mejoras en curso
                JSONArray list = a.optJSONArray("ups");
                for (int k = 0; list != null && k < list.length(); k++) {
                    JSONObject u = list.optJSONObject(k); if (u == null) continue;
                    long end = u.optLong("e"); if (end <= now) continue;
                    Up up = new Up(); up.acc = name; up.accId = id; up.color = color; up.name = u.optString("n"); up.end = end; ups.add(up);
                }
                // ayudantes y torre del reloj
                JSONArray rd = a.optJSONArray("ready");
                for (int k = 0; rd != null && k < rd.length(); k++) {
                    JSONObject r = rd.optJSONObject(k); if (r == null) continue;
                    long t = r.optLong("e");
                    if (t <= now) ready.add(r.optString("t") + (accs.length() > 1 ? " · " + name : ""));
                    else next = Math.min(next, t);
                }
            }
        }
        java.util.Collections.sort(ups, (x, y) -> Long.compare(x.end, y.end));
        if (!ups.isEmpty()) next = Math.min(next, ups.get(0).end);

        AppWidgetManager mgr = AppWidgetManager.getInstance(ctx);
        for (int id : mgr.getAppWidgetIds(new ComponentName(ctx, ClashWidgetLarge.class)))
            mgr.updateAppWidget(id, large(ctx, data == null, ups, free, ready, now));
        for (int id : mgr.getAppWidgetIds(new ComponentName(ctx, ClashWidgetSmall.class)))
            mgr.updateAppWidget(id, small(ctx, data == null, ups, free, now));
        scheduleNext(ctx, next, now);
    }

    private static RemoteViews large(Context ctx, boolean empty, List<Up> ups, List<Free> free, List<String> ready, long now) {
        RemoteViews v = new RemoteViews(ctx.getPackageName(), R.layout.widget_large);
        v.setOnClickPendingIntent(R.id.w_root, open(ctx, null, 0));
        v.setTextViewText(R.id.w_updated, "act. " + hhmm(now));
        if (!free.isEmpty()) {
            Free f = free.get(0);
            v.setViewVisibility(R.id.w_free, View.VISIBLE);
            v.setTextViewText(R.id.w_free_text, f.acc + ": " + f.text + (free.size() > 1 ? " (+" + (free.size() - 1) + ")" : ""));
            v.setOnClickPendingIntent(R.id.w_free, open(ctx, f.accId, 1));
        } else v.setViewVisibility(R.id.w_free, View.GONE);
        int[] rows = {R.id.w_row1, R.id.w_row2, R.id.w_row3};
        int[] dots = {R.id.w_dot1, R.id.w_dot2, R.id.w_dot3};
        int[] names = {R.id.w_name1, R.id.w_name2, R.id.w_name3};
        int[] chr = {R.id.w_chr1, R.id.w_chr2, R.id.w_chr3};
        int[] txt = {R.id.w_time1, R.id.w_time2, R.id.w_time3};
        for (int i = 0; i < 3; i++) {
            if (i < ups.size()) {
                Up u = ups.get(i);
                v.setViewVisibility(rows[i], View.VISIBLE);
                v.setInt(dots[i], "setColorFilter", u.color);
                v.setTextViewText(names[i], u.acc + " · " + u.name);
                setTime(v, chr[i], txt[i], u.end, now);
                v.setOnClickPendingIntent(rows[i], open(ctx, u.accId, 10 + i));
            } else v.setViewVisibility(rows[i], View.GONE);
        }
        v.setViewVisibility(R.id.w_empty, ups.isEmpty() ? View.VISIBLE : View.GONE);
        v.setTextViewText(R.id.w_empty, empty ? "Abre la app para cargar tus cuentas" : "No hay mejoras en curso");
        if (!ready.isEmpty()) {
            v.setViewVisibility(R.id.w_ready, View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ready.size() && i < 3; i++) { if (i > 0) sb.append("   "); sb.append("✓ ").append(ready.get(i)); }
            v.setTextViewText(R.id.w_ready, sb.toString());
        } else v.setViewVisibility(R.id.w_ready, View.GONE);
        return v;
    }

    private static RemoteViews small(Context ctx, boolean empty, List<Up> ups, List<Free> free, long now) {
        RemoteViews v = new RemoteViews(ctx.getPackageName(), R.layout.widget_small);
        if (ups.isEmpty()) {
            v.setTextViewText(R.id.s_label, empty ? "Clash Alerter" : "Próxima mejora");
            v.setViewVisibility(R.id.s_chr, View.GONE);
            v.setViewVisibility(R.id.s_time, View.VISIBLE);
            v.setTextViewText(R.id.s_time, "—");
            v.setTextViewText(R.id.s_name, empty ? "Abre la app" : "Nada en curso");
            v.setTextViewText(R.id.s_acc, "");
            v.setOnClickPendingIntent(R.id.s_root, open(ctx, null, 0));
        } else {
            Up u = ups.get(0);
            v.setTextViewText(R.id.s_label, "Próxima mejora");
            setTime(v, R.id.s_chr, R.id.s_time, u.end, now);
            v.setTextViewText(R.id.s_name, u.name);
            v.setTextViewText(R.id.s_acc, u.acc);
            v.setTextColor(R.id.s_acc, u.color);
            v.setOnClickPendingIntent(R.id.s_root, open(ctx, u.accId, 20));
        }
        int n = 0; for (Free f : free) n += countIn(f.text);
        if (n > 0) { v.setViewVisibility(R.id.s_free, View.VISIBLE); v.setTextViewText(R.id.s_free, "⚠ " + n + (n == 1 ? " libre" : " libres")); }
        else v.setViewVisibility(R.id.s_free, View.GONE);
        return v;
    }

    /** Menos de un día: cuenta atrás en directo. Más: texto tipo "1 d 4 h". */
    private static void setTime(RemoteViews v, int chrId, int txtId, long end, long now) {
        long left = end - now;
        if (left < 24L * 3600_000L && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.setViewVisibility(chrId, View.VISIBLE);
            v.setViewVisibility(txtId, View.GONE);
            v.setChronometer(chrId, SystemClock.elapsedRealtime() + left, null, true);
            v.setChronometerCountDown(chrId, true);
        } else {
            v.setViewVisibility(chrId, View.GONE);
            v.setViewVisibility(txtId, View.VISIBLE);
            v.setTextViewText(txtId, dur(left));
        }
    }

    private static PendingIntent open(Context ctx, String accId, int req) {
        Intent i;
        if (accId != null && !accId.isEmpty()) {
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(SCHEME + "://cuenta/" + Uri.encode(accId)));
            i.setClass(ctx, MainActivity.class);
        } else {
            i = new Intent(ctx, MainActivity.class);
            i.setAction(Intent.ACTION_MAIN);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(ctx, req, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    /** Vuelve a pintar cuando acaba algo (y como mucho cada hora, para los textos de días). */
    private static void scheduleNext(Context ctx, long next, long now) {
        long at = Math.min(next == Long.MAX_VALUE ? now + 3600_000L : next + 1000, now + 3600_000L);
        // Lo de más de un día pasa a cuenta atrás en directo al bajar de 24 h
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        Intent i = new Intent(ctx, ClashWidgetLarge.class);
        i.setAction(ACTION_TICK);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 99, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) am.setAndAllowWhileIdle(AlarmManager.RTC, at, pi);
        else am.set(AlarmManager.RTC, at, pi);
    }

    private static JSONObject read(Context ctx) {
        try {
            SharedPreferences p = ctx.getSharedPreferences("CapacitorStorage", Context.MODE_PRIVATE);
            String s = p.getString("widget", null);
            return s == null ? null : new JSONObject(s);
        } catch (Exception e) { return null; }
    }

    private static String workerText(String w, int n) {
        switch (w) {
            case "b": return n + (n == 1 ? " constructor libre" : " constructores libres");
            case "l": return "laboratorio libre";
            case "p": return "mascotas libre";
            case "bb": return n + (n == 1 ? " constructor nocturno libre" : " constructores nocturnos libres");
            case "bl": return "lab. estelar libre";
        }
        return n + " libre";
    }

    private static int countIn(String text) {
        int n = 0;
        for (String part : text.split(", ")) {
            String d = part.replaceAll("^(\\d+).*$", "$1");
            n += d.matches("\\d+") ? Integer.parseInt(d) : 1;
        }
        return n;
    }

    private static int parseColor(String c) {
        try { return Color.parseColor(c); } catch (Exception e) { return Color.parseColor("#F2B632"); }
    }

    private static String hhmm(long t) {
        Calendar c = Calendar.getInstance(); c.setTimeInMillis(t);
        return String.format(Locale.getDefault(), "%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    private static String dur(long ms) {
        long m = Math.max(0, ms / 60000), d = m / 1440, h = (m % 1440) / 60, mm = m % 60;
        if (d > 0) return d + " d " + h + " h";
        if (h > 0) return h + " h " + mm + " min";
        return mm + " min";
    }
}
