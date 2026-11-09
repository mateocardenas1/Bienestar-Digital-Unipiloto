package javaapplication3;

public class EstudianteInteresado {
    private String codigo;
    private String nombreEstudiante;
    private String ludicaSeleccionada;
    private String rutaFoto;

    public EstudianteInteresado(String codigo, String nombreEstudiante, String ludicaSeleccionada, String rutaFoto) {
        this.codigo = codigo;
        this.nombreEstudiante = nombreEstudiante;
        this.ludicaSeleccionada = ludicaSeleccionada;
        this.rutaFoto = rutaFoto;
    }

    public String getCodigo() { return codigo; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getLudicaSeleccionada() { return ludicaSeleccionada; }
    public String getRutaFoto() { return rutaFoto; }
}