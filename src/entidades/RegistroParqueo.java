package entidades;

public class RegistroParqueo {

    private int idRegistro;
    private Vehiculo vehiculo;
    private String fechaEntrada;
    private String horaEntrada;
    private String estado;

    public RegistroParqueo() {
    }

    public RegistroParqueo(int idRegistro, Vehiculo vehiculo, String fechaEntrada,
            String horaEntrada, String estado) {
        this.idRegistro = idRegistro;
        this.vehiculo = vehiculo;
        this.fechaEntrada = fechaEntrada;
        this.horaEntrada = horaEntrada;
        this.estado = estado;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
