package gm.zona_fit.servicios;

import gm.zona_fit.modelo.Cliente;

import java.util.List;

public interface IClienteServicio {
    public List<Cliente> listarClientes();
    public Cliente buscarClientePorId(Integer id);
    public void guardarCliente(Cliente cliente);
    public void eliminarCliente(Cliente cliente);
}
