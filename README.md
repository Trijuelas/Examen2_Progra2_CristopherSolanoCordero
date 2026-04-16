# ExamenParqueo

Proyecto academico en Java con NetBeans + Apache Ant para registrar ingresos de vehiculos en un parqueo publico.

## Version

`v1.1`

## Caracteristicas

- Interfaz grafica Swing con una ventana principal sencilla.
- Arquitectura por capas:
  - `presentacion`
  - `logica`
  - `accesodatos`
  - `entidades`
- Registro de ingreso con placa, tipo y hora automatica.
- Registro de salida con seleccion de vehiculo activo.
- Calculo automatico del monto a pagar a `₡500` por hora o fraccion.
- Tabla de historial de vehiculos procesados.
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
idRegistro;placa;tipo;fechaEntrada;horaEntrada;fechaSalida;horaSalida;minutosTotales;montoPagado;estado
```

Ejemplo de linea:

```text
1;ABC123;Carro;2026-04-15;18:40;;;0;0.0;ACTIVO
2;XYZ789;Moto;2026-04-15;17:00;2026-04-15;18:10;70;1000.0;FINALIZADO
```

## Ejecucion

1. Abrir el proyecto en NetBeans.
2. Verificar que la clase principal sea `presentacion.Main`.
3. Ejecutar el proyecto con Apache Ant.
