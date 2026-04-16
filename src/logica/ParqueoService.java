package logica;

import accesodatos.RegistroParqueoDAO;
import accesodatos.RegistroParqueoDAOImpl;
import entidades.RegistroParqueo;
import entidades.Vehiculo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParqueoService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String TIPO_CARRO = "Carro";
    private static final String TIPO_MOTO = "Moto";
    private final RegistroParqueoDAO registroParqueoDAO;
    private final DateTimeFormatter formatoFecha;
    private final DateTimeFormatter formatoHora;

    public ParqueoService() throws IOException {
        this.registroParqueoDAO = new RegistroParqueoDAOImpl();
        this.formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");
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
                ESTADO_ACTIVO
        );

        registroParqueoDAO.guardarRegistro(registro);
    }

    public List<RegistroParqueo> obtenerRegistrosActivos() throws IOException {
        return registroParqueoDAO.obtenerRegistrosActivos();
    }

    private void validarDatos(String placa, String tipo) {
        if (placa.isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }

        if (placa.contains(";")) {
            throw new IllegalArgumentException("La placa no puede contener el caracter ';'.");
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
}
