# Sistema de Parqueo Publico

## 1. Nombre del proyecto

Sistema de Parqueo Publico

## 2. Descripcion general

Este proyecto corresponde a un sistema academico desarrollado en Java para la gestion basica de un parqueo publico. Permite registrar el ingreso y la salida de vehiculos, calcular el monto a pagar segun el tiempo de permanencia, consultar los vehiculos activos dentro del parqueo y mantener un historial de registros procesados.

La aplicacion fue construida con interfaz grafica Swing, siguiendo una arquitectura por capas y usando archivos de texto como mecanismo de persistencia.

## 3. Objetivo

Desarrollar una aplicacion de escritorio sencilla, clara y funcional que permita aplicar conceptos de programacion orientada a objetos, arquitectura por capas, persistencia en archivos `.txt` e interfaces graficas en Java, dentro de un contexto academico.

## 4. Tecnologias utilizadas

- Java
- NetBeans
- Apache Ant
- Swing
- Archivos `.txt`

## 5. Arquitectura por capas utilizada

El sistema utiliza una arquitectura por capas para mantener una separacion clara de responsabilidades:

- `presentacion`
  Responsable de la interfaz grafica Swing, formularios, tablas, botones y mensajes visuales.
- `logica`
  Responsable de las validaciones, reglas de negocio, calculo de cobro y coordinacion de procesos.
- `accesodatos`
  Responsable de leer, escribir, actualizar y eliminar informacion en archivos `.txt`.
- `entidades`
  Responsable de representar los objetos del sistema, como los registros de parqueo y los vehiculos.

## 6. Funcionalidades principales

- Registrar ingreso de vehiculos.
- Registrar salida de vehiculos.
- Registrar manualmente los minutos de permanencia al momento de la salida.
- Calcular monto a pagar con tarifa de `CRC 500` por hora o fraccion.
- Mostrar tabla de vehiculos activos.
- Mostrar tabla de historial de registros.
- Eliminar registros historicos.
- Validar datos obligatorios antes de procesar operaciones.
- Cargar la informacion almacenada al abrir el sistema.

## 7. Estructura de carpetas

```text
ExamenParqueo/
- src/
- data/
- nbproject/
- build.xml
- manifest.mf
- README.md
- CHANGELOG.md
- prompts.txt
```

## 8. Como ejecutar el proyecto en NetBeans

1. Abrir NetBeans.
2. Seleccionar la opcion para abrir un proyecto existente.
3. Buscar la carpeta del proyecto `ExamenParqueo`.
4. Verificar que la clase principal configurada sea `presentacion.Main`.
5. Ejecutar el proyecto desde NetBeans con Apache Ant.
6. Interactuar con la interfaz grafica principal para registrar ingresos, salidas y consultar historial.

## 9. Reglas de negocio implementadas

- La placa es obligatoria.
- La placa debe tener entre 3 y 10 caracteres.
- La placa no puede contener espacios.
- La placa no puede contener el caracter `;`.
- La placa solo puede contener letras, numeros o guiones.
- El tipo de vehiculo es obligatorio.
- Solo se permiten los tipos `Carro` y `Moto`.
- Un vehiculo no puede registrar un nuevo ingreso si ya tiene un registro activo.
- La hora de entrada se registra automaticamente.
- En la salida, el usuario indica manualmente los minutos de permanencia.
- El cobro se calcula a `CRC 500` por hora o fraccion.
- Si el tiempo es menor a una hora, se cobra una hora minima.
- Los registros con salida procesada pasan a estado `FINALIZADO`.
- Solo se pueden eliminar registros historicos, no registros activos.

## 10. Versiones del sistema

- `v1.0`: registro de ingresos, tabla de activos, persistencia en archivo `.txt` y arquitectura base por capas.
- `v1.1`: registro de salida, calculo de cobro, historial de vehiculos y persistencia ampliada.
- `v1.2`: mejora visual de la interfaz, validaciones mas estrictas y eliminacion de registros historicos.
- `v1.3`: salida con tiempo manual en minutos y calculo de cobro segun el tiempo indicado.
- `v1.4`: mejora visual de la ventana principal con una presentacion mas moderna e interactiva.

## 11. Autor

Christopher Solano Cordero
