# Clash Alerter

App de Android para seguir las mejoras de tus cuentas de Clash of Clans y recibir un aviso en el móvil cuando termina cada una.

- Avisos con notificaciones del propio móvil: llegan a la hora exacta, sin internet y sin depender de nada más.
- Aviso si un constructor o laboratorio se queda libre demasiado tiempo.
- Todo lo de la versión web: aldea y base del constructor, catálogo con tiempos y costes, plan, boosts, libros, constructor duende, equipamiento, muros y progreso.

## Cómo conseguir la app (una sola vez)

1. Crea una cuenta en [github.com](https://github.com) si no la tienes.
2. Crea un repositorio nuevo llamado `clash-alerter` (puede ser privado) y sube el contenido de esta carpeta.
   La forma más fácil: en el repositorio vacío, **Add file → Upload files**, arrastra todo el contenido de la carpeta y pulsa **Commit changes**.
3. GitHub compila la app solo. Ve a la pestaña **Actions** y espera a que "Compilar APK" salga en verde (unos 5 minutos).
4. Desde el móvil, abre el repositorio → **Releases** → descarga `clash-alerter.apk` e instálalo.
   Android te pedirá permiso para instalar apps de esa fuente: acéptalo.
5. Abre la app, acepta los avisos y, en **Copia de seguridad → Restaurar**, pega los datos copiados desde la versión web.

## Actualizar

Cada cambio que se sube al repositorio genera una versión nueva en **Releases**. Descárgala e instálala encima: se conservan tus datos.

## Estructura

- `src/template.html`: la app (HTML, CSS y JavaScript en un solo archivo).
- `src/*.json`: datos del juego (tiempos, costes, niveles máximos, equipamiento).
- `www/images.json`: imágenes de edificios, tropas, héroes y objetos.
- `scripts/build-web.py`: genera `www/index.html` a partir de `src/`.
- `android/`: proyecto Android generado con Capacitor.
- `.github/workflows/build-apk.yml`: compila el APK en cada cambio.

Datos e imágenes del juego: Supercell, usados según su Fan Content Policy. Datos extraídos de ClashKing Assets.


## Licencia

© 2026 Alex Villar. **Todos los derechos reservados.** El código está a la vista, pero no se permite copiarlo, redistribuirlo, publicarlo en tiendas ni venderlo sin permiso. Ver [LICENSE](LICENSE).

Contenido de fans no oficial: no está afiliado ni respaldado por Supercell. Más información en la [Política de contenido de fans de Supercell](https://www.supercell.com/fan-content-policy).
