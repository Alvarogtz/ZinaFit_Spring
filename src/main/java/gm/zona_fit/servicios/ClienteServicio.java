package gm.zona_fit.servicios;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.repositorio.IClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteServicio implements IClienteServicio {

    @Autowired
    private IClienteRepositorio clienteRepositorio;

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    @Override
    public Cliente buscarClientePorId(Integer id) {
        return clienteRepositorio.findById(id).orElse(null);
    }

    @Override
    public void guardarCliente(Cliente cliente) {
        clienteRepositorio.save(cliente);
    }

    @Override
    public void eliminarCliente(Cliente cliente) {
        clienteRepositorio.delete(cliente);
    }
}
