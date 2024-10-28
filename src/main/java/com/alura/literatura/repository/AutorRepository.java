package com.alura.literatura.repository;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByAutorContainsIgnoreCase(String nombre);

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE a.id = :autorId")
    List<Libro> obtenerLibrosPorAutor(Long autorId);


    @Query("SELECT l FROM Libro l WHERE l.titulo ILIKE %:nombreLibro%")
    Libro verificarExistencia(String nombreLibro);

    @Query("SELECT l, a.autor FROM Libro l JOIN l.autor a")
    List<Object[]> obtenerLibros();

    @Query("SELECT l, a.autor FROM Libro l JOIN l.autor a WHERE l.idioma LIKE %:idioma%")
    List<Object[]> obtenerLibrosPorIdioma(String idioma);

    @Query("SELECT a FROM Autor a")
    List<Autor> obtenerAutores();

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :fecha AND a.fechaDeFallecimiento >= :fecha")
    List<Autor> obtenerAutoresEpoca(Integer fecha);


}
