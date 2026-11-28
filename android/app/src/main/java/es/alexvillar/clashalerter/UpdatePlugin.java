package es.alexvillar.clashalerter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Actualizar sin pasar por el navegador: descarga el APK de la versión nueva y abre el instalador de Android.
 * Solo acepta descargas de las versiones publicadas en el repositorio de la app, y Android solo deja instalarla
 * encima si está firmada con la misma clave.
 */
@CapacitorPlugin(name = "ClashUpdate")
public class UpdatePlugin extends Plugin {
    private static final String ALLOWED = "https://github.com/avillar25/clash-alerter/releases/download/";
    private volatile boolean busy = false;

    @PluginMethod
    public void canInstall(PluginCall call) {
        JSObject r = new JSObject();
        r.put("value", canInstallPackages(getContext()));
        call.resolve(r);
    }

    @PluginMethod
    public void openInstallSettings(PluginCall call) {
        Context ctx = getContext();
        try {
            Intent i;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                i = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + ctx.getPackageName()));
            } else {
                i = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            }
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
            call.resolve();
        } catch (Exception e) {
            call.reject("No se pudo abrir el ajuste: " + e.getMessage());
        }
    }

    @PluginMethod
    public void downloadAndInstall(final PluginCall call) {
        final String url = call.getString("url", "");
        if (!url.startsWith(ALLOWED) || !url.endsWith(".apk")) { call.reject("Descarga no permitida"); return; }
        if (busy) { call.reject("Ya se está descargando"); return; }
        busy = true;
        new Thread(() -> {
            HttpURLConnection con = null;
            try {
                File dir = new File(getContext().getCacheDir(), "update");
                if (!dir.exists()) dir.mkdirs();
                File apk = new File(dir, "clash-alerter.apk");
                if (apk.exists()) apk.delete();
                String cur = url;
                // GitHub redirige a otro servidor para la descarga: se siguen las redirecciones (solo https)
                for (int hop = 0; hop < 6; hop++) {
                    con = (HttpURLConnection) new URL(cur).openConnection();
                    con.setInstanceFollowRedirects(false);
                    con.setConnectTimeout(20000);
                    con.setReadTimeout(30000);
                    int code = con.getResponseCode();
                    if (code >= 300 && code < 400) {
                        String loc = con.getHeaderField("Location");
                        con.disconnect(); con = null;
                        if (loc == null || !loc.startsWith("https://")) throw new Exception("Redirección no válida");
                        cur = loc;
                        continue;
                    }
                    if (code != 200) throw new Exception("El servidor respondió " + code);
                    break;
                }
                if (con == null) throw new Exception("Demasiadas redirecciones");
                long total = con.getContentLengthLong();
                try (InputStream in = con.getInputStream(); OutputStream out = new FileOutputStream(apk)) {
                    byte[] buf = new byte[64 * 1024];
                    long done = 0; int last = -1, n;
                    while ((n = in.read(buf)) > 0) {
                        out.write(buf, 0, n); done += n;
                        if (total > 0) {
                            int pct = (int) (done * 100 / total);
                            if (pct != last) { last = pct; JSObject p = new JSObject(); p.put("pct", pct); notifyListeners("progress", p); }
                        }
                    }
                }
                Context ctx = getContext();
                Uri uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", apk);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri, "application/vnd.android.package-archive");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
                call.resolve();
            } catch (Exception e) {
                call.reject(e.getMessage() == null ? "Error al descargar" : e.getMessage());
            } finally {
                if (con != null) con.disconnect();
                busy = false;
            }
        }).start();
    }

    private static boolean canInstallPackages(Context ctx) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true;
        return ctx.getPackageManager().canRequestPackageInstalls();
    }
}
