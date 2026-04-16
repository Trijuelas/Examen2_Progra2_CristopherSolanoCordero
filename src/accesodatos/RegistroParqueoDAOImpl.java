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

    private static final String ENCABEZADO = "idRegistro;placa;tipo;fechaEntrada;horaEntrada;fechaSalida;horaSalida;minutosTotales;montoPagado;estado";

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
    public List<RegistroParqueo> obtenerRegistrosHistorial() throws IOException {
        List<RegistroParqueo> historial = new ArrayList<>();
        List<RegistroParqueo> todosLosRegistros = obtenerTodosLosRegistros();

        for (RegistroParqueo registro : todosLosRegistros) {
            if (!"ACTIVO".equalsIgnoreCase(registro.getEstado())) {
                historial.add(registro);
            }
        }

        return historial;
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
    public RegistroParqueo buscarRegistroActivoPorId(int idRegistro) throws IOException {
        List<RegistroParqueo> registrosActivos = obtenerRegistrosActivos();

        for (RegistroParqueo registro : registrosActivos) {
            if (registro.getIdRegistro() == idRegistro) {
                return registro;
            }
        }

        return null;
    }

    @Override
    public void actualizarRegistro(RegistroParqueo registroActualizado) throws IOException {
        List<RegistroParqueo> registros = obtenerTodosLosRegistros();

        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getIdRegistro() == registroActualizado.getIdRegistro()) {
                registros.set(i, registroActualizado);
                guardarTodosLosRegistros(registros);
                return;
            }
        }

        throw new IOException("No se encontro el registro a actualizar.");
    }

    @Override
    public void eliminarRegistroHistorial(int idRegistro) throws IOException {
        List<RegistroParqueo> registros = obtenerTodosLosRegistros();

        for (int i = 0; i < registros.size(); i++) {
            RegistroParqueo registro = registros.get(i);

            if (registro.getIdRegistro() == idRegistro) {
                if ("ACTIVO".equalsIgnoreCase(registro.getEstado())) {
                    throw new IOException("No se puede eliminar un registro activo.");
                }

                registros.remove(i);
                guardarTodosLosRegistros(registros);
                return;
            }
        }

        throw new IOException("No se encontro el registro historico a eliminar.");
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
                + valorTexto(registro.getFechaSalida()) + ";"
                + valorTexto(registro.getHoraSalida()) + ";"
                + registro.getMinutosTotales() + ";"
                + registro.getMontoPagado() + ";"
                + registro.getEstado();
    }

    private RegistroParqueo convertirLineaARegistro(String linea) throws IOException {
        String[] datos = linea.split(";", -1);

        if (datos.length != 6 && datos.length != 10) {
            throw new IOException("El archivo de registros contiene una linea invalida: " + linea);
        }

        int idRegistro = Integer.parseInt(datos[0]);
        String placa = datos[1];
        String tipo = datos[2];
        String fechaEntrada = datos[3];
        String horaEntrada = datos[4];
        String fechaSalida = "";
        String horaSalida = "";
        int minutosTotales = 0;
        double montoPagado = 0;
        String estado;

        if (datos.length == 6) {
            estado = datos[5];
        } else {
            fechaSalida = datos[5];
            horaSalida = datos[6];
            minutosTotales = Integer.parseInt(datos[7]);
            montoPagado = Double.parseDouble(datos[8]);
            estado = datos[9];
        }

        Vehiculo vehiculo = new Vehiculo(placa, tipo);
        return new RegistroParqueo(idRegistro, vehiculo, fechaEntrada, horaEntrada,
                fechaSalida, horaSalida, minutosTotales, montoPagado, estado);
    }

    private void guardarTodosLosRegistros(List<RegistroParqueo> registros) throws IOException {
        StringBuilder contenido = new StringBuilder();
        contenido.append(ENCABEZADO).append(System.lineSeparator());

        for (RegistroParqueo registro : registros) {
            contenido.append(convertirRegistroALinea(registro)).append(System.lineSeparator());
        }

        Files.writeString(obtenerRutaArchivo(), contenido.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String valorTexto(String valor) {
        return valor == null ? "" : valor;
    }
}
