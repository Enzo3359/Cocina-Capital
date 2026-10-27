# Cocina Capital

Aplicación móvil Android que conecta a personas con promociones gastronómicas de comercios locales mediante tickets digitales, validados en el local a través de un código QR.

**Asignatura:** DSY1105 — Desarrollo de Aplicaciones Móviles
**Institución:** Duoc UC — Ingeniería en Informática
**Grupo:** 6
**Evaluación:** Parcial 2 — Encargo (avance intermedio del EFT)

## Integrantes

- Enzo Moraga
- Dylan Elgueta
- Martín Roa

## Descripción del problema

Los comercios gastronómicos pequeños tienen dificultades para atraer clientes en horarios de baja demanda, y dependen de promociones informales difíciles de controlar: cupones impresos que se fotocopian, descuentos de palabra que no dejan registro o publicaciones en redes sociales sin seguimiento.

Cocina Capital resuelve esto con un circuito cerrado de tres actores:

- El **cliente** explora promociones cercanas, adquiere un ticket y recibe un código QR único.
- El **personal del local** escanea ese QR y el sistema valida si el ticket es legítimo y si no fue usado antes.
- El **administrador** gestiona el catálogo de promociones disponibles.

Cada ticket tiene un código irrepetible y un estado que cambia al ser canjeado, lo que impide que se use dos veces.

## Funcionalidades implementadas

### Autenticación y registro
- Registro de usuarios con validación por campo: nombre obligatorio, formato de correo mediante expresión regular, contraseña de mínimo 8 caracteres con al menos una letra y un dígito, confirmación coincidente y aceptación de términos.
- Verificación de correo duplicado contra la base de datos antes de crear la cuenta, reforzada con un índice único a nivel de tabla.
- Inicio de sesión con validación de credenciales y redirección automática según el rol del usuario.
- Tres roles diferenciados: `cliente`, `personal` y `administrador`.

### Módulo cliente
- Listado de promociones con comercio, beneficio, condiciones y período de vigencia.
- Cálculo de distancia en kilómetros entre la ubicación del usuario y cada comercio, con ordenamiento de las promociones por cercanía.
- Adquisición de tickets con generación de un código alfanumérico único de 6 caracteres.
- Control de duplicados: un mismo usuario no puede obtener dos tickets de la misma promoción.
- Pantalla "Mis Tickets" con el código QR renderizado para cada ticket disponible, más el código en texto como alternativa.
- Navegación entre secciones mediante barra inferior.

### Módulo personal del local
- Escaneo de códigos QR con la cámara del dispositivo.
- Validación del ticket contra la base de datos, distinguiendo entre ticket inexistente, ya utilizado, vencido, anulado y válido, cada uno con su mensaje propio.
- Marcado automático del ticket como utilizado al validarlo correctamente, lo que impide un segundo canje.
- Ingreso manual del código como alternativa cuando no hay cámara disponible.

### Módulo administrador
- Creación, edición y eliminación de promociones del catálogo.
- Validación de los campos obligatorios antes de guardar, con retroalimentación visual por campo.
- Persistencia inmediata de los cambios, reflejada en el listado del cliente mediante flujos reactivos.

### Animaciones
- `Crossfade` en el botón de guardado del administrador, que transiciona entre el texto de reposo y el de carga.
- `animateItem()` en el listado de promociones, que anima la inserción, eliminación y reordenamiento de los elementos de la lista.

## Recursos nativos del dispositivo

La aplicación integra dos recursos nativos:

| Recurso | Implementación | Uso en la aplicación |
|---|---|---|
| **Cámara** | ML Kit Code Scanner (`play-services-code-scanner`) | Escaneo de los códigos QR de los tickets en la pantalla de validación del personal |
| **Ubicación GPS** | Fused Location Provider (`play-services-location`) | Obtención de la posición del usuario para calcular y ordenar las promociones por distancia |

El permiso de ubicación se solicita en tiempo de ejecución mediante `rememberLauncherForActivityResult`, aceptando tanto precisión aproximada como precisa. El escáner de ML Kit ejecuta su interfaz dentro de Google Play Services, en un proceso separado, por lo que la aplicación nunca accede directamente al flujo de la cámara y no requiere declarar el permiso `CAMERA` en el manifiesto. Es la implementación recomendada por Google.

## Arquitectura

El proyecto sigue el patrón **MVVM** con separación estricta de responsabilidades:

```
com.proyecto.cocinacapital
├── model/          Entidades Room, DAOs y configuración de la base de datos
│   ├── Usuario.kt, Promocion.kt, Ticket.kt
│   ├── UsuarioDao.kt, PromocionDao.kt, TicketDao.kt
│   └── AppDatabase.kt
├── repository/     Capa de acceso a datos, única puerta de entrada a los DAOs
│   ├── UsuarioRepository.kt
│   ├── PromocionRepository.kt
│   └── TicketRepository.kt
├── viewmodel/      Estado de UI, lógica de validación y reglas de negocio
│   ├── RegistroViewModel.kt, LoginViewModel.kt
│   ├── InicioViewModel.kt, TicketViewModel.kt
│   ├── AdminViewModel.kt, ValidarTicketViewModel.kt
│   ├── SesionViewModel.kt
│   └── RegistroUiState.kt, LoginUiState.kt, AdminUiState.kt
│       RegistroErrores.kt, LoginErrores.kt, AdminErrores.kt
├── view/           Composables de Jetpack Compose
│   ├── screen/     Las seis pantallas de la aplicación
│   └── components/ Componentes reutilizables
├── navigation/     NavHost y definición de rutas
│   ├── AppNavHost.kt
│   └── Rutas.kt
└── util/           Funciones auxiliares sin estado
    ├── QrUtils.kt, CodigoUtils.kt
    ├── UbicacionUtils.kt, DistanciaUtils.kt
```

**Decisiones de diseño relevantes:**

- **La lógica de validación está centralizada en los ViewModels**, no en los composables. Cada formulario tiene una clase de errores (`RegistroErrores`, `LoginErrores`, `AdminErrores`) que viaja dentro del `UiState`. La interfaz solo observa ese estado y lo refleja: no decide nada.
- **El estado se expone como `StateFlow`** y se consume con `collectAsState()`, de modo que la interfaz reacciona automáticamente a cualquier cambio en los datos.
- **Los composables nunca acceden a los DAOs.** El flujo es siempre `View → ViewModel → Repository → DAO`.
- **La sesión activa se maneja en un `SesionViewModel` compartido**, que el resto de los ViewModels consulta para saber qué usuario está operando.

## Persistencia local

Base de datos **Room** con tres entidades relacionadas:

- `usuarios` — con índice único sobre el campo `correo` para impedir cuentas duplicadas a nivel de base de datos.
- `promociones` — catálogo gestionado por el administrador, con las coordenadas geográficas de cada comercio.
- `tickets` — vincula usuario y promoción, guarda el código único, el estado del ticket y la fecha de adquisición.

Los datos persisten entre cierres de la aplicación. Al primer arranque se cargan automáticamente tres promociones de ejemplo y los usuarios de prueba, si no existen.

## Tecnologías

| Componente | Versión / Detalle |
|---|---|
| Lenguaje | Kotlin 2.2.10 |
| Interfaz | Jetpack Compose + Material 3 |
| Arquitectura | MVVM |
| Persistencia | Room con KSP |
| Navegación | Navigation Compose 2.10.1 |
| Generación de QR | ZXing Core 3.5.4 |
| Lectura de QR | ML Kit Code Scanner 16.1.0 |
| Ubicación | Google Play Services Location |
| Android Gradle Plugin | 9.3.3 |
| `minSdk` / `targetSdk` | 31 / 36 |

## Requisitos previos

- Android Studio en una versión compatible con AGP 9.3.3.
- JDK 21 (incluido en Android Studio como `jbr-21`).
- Android SDK Platform 37 instalado desde el SDK Manager.
- Dispositivo físico con **Android 12 o superior** y Google Play Services.

> **Nota sobre el emulador:** el escaneo de QR usa ML Kit Code Scanner, que depende de Google Play Services. En un emulador sin imagen de Play Store esa función no está disponible. Para probar el escaneo se requiere un dispositivo físico.

## Pasos para ejecutar

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/Enzo3359/Cocina-Capital.git
   ```

2. **Abrir el proyecto en Android Studio**

   `File → Open` y seleccionar la carpeta raíz del proyecto. Esperar a que termine la sincronización de Gradle, que en la primera ejecución descarga las dependencias y puede tardar varios minutos.

3. **Instalar el SDK 37** si Android Studio lo solicita

   `Tools → SDK Manager → SDK Platforms → Android API 37 → Apply`

4. **Preparar el dispositivo físico**

   - `Ajustes → Acerca del teléfono → Información de software` y tocar siete veces "Número de compilación".
   - `Ajustes → Opciones de desarrollador → Depuración por USB`.
   - Conectar por cable de datos y seleccionar el modo **Transferencia de archivos (MTP)**.
   - Aceptar el diálogo "¿Permitir depuración USB?".

5. **Ejecutar**

   Seleccionar el dispositivo en el selector y presionar `Run` (Shift + F10).

## Credenciales de prueba

Estos usuarios se crean automáticamente al abrir la pantalla de inicio de sesión por primera vez:

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | `admin@cocinacapital.cl` | `admin123` |
| Personal del local | `personal@cocinacapital.cl` | `personal123` |
| Cliente | *Crear mediante el formulario de registro* | — |

## Cómo probar el flujo completo del QR

La base de datos es local a cada instalación, por lo que un ticket generado en un dispositivo no existe en otro. El ciclo completo debe probarse en **un mismo dispositivo**, mostrando el QR en una pantalla externa.

1. Registrarse como cliente desde el formulario de registro.
2. En la pantalla de inicio, adquirir un ticket de cualquier promoción.
3. Entrar a "Mis Tickets" y ubicar el código QR generado.
4. Capturar el QR y abrirlo en otra pantalla, o generar un QR equivalente con el código de 6 caracteres que aparece bajo la imagen.
5. Cerrar sesión e ingresar con las credenciales del personal.
6. Pulsar "Escanear ticket" y apuntar la cámara al QR. El resultado esperado es **"Ticket válido, aceptado"**.
7. Escanear el mismo QR una segunda vez. El resultado esperado es **"Ticket ya utilizado"**, lo que confirma que el cambio de estado se persistió correctamente.

Como alternativa, la pantalla de validación incluye un campo de texto para ingresar el código manualmente.

## Planificación del trabajo

La distribución de tareas del equipo está documentada en Trello:

https://trello.com/invite/b/6aca9f5c2d6f6c6b4a05eed1/ATTI9f501f7cd3d78c33f04067fc71dc85e099C1B0AB/cocina-capital

## Estado del proyecto

Esta entrega corresponde al avance intermedio del Examen Final Transversal. Las funcionalidades descritas se encuentran implementadas y operativas. Para la entrega final se contempla incorporar las retroalimentaciones recibidas en esta evaluación.
