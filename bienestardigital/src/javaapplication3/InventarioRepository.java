package javaapplication3;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioRepository {
    private static final int MAGIC = 0x4249454E;
    private static final int VERSION = 1;
    private static final int MAX_ITEMS = 100000;
    private final Path archivo;

    public InventarioRepository() {
        this(Paths.get(System.getProperty("user.home"), ".bienestardigital", "inventario.dat"));
    }

    InventarioRepository(Path archivo) {
        this.archivo = archivo;
    }

    public List<ImplementoDeportivo> cargar() throws IOException {
        if (!Files.exists(archivo)) return null;

        try (DataInputStream entrada = new DataInputStream(new BufferedInputStream(Files.newInputStream(archivo)))) {
            if (entrada.readInt() != MAGIC || entrada.readInt() != VERSION) {
                throw new IOException("Formato de inventario no compatible.");
            }
            int cantidadItems = entrada.readInt();
            if (cantidadItems < 0 || cantidadItems > MAX_ITEMS) {
                throw new IOException("Cantidad de elementos no válida.");
            }

            List<ImplementoDeportivo> inventario = new ArrayList<>(cantidadItems);
            for (int i = 0; i < cantidadItems; i++) {
                String id = entrada.readUTF();
                String nombre = entrada.readUTF();
                String categoria = entrada.readUTF();
                int cantidad = entrada.readInt();
                String estado = entrada.readUTF();
                String ubicacion = entrada.readUTF();
                int prestamos = entrada.readInt();

                ImplementoDeportivo implemento = new ImplementoDeportivo(id, nombre, categoria, cantidad, estado, ubicacion);
                for (int j = 0; j < prestamos; j++) {
                    implemento.getPrestadoA().add(entrada.readUTF());
                }
                inventario.add(implemento);
            }
            return inventario;
        }
    }

    public void guardar(List<ImplementoDeportivo> inventario) throws IOException {
        Path directorio = archivo.getParent();
        if (directorio != null) Files.createDirectories(directorio);

        Path temporal = archivo.resolveSibling(archivo.getFileName() + ".tmp");
        try (DataOutputStream salida = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(temporal)))) {
            salida.writeInt(MAGIC);
            salida.writeInt(VERSION);
            salida.writeInt(inventario.size());
            for (ImplementoDeportivo implemento : inventario) {
                salida.writeUTF(implemento.getId());
                salida.writeUTF(implemento.getNombre());
                salida.writeUTF(implemento.getCategoria());
                salida.writeInt(implemento.getCantidad());
                salida.writeUTF(implemento.getEstado());
                salida.writeUTF(implemento.getUbicacion());
                salida.writeInt(implemento.getPrestadoA().size());
                for (String prestamo : implemento.getPrestadoA()) {
                    salida.writeUTF(prestamo);
                }
            }
        }
        try {
            Files.move(temporal, archivo, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}