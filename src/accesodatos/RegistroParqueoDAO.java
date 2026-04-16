package accesodatos;

import entidades.RegistroParqueo;
import java.io.IOException;
import java.util.List;

public interface RegistroParqueoDAO {

    void guardarRegistro(RegistroParqueo registro) throws IOException;

    List<RegistroParqueo> obtenerRegistrosActivos() throws IOException;

    List<RegistroParqueo> obtenerRegistrosHistorial() throws IOException;

    List<RegistroParqueo> obtenerTodosLosRegistros() throws IOException;

    boolean existePlacaActiva(String placa) throws IOException;

    RegistroParqueo buscarRegistroActivoPorId(int idRegistro) throws IOException;

    void actualizarRegistro(RegistroParqueo registroActualizado) throws IOException;

    int obtenerSiguienteId() throws IOException;
}
