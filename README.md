# ExamenParqueo

Proyecto academico en Java con NetBeans + Apache Ant para registrar ingresos de vehiculos en un parqueo publico.

## Version

`v1.0`

## Caracteristicas

- Interfaz grafica Swing con una ventana principal sencilla.
- Arquitectura por capas:
  - `presentacion`
  - `logica`
  - `accesodatos`
  - `entidades`
- Registro de ingreso con placa, tipo y hora automatica.
- Validacion de placa obligatoria.
- Validacion de tipo obligatorio.
- Validacion de unicidad para impedir placas activas repetidas.
- Persistencia en archivo de texto.
- Tabla con vehiculos actualmente dentro del parqueo.

## Estructura

```text
/src
   /accesodatos
   /entidades
   /logica
   /presentacion
/data
   registros_parqueo.txt
/prompts.txt
/CHANGELOG.md
/README.md
```

## Archivo de persistencia

El sistema crea automaticamente `data/registros_parqueo.txt` con este encabezado:

```text
idRegistro;placa;tipo;fechaEntrada;horaEntrada;estado
```

Ejemplo de linea:

```text
1;ABC123;Carro;2026-04-15;18:40;ACTIVO
```

## Ejecucion

1. Abrir el proyecto en NetBeans.
2. Verificar que la clase principal sea `presentacion.Main`.
3. Ejecutar el proyecto con Apache Ant.
