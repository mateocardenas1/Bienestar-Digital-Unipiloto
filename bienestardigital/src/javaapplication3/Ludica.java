package javaapplication3;

public class Ludica {
    private String nombre;
    private String descripcion;
    private String horario;
    private String instructor;

    public Ludica(String nombre, String descripcion, String horario, String instructor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horario = horario;
        this.instructor = instructor;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getHorario() { return horario; }
    public String getInstructor() { return instructor; }
}