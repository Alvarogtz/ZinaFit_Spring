package gm.zona_fit.gui;

import gm.zona_fit.ZonaFitApplication;
import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicios.ClienteServicio;
import gm.zona_fit.servicios.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class ZonaFitForma extends JFrame{

    private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);

    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JLabel labelNombre;
    private JTextField textNombre;
    private JLabel labelApellido;
    private JTextField textApellido;
    private JLabel labelMembresia;

    private JTextField textMembresia;
    private JButton buttonGuardar;
    private JButton buttonEliminar;
    private JButton buttonLimpiar;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloClientes;
    private Integer id;

    //Inyecto el servicio en el constructor, ya que lo necesitamos antes de crear el objeto
    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clienteServicio = clienteServicio;
        iniciarForma();

        buttonGuardar.addActionListener(e -> guardarCliente());

        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });

        buttonEliminar.addActionListener(e -> eliminarCliente());

        buttonLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void iniciarForma() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null); // centrar ventana
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.tablaModeloClientes = new DefaultTableModel(0,4){ // Para no permitir editar desde las celdas
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };

        String[] cabecera = {"id","nombre","apellido","membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(cabecera);
        this.clientesTabla = new JTable(tablaModeloClientes);
        // Multiple seleccion deshabilitada
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Cargo los clientes
        listarClientes();
    }

    private void listarClientes() {
        this.tablaModeloClientes.setRowCount(0);
        List<Cliente> clientes = this.clienteServicio.listarClientes();

        clientes.forEach(cliente -> {
            Object[] fila = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.tablaModeloClientes.addRow(fila);
        });
    }

    private void guardarCliente() {
        if(textNombre.getText().equals("")){
            mostrarMensaje("Introduce un nombre");
            textNombre.requestFocusInWindow();
            return;
        }else if(textMembresia.equals("")){
            mostrarMensaje("Introduce una membresia");
            textMembresia.requestFocusInWindow();
            return;
        }else if(!textMembresia.equals("") && !isValidNumberValue()){
            mostrarMensaje("La membresia debe ser un valor numerico");
            textMembresia.requestFocusInWindow();
            return;
        }

        // Creamos el cliente
        Cliente cliente = new Cliente(this.id,textNombre.getText(),textApellido.getText(),Integer.parseInt(textMembresia.getText()));

        if(cliente.getId() != null)
            mostrarMensaje("Cliente actualizado");
        else
            mostrarMensaje("Cliente agregado");

        this.clienteServicio.guardarCliente(cliente);
        limpiarFormulario();
        listarClientes();
    }

    private boolean isValidNumberValue() {
        try{
            Integer.parseInt(textMembresia.getText());
            return true;
        }catch(Exception e){
            logger.error(e.getMessage());
        }

        return false;
    }

    private void cargarClienteSeleccionado() {
        int line = clientesTabla.getSelectedRow();
        if(line != -1){ //-1 significa que no se ha seleccionado nada
            int id = (Integer) clientesTabla.getModel().getValueAt(line,0);
            this.id = id;
            this.textNombre.setText(clientesTabla.getModel().getValueAt(line,1).toString());
            this.textApellido.setText(clientesTabla.getModel().getValueAt(line,2).toString());
            this.textMembresia.setText(clientesTabla.getModel().getValueAt(line,3).toString());
        }
    }

    private void eliminarCliente() {
        int line = clientesTabla.getSelectedRow();
        if(line != -1) { //-1 significa que no se ha seleccionado nada
            Cliente cliente = new Cliente();
            cliente.setId((Integer)clientesTabla.getModel().getValueAt(line, 0));
            this.clienteServicio.eliminarCliente(cliente);
            mostrarMensaje("Cliente " + cliente.getId() + " eliminado.");
            listarClientes();
        }else{
            mostrarMensaje("Debe seleccionar el cliente a eliminar");
        }
    }

    private void limpiarFormulario() {
        textNombre.setText("");
        textApellido.setText("");
        textMembresia.setText("");
        this.id = null;
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String texto) {
        JOptionPane.showMessageDialog(this,texto);
    }
}
