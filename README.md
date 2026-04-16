# ExamenParqueo

Proyecto academico en Java con NetBeans + Apache Ant para registrar ingresos y salidas de vehiculos en un parqueo publico.

## Version

`v1.2`

## Caracteristicas

- Interfaz grafica Swing con una ventana principal mas organizada.
- Arquitectura por capas:
  - `presentacion`
  - `logica`
  - `accesodatos`
  - `entidades`
- Registro de ingreso con placa, tipo y hora automatica.
- Registro de salida con seleccion de vehiculo activo.
- Calculo automatico del monto a pagar a `CRC 500` por hora o fraccion.
- Tabla de historial de vehiculos procesados.
- Eliminacion de registros historicos desde la interfaz.
- Validaciones mas estrictas para la placa.
- Persistencia en archivo de texto.
- Resumen visual de registros activos e historicos.

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

El sistema crea y mantiene `data/registros_parqueo.txt` con este encabezado:

```text
idRegistro;placa;tipo;fechaEntrada;horaEntrada;fechaSalida;horaSalida;minutosTotales;montoPagado;estado
```

Ejemplo de lineas:

```text
1;ABC123;Carro;2026-04-15;18:40;;;0;0.0;ACTIVO
2;XYZ789;Moto;2026-04-15;17:00;2026-04-15;18:10;70;1000.0;FINALIZADO
```

## Ejecucion

1. Abrir el proyecto en NetBeans.
2. Verificar que la clase principal sea `presentacion.Main`.
3. Ejecutar el proyecto con Apache Ant.
