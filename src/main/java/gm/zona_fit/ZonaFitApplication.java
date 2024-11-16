package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicios.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	@Autowired
	private IClienteServicio clienteServicio;

	private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);
	private Scanner in = new Scanner(System.in);
	private final String[] opciones = {"LISTAR CLIENTES","BUSCAR CLIENTE","AGREGAR CLIENTE","MODIFICAR CLIENTE","ELIMINAR CLIENTE","SALIR"};

	public static void main(String[] args) {
		logger.info("Iniciando la aplicacion");
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("Aplicacion finalizada");
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("*** Aplicacion ZonaFit (GYM) ***");

		boolean salir = false;

		while(!salir) {

			int option = pintarMenu();
			switch (option) {
				case 1:
					listarClientes();
					break;
				case 2:
					buscarCliente();
					break;
				case 3:
					agregarCliente();
					break;
				case 4:
					modificarCliente();
					break;
				case 5:
					eliminarCliente();
					break;
				case 6:
					logger.info("\n ** Vuelve pronto!! =) **");
					salir = true;
					break;
			}
		}
	}

	private void buscarCliente() {
		logger.info("Introduce el id del cliente: ");
		Cliente cliente = clienteServicio.buscarClientePorId(checkEntradaNumero());

		if(cliente != null) {
			logger.info(cliente.toString());
		}else{
			logger.info("El id introducido no existe.");
		}
	}

	private void eliminarCliente() {
		logger.info("Introduce el id del cliente: ");
		Cliente cliente = clienteServicio.buscarClientePorId(checkEntradaNumero());

		if(cliente != null) {
			clienteServicio.eliminarCliente(cliente);
		}else{
			logger.info("El id introducido no existe.");
		}
	}

	private void modificarCliente() {

		logger.info("Introduce el id del cliente: ");
		Cliente cliente = clienteServicio.buscarClientePorId(checkEntradaNumero());

		if(cliente != null) {
			logger.info("Introduce el nombre: ");
			String nombre = in.next();
			logger.info("Introduce el apellido: ");
			String apellido = in.next();
			logger.info("Introduce la membresia: ");
			int membresia = checkEntradaNumero();

			clienteServicio.guardarCliente(cliente);
		}else{
			logger.info("El id introducido no existe.");
		}
	}

	private void agregarCliente() {

		Cliente cliente = new Cliente();

		logger.info("Introduce el nombre: ");
		cliente.setNombre(in.next());
		logger.info("Introduce el apellido: ");
		cliente.setApellido(in.next());
		logger.info("Introduce la membresia: ");
		cliente.setMembresia(checkEntradaNumero());

		clienteServicio.guardarCliente(cliente);
	}

	private int checkEntradaNumero() {
		boolean correcto = false;
		int result = 0;
		while (!correcto) {
			try {
				in = new Scanner(System.in);
				result = in.nextInt();
				correcto = true;
			}catch(Exception e){
				logger.info(e.getMessage());
				logger.info("\nOpcion: ");
			}
		}

		return result;
	}

	private void listarClientes() {
		List<Cliente> clientes = clienteServicio.listarClientes();

		for(Cliente cliente : clientes){
			logger.info(cliente.toString());
		}

		if(clientes.size() == 0)
			logger.info("No existen clientes en la base de datos.");
	}

	private int pintarMenu() {
		int option = 0;
		try{
			while(option <= 0 || option > opciones.length){
				logger.info("\n");
				logger.info(""" 
                *** Zona Fit App ***""" + "\n" +
						"1. " + opciones[0] + "\n" +
						"2. " + opciones[1] + "\n" +
						"3. " + opciones[2] + "\n" +
						"4. " + opciones[3] + "\n" +
						"5. " + opciones[4] + "\n" +
						"6. " + opciones[5] + "\n\n" +
						"Opcion: ");

				option = checkEntradaNumero();

				if(option <= 0 || option > opciones.length)
					logger.info("Opcion incorrecta !!");
			}
		}catch(Exception e){
			logger.info(e.getMessage());
		}

		return option;
	}
}
