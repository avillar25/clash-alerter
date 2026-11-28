# Historial de cambios

<!-- La versión más nueva arriba. Cambios pequeños: 1.2, 1.3…  Cambios gordos: 2.0 -->

## v1.17 — 28/11/2026
- "Qué empezar ahora": para cada trabajador libre, las mejoras que acaban cuando estás despierto (según tu horario de sueño) y, de esas, primero las de ataque (campamentos, cuarteles, laboratorio, héroes, hechizos...).
- Panel de Recursos: oro, elixir y elixir oscuro que necesitas para el plan, para maxear el ayuntamiento y para los muros. Si apuntas lo que tienes, te dice cuánto te falta.
- Actualizar desde la app: el botón descarga la versión nueva y abre directamente el instalador (la primera vez Android pide permiso).

## v1.16 — 27/11/2026
- Seguridad: la app se firma con una clave nueva que ya no está en el repositorio público. **Hay que desinstalar la versión anterior e instalar esta** (una sola vez); los datos vuelven al poner el token de sincronización.
- Seguridad: compilación definitiva en vez de la de pruebas (no se pueden leer los datos de la app conectando el móvil a un ordenador).
- Seguridad: la app ya no entra en las copias de seguridad automáticas de Android.
- Seguridad: aviso si el token de GitHub tiene más permisos de los necesarios.
- Web: política de seguridad que solo le deja conectarse a GitHub.
- El alquimista y el prospector ya no salen en los avisos ni en el widget.
- Al tocar un aviso se abre directamente la cuenta y la aldea de la que habla (por ejemplo, Principal → base del constructor). También en los avisos de la web.

## v1.15 — 27/11/2026
- Widget: ya no muestra la torre del reloj.

## v1.14 — 27/11/2026
- Resumen: el texto "Acaba hoy a las…" de las pestañas se ve nítido (antes salía borroso en la aldea principal).

## v1.13 — 27/11/2026
- Resumen más corto: ya no muestra la lista de mejoras en curso, solo los trabajadores y cuándo acaba la siguiente. Si la quieres ver, marca la casilla "Ver lista" en cada aldea del Resumen.

## v1.12 — 27/11/2026
- Ayudantes (aprendiz, ayudante de laboratorio, alquimista): botón para poner cuánto les falta para estar disponibles ("No está disponible") o marcarlos como listos ("Ya está disponible"), igual que la torre del reloj.

## v1.11 — 27/11/2026
- Al volver a tocar la rueda de ajustes se cierran y vuelves a la pantalla donde estabas.

## v1.10 — 27/11/2026
- Nuevo botón de ajustes (rueda dentada) arriba a la derecha: abre una pantalla con todos los ajustes, los de cada cuenta (importar JSON) y los generales. Ya no hay que bajar hasta el final.
- Web: avisos del navegador mientras la web está abierta (Ajustes → Avisos en este ordenador).

## v1.9 — 27/11/2026
- Botón en cada mejora de la aldea para cambiar quién la hace: "Era el duende" / "Era un constructor".
- Si la app pone una mejora con el constructor duende, el aviso trae el botón "Era un constructor" para corregirlo al momento.
- Torre del reloj: botón para marcarla como "No está disponible" (pones cuánto le falta) o "Ya está disponible".
- Web: icono de la app en la pestaña del navegador.

## v1.8 — 25/11/2026
- Sincronización: si cambias la misma cuenta en el móvil y en el PC, ya no se pierde nada. Se juntan las mejoras empezadas, terminadas y canceladas en los dos lados (antes ganaba entera la última versión guardada y podía borrar una mejora).
- Versión web: en Ajustes → Acerca de se ve la versión y el historial de cambios.

## v1.7 — 25/11/2026
- Sincronización: siempre se leen los datos frescos de GitHub (antes el móvil o el navegador podían leer una copia guardada y no ver los cambios del otro lado).
- Los cambios se suben al momento al salir de la app o cambiar de pestaña, sin esperar.
- Si hubiera más de una copia en GitHub, todos los dispositivos usan la misma. En Ajustes se ve qué copia usa cada uno.

## v1.6 — 24/11/2026
- Sincronización entre el móvil y el PC con un Gist secreto de tu GitHub (Ajustes → Sincronizar).
- Versión web para el PC en avillar25.github.io/clash-alerter, con los mismos datos que el móvil.
- Eventos: ahora también se ven los que están en curso (por ejemplo, los Juegos del clan) y cuándo acaban.

## v1.5 — 24/11/2026
- Resumen sin la cabecera "Clash Alerter · Guardado en este navegador".

## v1.4 — 24/11/2026
- Widgets para la pantalla de inicio: grande (próximas 3 mejoras, trabajadores libres y ayudantes listos) y pequeño (la próxima mejora).
- Botón "Añadir widget" en Ajustes para elegir el tamaño y colocarlo.
- Al tocar el widget se abre la app en la cuenta de esa mejora.

## v1.3 — 24/11/2026
- Icono nuevo: anillo de progreso con un martillo de constructor y un reloj de arena, con contorno cuadrado (adiós al ayuntamiento 15).
- Pantalla de inicio con el nuevo icono.

## v1.2 — 24/11/2026
- Horario de sueño en formato hora (HH:MM): los ":" se ponen solos y ya no deja escribir más de 4 cifras.
- "Horas libre antes de avisar" limitado a 1–24.

## v1.1 — 24/11/2026
Primera versión de la app del móvil.
- Dos cuentas con aldea principal y base nocturna, importando el JSON del juego.
- Mejoras en curso por pestañas (constructores, laboratorio, mascotas) y empezar mejoras al momento con el tiempo del juego.
- No deja empezar mejoras con todos los trabajadores ocupados, salvo que el duende esté libre.
- Pase de oro, pociones, sopa, carne, torre del reloj (duración por nivel y recarga de 22 h) y ayudantes (también hasta que acabe la mejora).
- Ayudantes con su nivel, bloqueados o no, y gemas para desbloquearlos y maxearlos.
- Terminar con gemas o libros, martillos e inventario de objetos mágicos.
- Plan de mejoras con propuesta recomendada, progreso del ayuntamiento, muros, guardianes y nivel máximo de cada mejora.
- Equipamiento de héroes en el orden del juego, con el que no tienes marcado como bloqueado.
- Avisos en el móvil: mejora terminada (y qué toca después según el plan), trabajador libre, ayudantes y torre del reloj listos, y eventos (Liga de guerra, Juegos del clan, asaltos y fin de temporada).
- Aviso dentro de la app cuando hay una versión nueva, con su historial de cambios.
