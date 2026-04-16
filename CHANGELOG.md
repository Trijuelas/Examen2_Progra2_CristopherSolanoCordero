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
