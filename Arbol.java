import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Arbol extends JFrame {
    private JTextField campoTexto;
    private JButton btnInsertar, btnBorrar;
    private PanelArbol panelArbol;
    private Nodo raiz = null;

    public Arbol() {
        super("Árbol Binario");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel panelSuperior = new JPanel(new FlowLayout());
        campoTexto = new JTextField(10);
        btnInsertar = new JButton("Insertar");
        btnBorrar = new JButton("Borrar");

        panelSuperior.add(new JLabel("Número:"));
        panelSuperior.add(campoTexto);
        panelSuperior.add(btnInsertar);
        panelSuperior.add(btnBorrar);

        add(panelSuperior, BorderLayout.NORTH);

        panelArbol = new PanelArbol();
        add(panelArbol, BorderLayout.CENTER);

        btnInsertar.addActionListener(e -> insertar());
        btnBorrar.addActionListener(e -> borrar());

        setVisible(true);
    }

    // ------------------- LÓGICA DEL ÁRBOL ---------------------
    private static class Nodo {
        int dato;
        Nodo izq, der;
        Nodo(int d) { dato = d; }
    }

    private void insertar() {
        try {
            int valor = Integer.parseInt(campoTexto.getText().trim());
            raiz = insertarRec(raiz, valor);
            campoTexto.setText("");
            panelArbol.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido");
        }
    }

    private Nodo insertarRec(Nodo nodo, int valor) {
        if (nodo == null) return new Nodo(valor);
        if (valor < nodo.dato) nodo.izq = insertarRec(nodo.izq, valor);
        else nodo.der = insertarRec(nodo.der, valor);
        return nodo;
    }

    private void borrar() {
        try {
            int valor = Integer.parseInt(campoTexto.getText().trim());
            raiz = borrarRec(raiz, valor);
            campoTexto.setText("");
            panelArbol.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido");
        }
    }

    private Nodo borrarRec(Nodo nodo, int valor) {
        if (nodo == null) return null;

        if (valor < nodo.dato)
            nodo.izq = borrarRec(nodo.izq, valor);
        else if (valor > nodo.dato)
            nodo.der = borrarRec(nodo.der, valor);
        else {
            if (nodo.izq == null) return nodo.der;
            if (nodo.der == null) return nodo.izq;

            Nodo sucesor = min(nodo.der);
            nodo.dato = sucesor.dato;
            nodo.der = borrarRec(nodo.der, sucesor.dato);
        }
        return nodo;
    }

    private Nodo min(Nodo nodo) {
        while (nodo.izq != null) nodo = nodo.izq;
        return nodo;
    }

    // ------------------- PANEL BONITO QUE DIBUJA ---------------------
    private class PanelArbol extends JPanel {
        private final int RADIO = 28;     
        private final int VSEP = 100;    

        public PanelArbol() {
            setBackground(new Color(245, 245, 250)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (raiz == null) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int separacion = Math.max(60, getWidth() / 6);
            dibujarNodo(g2, raiz, getWidth() / 2, 60, separacion);
        }

        private void dibujarNodo(Graphics2D g2, Nodo nodo, int x, int y, int separacion) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(50, 70, 90, 180));
 
            if (nodo.izq != null) {
                int childX = x - separacion;
                int childY = y + VSEP;
                g2.drawLine(x, y + RADIO/2, childX, childY - RADIO/2);
                dibujarNodo(g2, nodo.izq, childX, childY, separacion / 2);
            }
            if (nodo.der != null) {
                int childX = x + separacion;
                int childY = y + VSEP;
                g2.drawLine(x, y + RADIO/2, childX, childY - RADIO/2);
                dibujarNodo(g2, nodo.der, childX, childY, separacion / 2);
            }

            g2.setColor(new Color(0,0,0,40));
            g2.fillOval(x - RADIO + 3, y - RADIO + 6, RADIO * 2, RADIO * 2);

            Color colorNodo = (nodo.dato % 2 == 0) ? new Color(220, 50, 50) : new Color(35, 35, 35);
            g2.setColor(colorNodo);
            g2.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

        
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String texto = String.valueOf(nodo.dato);
            int tx = x - fm.stringWidth(texto) / 2;
            int ty = y + (fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.WHITE);
            g2.drawString(texto, tx, ty);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Arbol::new);
    }
}
