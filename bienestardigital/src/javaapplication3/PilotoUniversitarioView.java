package javaapplication3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class PilotoUniversitarioView extends JFrame {
    private List<ImplementoDeportivo> listaInv = new ArrayList<>();
    private final List<Ludica> listaLud = new ArrayList<>();
    private DefaultTableModel modeloInv, modeloLud;
    private JTable tablaInv, tablaLud;
    private JTextField txtBuscar, txtCod, txtNom;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel lblTitulo, lblSub, lblReloj, lblTot, lblDisp, lblPres, lblCat;
    private final Map<String, JButton> botonesMenu = new LinkedHashMap<>();
    private boolean max = false;
    private Rectangle boundsAnt = null;
    private final InventarioRepository repo = new InventarioRepository();

    private final Color BG = new Color(0, 0, 0);
    private final Color PANEL = new Color(30, 30, 30);
    private final Color ALT = new Color(35, 35, 35);
    private final Color INPUT = new Color(42, 42, 42);
    private final Color BORDER = new Color(48, 48, 48);
    private final Color RED = new Color(210, 35, 42);
    private final Color MAIN = new Color(255, 255, 255);
    private final Color MUTED = new Color(160, 160, 160);
    private final Color SUCCESS = new Color(46, 204, 113);

    public PilotoUniversitarioView() {
        cargar();

        listaLud.add(new Ludica("Fútbol Sala", "Entrenamiento táctico y torneos", "Martes y Jueves 4:00 PM", "Prof. Carlos"));
        listaLud.add(new Ludica("Baloncesto", "Acondicionamiento físico", "Lunes y Miércoles 2:00 PM", "Prof. Andrea"));
        listaLud.add(new Ludica("Voleibol", "Fundamentación técnica", "Viernes 3:00 PM", "Prof. Mario"));

        setTitle("Universidad Piloto de Colombia · Bienestar Institucional");
        setUndecorated(true);
        setBackground(Color.BLACK);
        setIconImage(LogoUtils.crearImagenLogo(64));
        setSize(1300, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        main.add(crearBarraTitulo(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(BG);
        cuerpo.add(crearSidebar(), BorderLayout.WEST);

        JPanel derecho = new JPanel(new BorderLayout());
        derecho.setBackground(BG);
        derecho.add(crearHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG);
        cardPanel.setBorder(new EmptyBorder(15, 20, 20, 20));

        cardPanel.add(crearPanelInventario(), "INVENTARIO");
        cardPanel.add(crearPanelLudicas(), "LUDICAS");

        derecho.add(cardPanel, BorderLayout.CENTER);
        cuerpo.add(derecho, BorderLayout.CENTER);
        main.add(cuerpo, BorderLayout.CENTER);
        add(main);

        cardLayout.show(cardPanel, "INVENTARIO");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        max = true;
    }

    private JPanel crearBarraTitulo() {
        JPanel b = new JPanel(new BorderLayout());
        b.setBackground(PANEL);
        b.setPreferredSize(new Dimension(0, 36));
        b.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel t = new JLabel("  UNIVERSIDAD PILOTO DE COLOMBIA  ·  BIENESTAR INSTITUCIONAL");
        t.setForeground(MAIN);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btns.setOpaque(false);
        
        JButton min = crearBotonVentana("-", false);
        JButton maxBtn = crearBotonVentana("O", false);
        JButton close = crearBotonVentana("X", true);

        min.addActionListener(e -> setState(JFrame.ICONIFIED));
        maxBtn.addActionListener(e -> {
            if (!max) {
                boundsAnt = getBounds();
                setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                max = true;
                maxBtn.setText("[]");
            } else {
                setExtendedState(JFrame.NORMAL);
                if (boundsAnt != null) setBounds(boundsAnt);
                max = false;
                maxBtn.setText("O");
            }
        });
        close.addActionListener(e -> System.exit(0));

        btns.add(min); btns.add(maxBtn); btns.add(close);
        b.add(t, BorderLayout.WEST); b.add(btns, BorderLayout.EAST);
        return b;
    }

    private JButton crearBotonVentana(String txt, boolean esCierre) {
        JButton btn = new JButton(txt);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(48, 36));
        btn.setFont(new Font("Arial", Font.BOLD, 12));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(esCierre ? RED : new Color(70, 70, 80));
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(45, 45, 45));
                btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

    private JPanel crearSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(BG);
        sb.setPreferredSize(new Dimension(260, 0));
        sb.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel logo = new JLabel(new ImageIcon(LogoUtils.crearImagenLogo(90)));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sb.add(logo);
        sb.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel t1 = new JLabel("BIENESTAR INSTITUCIONAL");
        t1.setForeground(MAIN); t1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t1.setAlignmentX(Component.CENTER_ALIGNMENT);
        sb.add(t1);

        JLabel t2 = new JLabel("UNIVERSIDAD PILOTO DE COLOMBIA");
        t2.setForeground(RED); t2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t2.setAlignmentX(Component.CENTER_ALIGNMENT);
        sb.add(t2);

        sb.add(Box.createRigidArea(new Dimension(0, 16)));
        JPanel div = new JPanel(); div.setBackground(RED);
        div.setMaximumSize(new Dimension(45, 3)); div.setPreferredSize(new Dimension(45, 3));
        div.setAlignmentX(Component.CENTER_ALIGNMENT);
        sb.add(div);

        sb.add(Box.createRigidArea(new Dimension(0, 24)));
        sb.add(crearBotonMenu("Stock y Préstamos", "INVENTARIO", true));
        sb.add(Box.createRigidArea(new Dimension(0, 10)));
        sb.add(crearBotonMenu("Agenda Lúdicas", "LUDICAS", false));
        sb.add(Box.createVerticalGlue());

        JLabel f1 = new JLabel("Seccional Alto Magdalena");
        f1.setForeground(MUTED); f1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        f1.setAlignmentX(Component.CENTER_ALIGNMENT);
        sb.add(f1);
        return sb;
    }

    private JButton crearBotonMenu(String txt, String card, boolean activo) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                super.paintComponent(g2); g2.dispose();
            }
        };
        b.setMaximumSize(new Dimension(230, 40));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFocusPainted(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(0, 16, 0, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        estiloMenu(b, activo);
        b.addActionListener(e -> seleccionarMenu(card));
        botonesMenu.put(card, b);
        return b;
    }

    private void estiloMenu(JButton b, boolean activo) {
        b.setBackground(activo ? RED : BG);
        b.setForeground(activo ? MAIN : MUTED);
    }

    private void seleccionarMenu(String card) {
        botonesMenu.forEach((k, v) -> estiloMenu(v, k.equals(card)));
        cardLayout.show(cardPanel, card);
        lblTitulo.setText(card.equals("LUDICAS") ? "Agenda de Lúdicas" : "Stock y Préstamos Deportivos");
        lblSub.setText(card.equals("LUDICAS") ? "Actividades e instructores" : "Control de inventario e insumos");
    }

    private JPanel crearHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG);
        h.setBorder(new EmptyBorder(12, 20, 12, 20));

        JPanel l = new JPanel(); l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS)); l.setOpaque(false);
        lblTitulo = new JLabel("Stock y Préstamos Deportivos");
        lblTitulo.setForeground(MAIN); lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblSub = new JLabel("Control de implementos e inventario de Bienestar Institucional");
        lblSub.setForeground(MUTED); lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.add(lblTitulo); l.add(lblSub);

        lblReloj = new JLabel(" ");
        lblReloj.setForeground(MAIN); lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        new javax.swing.Timer(1000, e -> lblReloj.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy, HH:mm:ss", new Locale("es", "ES"))))).start();

        h.add(l, BorderLayout.WEST); h.add(lblReloj, BorderLayout.EAST);
        return h;
    }

    private JPanel crearCardStat(String t, JLabel v, Color c) {
        JPanel p = new JPanel(new BorderLayout(0, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(BORDER); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setBorder(new EmptyBorder(10, 14, 10, 14));
        JLabel title = new JLabel(t.toUpperCase()); title.setForeground(MUTED); title.setFont(new Font("Segoe UI", Font.BOLD, 10));
        v.setForeground(c); v.setFont(new Font("Segoe UI", Font.BOLD, 20));
        p.add(title, BorderLayout.NORTH); p.add(v, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelInventario() {
        JPanel p = new JPanel(new BorderLayout(0, 12)); p.setBackground(BG);

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0)); stats.setBackground(BG);
        stats.add(crearCardStat("Tipos", lblTot = new JLabel("0"), MAIN));
        stats.add(crearCardStat("Disponibles", lblDisp = new JLabel("0"), SUCCESS));
        stats.add(crearCardStat("Préstamos", lblPres = new JLabel("0"), RED));
        stats.add(crearCardStat("Categorías", lblCat = new JLabel("0"), MAIN));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        form.setOpaque(false); form.setBorder(new EmptyBorder(10, 14, 10, 14));
        form.add(label("Cód. Carné (9 dígitos):")); form.add(txtCod = input(11));
        form.add(label("Estudiante:")); form.add(txtNom = input(15));
        JButton btnPrest = boton("Registrar Préstamo", RED);
        form.add(btnPrest);

        JPanel top = new JPanel(new BorderLayout(0, 10)); top.setBackground(BG);
        top.add(stats, BorderLayout.NORTH); top.add(form, BorderLayout.SOUTH);
        p.add(top, BorderLayout.NORTH);

        modeloInv = modelo(new String[]{"ID", "Implemento", "Categoría", "Stock", "Estado", "Ubicación", "Asignado A"});
        tablaInv = tabla(modeloInv);

        JPanel barra = new JPanel(new BorderLayout(12, 0)); barra.setBackground(BG);
        txtBuscar = input(14);
        JPanel bBox = new JPanel(new BorderLayout(8, 0)); bBox.setOpaque(false);
        bBox.add(label("BUSCAR:"), BorderLayout.WEST); bBox.add(txtBuscar, BorderLayout.CENTER);
        barra.add(bBox, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); btns.setOpaque(false);
        JButton btnNew = boton("+ Nuevo", new Color(39, 174, 96));
        JButton btnEdit = boton("Editar", new Color(41, 128, 185));
        JButton btnDev = boton("Devolución", new Color(230, 126, 34));
        JButton btnBaja = boton("Dar de baja", new Color(192, 57, 43));
        btns.add(btnNew); btns.add(btnEdit); btns.add(btnDev); btns.add(btnBaja);
        barra.add(btns, BorderLayout.EAST);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloInv);
        tablaInv.setRowSorter(sorter);
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            private void filtrar() {
                String t = txtBuscar.getText().trim();
                sorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(t)));
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaInv);
        scrollTabla.setBackground(BG);
        scrollTabla.getViewport().setBackground(BG);
        scrollTabla.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JPanel tableCont = new JPanel(new BorderLayout(0, 6)); tableCont.setBackground(BG);
        tableCont.add(barra, BorderLayout.NORTH); tableCont.add(scrollTabla, BorderLayout.CENTER);
        p.add(tableCont, BorderLayout.CENTER);

        actualizarTablaInv();

        btnNew.addActionListener(e -> formImp(null));
        btnEdit.addActionListener(e -> { ImplementoDeportivo i = getSel(); if (i != null) formImp(i); });
        btnBaja.addActionListener(e -> bajaSel());
        btnDev.addActionListener(e -> devSel());
        btnPrest.addActionListener(e -> {
            ImplementoDeportivo i = getSel();
            if (i == null || txtCod.getText().trim().isEmpty() || txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un objeto y complete todos los campos."); return;
            }
            
            String codigoCarné = txtCod.getText().trim();
            if (!codigoCarné.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "El código de carné debe tener exactamente 9 dígitos numéricos.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (i.getCantidad() <= 0) { JOptionPane.showMessageDialog(this, "Sin stock disponible."); return; }
            i.setCantidad(i.getCantidad() - 1);
            i.getPrestadoA().add(txtNom.getText().trim() + " (" + codigoCarné + ")");
            actualizarTablaInv(); guardar();
            txtCod.setText(""); txtNom.setText("");
            JOptionPane.showMessageDialog(this, "Préstamo registrado exitosamente.");
        });

        return p;
    }

    private JPanel crearPanelLudicas() {
        JPanel p = new JPanel(new BorderLayout(12, 12)); p.setBackground(BG);
        modeloLud = modelo(new String[]{"Lúdica", "Descripción", "Horario", "Instructor"});
        tablaLud = tabla(modeloLud);
        for (Ludica l : listaLud) modeloLud.addRow(new Object[]{l.getNombre(), l.getDescripcion(), l.getHorario(), l.getInstructor()});
        
        JScrollPane scrollLudicas = new JScrollPane(tablaLud);
        scrollLudicas.setBackground(BG);
        scrollLudicas.getViewport().setBackground(BG);
        scrollLudicas.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        p.add(scrollLudicas, BorderLayout.CENTER);
        return p;
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s); l.setForeground(MUTED); l.setFont(new Font("Segoe UI", Font.BOLD, 11)); return l;
    }

    private JTextField input(int cols) {
        JTextField t = new JTextField(cols) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                super.paintComponent(g2); g2.dispose();
            }
        };
        t.setOpaque(false); t.setForeground(MAIN); t.setCaretColor(RED);
        t.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        return t;
    }

    private JButton boton(String txt, Color bg) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                super.paintComponent(g2); g2.dispose();
            }
        };
        b.setBackground(bg); b.setForeground(MAIN); b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setFocusPainted(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private DefaultTableModel modelo(String[] cols) {
        return new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    }

    private JTable tabla(DefaultTableModel m) {
        JTable t = new JTable(m); 
        t.setBackground(PANEL); 
        t.setForeground(MAIN);
        t.setGridColor(BORDER); 
        t.setRowHeight(34);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setBackground(BG); 
        t.getTableHeader().setForeground(MAIN);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean f, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, v, sel, f, row, col);
                setBackground(sel ? INPUT : (row % 2 == 0 ? PANEL : ALT));
                setForeground(MAIN); 
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(r);
        return t;
    }

    private void actualizarTablaInv() {
        modeloInv.setRowCount(0);
        int total = 0, prestados = 0; Set<String> cats = new HashSet<>();
        for (ImplementoDeportivo i : listaInv) {
            modeloInv.addRow(new Object[]{i.getId(), i.getNombre(), i.getCategoria(), i.getCantidad(), i.getEstado(), i.getUbicacion(), i.getResumenPrestamo()});
            if ("Disponible".equalsIgnoreCase(i.getEstado())) total += i.getCantidad();
            prestados += i.getPrestadoA().size(); 
            cats.add(i.getCategoria());
        }
        lblTot.setText(String.valueOf(listaInv.size()));
        lblDisp.setText(String.valueOf(total));
        lblPres.setText(String.valueOf(prestados));
        lblCat.setText(String.valueOf(cats.size()));
    }

    private ImplementoDeportivo getSel() {
        int r = tablaInv.getSelectedRow();
        return r < 0 ? null : listaInv.get(tablaInv.convertRowIndexToModel(r));
    }

    private void formImp(ImplementoDeportivo ext) {
        JTextField id = input(10), nom = input(12), cat = input(10), cant = input(6), ubi = input(10);
        if (ext != null) { id.setText(ext.getId()); id.setEditable(false); nom.setText(ext.getNombre()); cat.setText(ext.getCategoria()); cant.setText(String.valueOf(ext.getCantidad())); ubi.setText(ext.getUbicacion()); }
        JPanel f = new JPanel(new GridLayout(0, 2, 8, 8)); f.setBackground(PANEL); f.setBorder(new EmptyBorder(8, 8, 8, 8));
        f.add(label("ID:")); f.add(id); f.add(label("Nombre:")); f.add(nom); f.add(label("Categoría:")); f.add(cat);
        f.add(label("Cantidad:")); f.add(cant); f.add(label("Ubicación:")); f.add(ubi);
        if (JOptionPane.showConfirmDialog(this, f, ext == null ? "Nuevo Item" : "Editar Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int c = Integer.parseInt(cant.getText().trim());
                String estado = c > 0 ? "Disponible" : "Agotado";
                if (ext != null) estado = ext.getEstado(); 
                ImplementoDeportivo item = new ImplementoDeportivo(id.getText().trim(), nom.getText().trim(), cat.getText().trim(), c, estado, ubi.getText().trim());
                if (ext == null) listaInv.add(item);
                else { item.getPrestadoA().addAll(ext.getPrestadoA()); listaInv.set(listaInv.indexOf(ext), item); }
                actualizarTablaInv(); guardar();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Datos no válidos."); }
        }
    }

    private void bajaSel() {
        ImplementoDeportivo i = getSel();
        if (i == null) { JOptionPane.showMessageDialog(this, "Seleccione un implemento."); return; }
        if (!i.getPrestadoA().isEmpty()) { JOptionPane.showMessageDialog(this, "Tiene préstamos activos, no se puede dar de baja."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Cambiar estado de \"" + i.getNombre() + "\" a Dado de baja?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            i.setEstado("Dado de baja");
            i.setCantidad(0);
            actualizarTablaInv(); guardar();
        }
    }

    private void devSel() {
        ImplementoDeportivo i = getSel();
        if (i == null || i.getPrestadoA().isEmpty()) { JOptionPane.showMessageDialog(this, "Sin préstamos pendientes."); return; }
        String p = (String) JOptionPane.showInputDialog(this, "Cerrar préstamo:", "Devolución", JOptionPane.PLAIN_MESSAGE, null, i.getPrestadoA().toArray(), i.getPrestadoA().get(0));
        if (p != null && i.getPrestadoA().remove(p)) { 
            i.setCantidad(i.getCantidad() + 1); 
            if("Agotado".equals(i.getEstado())) i.setEstado("Disponible");
            actualizarTablaInv(); guardar(); 
        }
    }

    private void cargar() {
        try {
            List<ImplementoDeportivo> g = repo.cargar();
            if (g != null) listaInv = g;
        } catch (Exception ignored) {}

        asegurarItemObligatorio("IMP-101", "Balón de Fútbol Profesional", "Fútbol", 4, "Disponible", "Casillero A-01");
        asegurarItemObligatorio("IMP-102", "Balón de Baloncesto", "Baloncesto", 3, "Disponible", "Casillero A-02");
        asegurarItemObligatorio("IMP-103", "Balón de Voleibol", "Voleibol", 5, "Disponible", "Casillero B-01");
        asegurarItemObligatorio("IMP-104", "Lazos para Saltar la Cuerda", "Acondicionamiento", 4, "Disponible", "Casillero C-05");
        guardar();
    }

    private void asegurarItemObligatorio(String id, String nombre, String categoria, int cantidad, String estado, String ubicacion) {
        boolean existe = false;
        for (ImplementoDeportivo i : listaInv) {
            if (i.getId().equalsIgnoreCase(id)) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            listaInv.add(new ImplementoDeportivo(id, nombre, categoria, cantidad, estado, ubicacion));
        }
    }

    private void guardar() { try { repo.guardar(listaInv); } catch (Exception ignored) {} }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new PilotoUniversitarioView().setVisible(true)); }
}