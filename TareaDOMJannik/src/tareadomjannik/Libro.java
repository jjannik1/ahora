
package tareadomjannik;

import java.util.ArrayList;

public class Libro {

    private String isbn;
    private String titulo;
    private ArrayList<String> autores;
    private ArrayList<String> sexos;
    private String genero;
    private double precio;
    private String moneda;

    public Libro(String isbn, String titulo,
            ArrayList<String> autores, ArrayList<String> sexos,
            String genero, double precio, String moneda) {

        this.isbn = isbn;
        this.titulo = titulo;
        this.autores = autores;
        this.sexos = sexos;
        this.genero = genero;
        this.precio = precio;
        this.moneda = moneda;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public ArrayList<String> getAutores() {
        return autores;
    }

    public ArrayList<String> getSexos() {
        return sexos;
    }

    public String getGenero() {
        return genero;
    }

    public double getPrecio() {
        return precio;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutores(ArrayList<String> autores) {
        this.autores = autores;
    }

    public void setSexos(ArrayList<String> sexos) {
        this.sexos = sexos;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        return "Libro{" 
                + "isbn=" + isbn
                + ", titulo=" + titulo
                + ", autores=" + autores
                + ", sexos=" + sexos
                + ", genero=" + genero
                + ", precio=" + precio
                + ", moneda=" + moneda
                + '}';
    }
}
