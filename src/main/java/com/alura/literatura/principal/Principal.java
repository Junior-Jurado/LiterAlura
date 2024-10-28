package com.alura.literatura.principal;

import com.alura.literatura.model.*;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private AutorRepository repositorio;

    public Principal(AutorRepository repository) {
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
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresEpocas();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida");

            }
        }
    }

    private Datos obtenerLibrosWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        String nombreLibro = teclado.nextLine().trim().replace(" ", "%20").toLowerCase();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        return (datos != null && !datos.resultados().isEmpty()) ? datos : null;
    }

    private void buscarLibroPorTitulo() {
        var datos = obtenerLibrosWeb();
        if (datos == null) {
            System.out.println("Libro no encontrado");
            return;
        }

        var resultado = datos.resultados().get(0);
        if (!resultado.autores().isEmpty()) {
            var autores = resultado.autores().get(0);
            Optional<Autor> autorExistente = repositorio.findByAutorContainsIgnoreCase(autores.nombre());

            Autor autor;
            if (!autorExistente.isPresent()) {
                var datosAutor = new DatosAutor(autores.nombre(), autores.fechaDeNacimiento(), autores.fechaDeFallecimiento());
                autor = new Autor(datosAutor);
                autor = repositorio.save(autor);
            } else {
                autor = autorExistente.get();
            }

            var datosLibro = new DatosLibro(resultado.titulo(), resultado.autores(), resultado.idiomas(), resultado.numeroDeDescargas());
            Libro nuevoLibro = new Libro(autor.getId(), datosLibro);

            Libro libroExistente = repositorio.verificarExistencia(nuevoLibro.getTitulo());
            if(libroExistente != null) {
                System.out.println("***********************************************************");
                System.out.println("No puedes adicionar un libro que anteriormente fue añadido.");
                return;
            }

            List<Libro> libros = repositorio.obtenerLibrosPorAutor(autor.getId());
            libros.add(nuevoLibro);
            autor.setLibrosEscritos(libros);
            repositorio.save(autor);
            System.out.println("*********************************");
            System.out.println("El libro fue añadido exitosamente");
        } else {
            System.out.println("No se encontró información sobre el autor.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Object[]> libros = repositorio.obtenerLibros();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        mostrarLibros(libros);
    }

    private void listarLibrosPorIdioma() {
        String msg = """
    Ingrese el idioma para buscar los libros
    es - Español
    en - Inglés
    fr - Francés
    pt - Portugués
    """;
        System.out.println(msg);
        String idioma = teclado.nextLine().trim().toLowerCase();

        List<Object[]> libros = repositorio.obtenerLibrosPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("************************************************");
            System.out.printf("Aún no hay libros registrados con idioma en %s%n", idioma);
            return;
        }

        mostrarLibros(libros);
    }

    private void mostrarLibros(List<Object[]> libros) {
        libros.forEach(libro -> {
            Libro libroInfo = (Libro) libro[0];
            System.out.println("\n****************  LIBRO  ****************");
            System.out.printf("Título:\t%s%n", libroInfo.getTitulo());
            System.out.printf("Autor:\t%s%n", libro[1]);
            System.out.printf("Idioma:\t%s%n", libroInfo.getIdioma());
            System.out.printf("Número de descargas:\t%d%n", libroInfo.getNumeroDeDescargas());
            System.out.println("*****************************************\n");
        });
    }


    private void listarAutoresRegistrados() {
        List<Autor> autores = repositorio.obtenerAutores();

        if (autores.isEmpty()) {
            System.out.println("***********************************************");
            System.out.println("No hay ningún autor registrado hasta el momento");
            return;
        }

        autores.forEach(this::imprimirAutor);
    }

    private void listarAutoresEpocas() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar");
        Integer fecha = teclado.nextInt();

        List<Autor> autores = repositorio.obtenerAutoresEpoca(fecha);

        if (autores.isEmpty()) {
            System.out.println("**********************************************************************************");
            System.out.printf("No hay ningún autor registrado que estuviera vivo en el año %d hasta el momento. \n", fecha);
            return;
        }

        autores.forEach(this::imprimirAutor);
    }

    private void imprimirAutor(Autor autor) {
        System.out.println("\n****************  AUTOR  ****************");
        System.out.printf("Autor: \t%s\n", autor.getAutor());
        System.out.printf("Fecha de Nacimiento: \t%d\n", autor.getFechaDeNacimiento());
        System.out.printf("Fecha de Fallecimiento: \t%d\n", autor.getFechaDeFallecimiento());
        System.out.println("Libros: \t" + autor.getLibrosEscritos().stream()
                .map(Libro::getTitulo)
                .collect(Collectors.toList()));
        System.out.println("*****************************************\n");
    }




}
