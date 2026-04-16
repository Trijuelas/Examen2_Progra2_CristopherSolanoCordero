# CHANGELOG

## v1.0

- Se crea la estructura base del proyecto en Java con NetBeans y Apache Ant.
- Se implementa arquitectura por capas con los paquetes `presentacion`, `logica`, `accesodatos` y `entidades`.
- Se agrega la ventana principal en Swing para registrar ingresos al parqueo.
- Se incorpora una tabla para mostrar los vehiculos actualmente dentro del parqueo.
- Se implementa la validacion de placa obligatoria.
- Se implementa la validacion de tipo de vehiculo obligatorio.
- Se aplica la regla de unicidad para impedir ingresos repetidos de una placa activa.
- Se agrega persistencia en `data/registros_parqueo.txt`.
- La hora de entrada se registra automaticamente.

## v1.1

- Se implementa el registro de salida de vehiculos desde la interfaz.
- La salida toma la fecha y hora automaticamente.
- Se calcula el monto a pagar con una tarifa de `₡500` por hora o fraccion.
- Se agregan dos tablas: una para vehiculos activos y otra para historial.
- Los registros salen del estado `ACTIVO` y pasan a `FINALIZADO`.
- Se amplia el archivo `data/registros_parqueo.txt` para guardar salida, minutos y monto pagado.
- La informacion se recarga desde archivos al abrir el sistema.
