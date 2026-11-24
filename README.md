# Clash Alerter

App de Android para seguir las mejoras de tus cuentas de Clash of Clans y recibir un aviso en el móvil cuando termina cada una.

- Avisos con notificaciones del propio móvil: llegan a la hora exacta, sin internet y sin depender de nada más.
- Aviso si un constructor o laboratorio se queda libre demasiado tiempo.
- Todo lo de la versión web: aldea y base del constructor, catálogo con tiempos y costes, plan, boosts, libros, constructor duende, equipamiento, muros y progreso.

## Actualizaciones

Cada versión nueva aparece en **Releases** con su lista de cambios (también en [CHANGELOG.md](CHANGELOG.md)). La app avisa cuando hay una versión nueva y se instala encima conservando los datos.

## Estructura

- `src/template.html`: la app (HTML, CSS y JavaScript en un solo archivo).
- `src/*.json`: datos del juego (tiempos, costes, niveles máximos, equipamiento).
- `www/images.json`: imágenes de edificios, tropas, héroes y objetos.
- `scripts/build-web.py`: genera `www/index.html` a partir de `src/`.
- `android/`: proyecto Android generado con Capacitor.
- `.github/workflows/build-apk.yml`: compila el APK en cada cambio.

Datos e imágenes del juego: Supercell, usados según su Fan Content Policy. Datos extraídos de ClashKing Assets.


## Licencia

© 2026 avillar25. **Todos los derechos reservados.** El código está a la vista, pero no se permite copiarlo, redistribuirlo, publicarlo en tiendas ni venderlo sin permiso. Ver [LICENSE](LICENSE).

Contenido de fans no oficial: no está afiliado ni respaldado por Supercell. Más información en la [Política de contenido de fans de Supercell](https://www.supercell.com/fan-content-policy).
