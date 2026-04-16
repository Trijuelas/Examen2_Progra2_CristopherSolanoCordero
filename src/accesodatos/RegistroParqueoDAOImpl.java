package accesodatos;

import entidades.RegistroParqueo;
import entidades.Vehiculo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RegistroParqueoDAOImpl implements RegistroParqueoDAO {

    private static final String ENCABEZADO = "idRegistro;placa;tipo;fechaEntrada;horaEntrada;estado";

    public RegistroParqueoDAOImpl() throws IOException {
        asegurarArchivoExiste();
    }

    @Override
    public void guardarRegistro(RegistroParqueo registro) throws IOException {
        asegurarArchivoExiste();
        String linea = convertirRegistroALinea(registro) + System.lineSeparator();
        Files.writeString(obtenerRutaArchivo(), linea, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Override
    public List<RegistroParqueo> obtenerRegistrosActivos() throws IOException {
        List<RegistroParqueo> registrosActivos = new ArrayList<>();
        List<RegistroParqueo> todosLosRegistros = obtenerTodosLosRegistros();

        for (RegistroParqueo registro : todosLosRegistros) {
            if ("ACTIVO".equalsIgnoreCase(registro.getEstado())) {
                registrosActivos.add(registro);
            }
        }

        return registrosActivos;
    }

    @Override
    public List<RegistroParqueo> obtenerTodosLosRegistros() throws IOException {
        asegurarArchivoExiste();
        List<String> lineas = Files.readAllLines(obtenerRutaArchivo(), StandardCharsets.UTF_8);
        List<RegistroParqueo> registros = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (!linea.isEmpty()) {
                registros.add(convertirLineaARegistro(linea));
            }
        }

        return registros;
    }

    @Override
    public boolean existePlacaActiva(String placa) throws IOException {
        List<RegistroParqueo> registrosActivos = obtenerRegistrosActivos();

        for (RegistroParqueo registro : registrosActivos) {
            if (registro.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int obtenerSiguienteId() throws IOException {
        List<RegistroParqueo> registros = obtenerTodosLosRegistros();
        int mayorId = 0;

        for (RegistroParqueo registro : registros) {
            if (registro.getIdRegistro() > mayorId) {
                mayorId = registro.getIdRegistro();
            }
        }

        return mayorId + 1;
    }

    private Path obtenerRutaArchivo() {
        return Paths.get(System.getProperty("user.dir"), "data", "registros_parqueo.txt");
    }

    private void asegurarArchivoExiste() throws IOException {
        Path rutaArchivo = obtenerRutaArchivo();
        Path carpeta = rutaArchivo.getParent();

        if (carpeta != null && Files.notExists(carpeta)) {
            Files.createDirectories(carpeta);
        }

        if (Files.notExists(rutaArchivo) || Files.size(rutaArchivo) == 0) {
            Files.writeString(rutaArchivo, ENCABEZADO + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        }
    }

    private String convertirRegistroALinea(RegistroParqueo registro) {
        return registro.getIdRegistro() + ";"
                + registro.getVehiculo().getPlaca() + ";"
                + registro.getVehiculo().getTipo() + ";"
                + registro.getFechaEntrada() + ";"
                + registro.getHoraEntrada() + ";"
                + registro.getEstado();
    }

    private RegistroParqueo convertirLineaARegistro(String linea) throws IOException {
        String[] datos = linea.split(";", -1);

        if (datos.length != 6) {
            throw new IOException("El archivo de registros contiene una linea invalida: " + linea);
        }

        int idRegistro = Integer.parseInt(datos[0]);
        String placa = datos[1];
        String tipo = datos[2];
        String fechaEntrada = datos[3];
        String horaEntrada = datos[4];
        String estado = datos[5];

        Vehiculo vehiculo = new Vehiculo(placa, tipo);
        return new RegistroParqueo(idRegistro, vehiculo, fechaEntrada, horaEntrada, estado);
    }
}
