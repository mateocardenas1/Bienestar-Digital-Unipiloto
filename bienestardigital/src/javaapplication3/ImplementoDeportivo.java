package javaapplication3;

import java.util.ArrayList;
import java.util.List;

public class ImplementoDeportivo {
    private String id;
    private String nombre;
    private String categoria;
    private int cantidad;
    private String estado;
    private String ubicacion;
    private List<String> prestadoA;

    public ImplementoDeportivo(String id, String nombre, String categoria, int cantidad, String estado, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.prestadoA = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getUbicacion() { return ubicacion; }
    public List<String> getPrestadoA() { return prestadoA; }

    public String getResumenPrestamo() {
        if (prestadoA.isEmpty()) {
            return "Disponible";
        }
        return String.join(", ", prestadoA);
    }
}