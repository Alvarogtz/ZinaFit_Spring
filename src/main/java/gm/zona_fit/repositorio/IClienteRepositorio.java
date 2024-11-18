package gm.zona_fit.repositorio;

import gm.zona_fit.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// El integer es la clave primaria del objeto
public interface IClienteRepositorio extends JpaRepository<Cliente,Integer> {}
