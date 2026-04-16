package logica;

import accesodatos.RegistroParqueoDAO;
import accesodatos.RegistroParqueoDAOImpl;
import entidades.RegistroParqueo;
import entidades.Vehiculo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParqueoService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_FINALIZADO = "FINALIZADO";
    private static final String TIPO_CARRO = "Carro";
    private static final String TIPO_MOTO = "Moto";
    private static final int TARIFA_POR_HORA = 500;
    private static final int LONGITUD_MINIMA_PLACA = 3;
    private static final int LONGITUD_MAXIMA_PLACA = 10;
    private final RegistroParqueoDAO registroParqueoDAO;
    private final DateTimeFormatter formatoFecha;
    private final DateTimeFormatter formatoHora;
    private final DateTimeFormatter formatoFechaHora;

    public ParqueoService() throws IOException {
        this.registroParqueoDAO = new RegistroParqueoDAOImpl();
        this.formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        this.formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public void registrarIngreso(String placa, String tipo) throws IOException {
        String placaNormalizada = normalizarPlaca(placa);
        String tipoNormalizado = normalizarTipo(tipo);

        validarDatos(placaNormalizada, tipoNormalizado);

        if (registroParqueoDAO.existePlacaActiva(placaNormalizada)) {
            throw new IllegalArgumentException("La placa ya tiene un ingreso activo.");
        }

        Vehiculo vehiculo = new Vehiculo(placaNormalizada, tipoNormalizado);
        RegistroParqueo registro = new RegistroParqueo(
                registroParqueoDAO.obtenerSiguienteId(),
                vehiculo,
                LocalDate.now().format(formatoFecha),
                LocalTime.now().format(formatoHora),
                "",
                "",
                0,
                0,
                ESTADO_ACTIVO
        );

        registroParqueoDAO.guardarRegistro(registro);
    }

    public List<RegistroParqueo> obtenerRegistrosActivos() throws IOException {
        List<RegistroParqueo> registrosActivos = new ArrayList<>(registroParqueoDAO.obtenerRegistrosActivos());
        registrosActivos.sort(Comparator.comparingInt(RegistroParqueo::getIdRegistro));
        return registrosActivos;
    }

    public List<RegistroParqueo> obtenerRegistrosHistorial() throws IOException {
        List<RegistroParqueo> historial = new ArrayList<>(registroParqueoDAO.obtenerRegistrosHistorial());
        historial.sort(Comparator.comparingInt(RegistroParqueo::getIdRegistro).reversed());
        return historial;
    }

    public RegistroParqueo registrarSalida(int idRegistro) throws IOException {
        if (idRegistro <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un vehiculo activo.");
        }

        RegistroParqueo registro = registroParqueoDAO.buscarRegistroActivoPorId(idRegistro);

        if (registro == null) {
            throw new IllegalArgumentException("El vehiculo seleccionado ya no se encuentra activo.");
        }

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        registro.setFechaSalida(fechaActual.format(formatoFecha));
        registro.setHoraSalida(horaActual.format(formatoHora));
        registro.setMinutosTotales(calcularMinutosTotales(registro));
        registro.setMontoPagado(calcularMonto(registro.getMinutosTotales()));
        registro.setEstado(ESTADO_FINALIZADO);

        registroParqueoDAO.actualizarRegistro(registro);
        return registro;
    }

    public void eliminarRegistroHistorial(int idRegistro) throws IOException {
        if (idRegistro <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un registro del historial.");
        }

        registroParqueoDAO.eliminarRegistroHistorial(idRegistro);
    }

    private void validarDatos(String placa, String tipo) {
        if (placa.isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }

        if (placa.length() < LONGITUD_MINIMA_PLACA || placa.length() > LONGITUD_MAXIMA_PLACA) {
            throw new IllegalArgumentException("La placa debe tener entre 3 y 10 caracteres.");
        }

        if (placa.contains(";")) {
            throw new IllegalArgumentException("La placa no puede contener el caracter ';'.");
        }

        if (placa.contains(" ")) {
            throw new IllegalArgumentException("La placa no puede contener espacios.");
        }

        if (!placa.matches("[A-Z0-9-]+")) {
            throw new IllegalArgumentException("La placa solo puede contener letras, numeros o guiones.");
        }

        if (tipo.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de vehiculo.");
        }

        if (!TIPO_CARRO.equals(tipo) && !TIPO_MOTO.equals(tipo)) {
            throw new IllegalArgumentException("El tipo de vehiculo seleccionado no es valido.");
        }
    }

    private String normalizarPlaca(String placa) {
        if (placa == null) {
            return "";
        }

        return placa.trim().toUpperCase();
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) {
            return "";
        }

        String tipoNormalizado = tipo.trim().toLowerCase();

        if ("carro".equals(tipoNormalizado)) {
            return TIPO_CARRO;
        }

        if ("moto".equals(tipoNormalizado)) {
            return TIPO_MOTO;
        }

        return tipo.trim();
    }

    private int calcularMinutosTotales(RegistroParqueo registro) {
        try {
            LocalDateTime fechaHoraEntrada = LocalDateTime.parse(
                    registro.getFechaEntrada() + " " + registro.getHoraEntrada(), formatoFechaHora);
            LocalDateTime fechaHoraSalida = LocalDateTime.parse(
                    registro.getFechaSalida() + " " + registro.getHoraSalida(), formatoFechaHora);

            long minutos = Duration.between(fechaHoraEntrada, fechaHoraSalida).toMinutes();
            if (minutos <= 0) {
                return 1;
            }

            return (int) minutos;
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("No fue posible calcular el tiempo del registro.");
        }
    }

    private double calcularMonto(int minutosTotales) {
        int horasCobradas = minutosTotales / 60;

        if (minutosTotales % 60 != 0) {
            horasCobradas++;
        }

        if (horasCobradas == 0) {
            horasCobradas = 1;
        }

        return horasCobradas * TARIFA_POR_HORA;
    }
}
