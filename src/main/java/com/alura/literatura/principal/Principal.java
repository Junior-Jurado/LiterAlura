package com.alura.literatura.principal;

import com.alura.literatura.model.Datos;
import com.alura.literatura.model.DatosLibro;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositorio;
    private List<Libro> libros;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while(opcion != 0) {
            String menu = """
                    **************************************
                    Elija alguna opción del siguiente menú
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - SALIR
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                default:
                    System.out.println("Opción inválida");

            }
        }
    }

    private DatosLibro obtenerLibrosWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        String nombreLibro = teclado.nextLine().replace(" ", "%20");
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.resultados().isEmpty()) {
            return null;
        }
        return new DatosLibro(datos.resultados().get(0).titulo(), datos.resultados().get(0).autores(),
                datos.resultados().get(0).idiomas(), datos.resultados().get(0).numeroDeDescargas());
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datos = obtenerLibrosWeb();
        if (datos == null) {
            System.out.println("Libro no encontrado");
            return;
        }
        Libro libro = new Libro(datos);
        System.out.println(datos);
        repositorio.save(libro);
    }

    private void listarLibrosRegistrados(){
        libros = repositorio.findAll();
        libros.forEach(System.out::println);
    }
}
