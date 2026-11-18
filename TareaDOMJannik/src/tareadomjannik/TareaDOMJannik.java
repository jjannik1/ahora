
package tareadomjannik;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TareaDOMJannik {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        int opcion = -1;

        ArrayList<Libro> listaLibros = new ArrayList<>();

        LeerDom(listaLibros);

        do {
            System.out.println("--------MENU----------");
            System.out.println("1....Listado de Libros");
            System.out.println("2....Alta Libros");
            System.out.println("3....Modificar Libros");
            System.out.println("4....Baja Libros");
            System.out.println("0....SALIR");
            System.out.println("Introduce opcion:");

            opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                    Listar(listaLibros);
                    break;

                case 2:
                    Alta(listaLibros, teclado);
                    Grabar(listaLibros);
                    break;

                case 3:
                    Modificar(listaLibros, teclado);
                    Grabar(listaLibros);
                    break;

                case 4:
                    Baja(listaLibros, teclado);
                    Grabar(listaLibros);
                    break;

                case 0:
                    System.out.println("Adios!!!");
                    break;

                default:
                    System.out.println("Opcion incorrecta!!!");
                    break;
            }

        } while (opcion != 0);
    }


    private static void LeerDom(ArrayList<Libro> listaLibros) {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File("biblioteca.xml"));

            NodeList lista = doc.getElementsByTagName("libro");

            for (int i = 0; i < lista.getLength(); i++) {

                Element libro = (Element) lista.item(i);

                String isbn = libro.getAttribute("isbn");

                String titulo = libro.getElementsByTagName("titulo")
                        .item(0).getTextContent();

                // Leer todos los autores
                ArrayList<String> autores = new ArrayList<>();
                ArrayList<String> sexos = new ArrayList<>();

                NodeList listaAutores =
                        libro.getElementsByTagName("autor");

                for (int j = 0; j < listaAutores.getLength(); j++) {

                    Element autorElemento =
                            (Element) listaAutores.item(j);

                    String autor = autorElemento.getTextContent();
                    String sexo = autorElemento.getAttribute("sexo");

                    autores.add(autor);
                    sexos.add(sexo);
                }

                String genero = libro.getElementsByTagName("genero")
                        .item(0).getTextContent();

                Element precioElemento = (Element) libro
                        .getElementsByTagName("precio").item(0);

                double precio = Double.parseDouble(
                        precioElemento.getTextContent());

                String moneda = precioElemento.getAttribute("moneda");

                Libro nuevo = new Libro(isbn, titulo, autores,
                        sexos, genero, precio, moneda);

                listaLibros.add(nuevo);
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TareaDOMJannik.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.out.println("Error en el parseo!!!");

        } catch (SAXException ex) {
            Logger.getLogger(TareaDOMJannik.class.getName())
                    .log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(TareaDOMJannik.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }


    private static void Listar(ArrayList<Libro> listaLibros) {

        if (!listaLibros.isEmpty()) {

            for (Libro libro : listaLibros) {
                System.out.println(libro);
            }

        } else {
            System.out.println("No hay libros!!!");
        }
    }

    private static void Alta(ArrayList<Libro> listaLibros,
            Scanner teclado) {

        String isbn, titulo, genero, moneda;
        double precio;

        ArrayList<String> autores = new ArrayList<>();
        ArrayList<String> sexos = new ArrayList<>();

        teclado.nextLine();

        System.out.println("Dime ISBN del libro");
        isbn = teclado.nextLine();

        if (Busca(isbn, listaLibros) != -1) {
            System.out.println("Libro ya existente...");
        } else {

            System.out.println("Dime titulo");
            titulo = teclado.nextLine();

            System.out.println("Cuantos autores tiene el libro?");
            int cantidad = teclado.nextInt();
            teclado.nextLine();

            for (int i = 0; i < cantidad; i++) {
                System.out.println("Dime nombre del autor " + (i + 1));
                autores.add(teclado.nextLine());

                System.out.println("Dime sexo del autor " + (i + 1));
                sexos.add(teclado.nextLine());
            }

            System.out.println("Dime genero");
            genero = teclado.nextLine();

            System.out.println("Dime precio");
            precio = Double.parseDouble(
                    teclado.next().replace(",", "."));
            teclado.nextLine();

            System.out.println("Dime moneda (euro, dolar, etc.)");
            moneda = teclado.nextLine();

            Libro nuevo = new Libro(isbn, titulo, autores,
                    sexos, genero, precio, moneda);

            listaLibros.add(nuevo);

            System.out.println("Libro añadido!!!");
        }
    }

    private static void Grabar(ArrayList<Libro> listaLibros) {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root = doc.createElement("biblioteca");
            doc.appendChild(root);

            for (Libro l : listaLibros) {

                Element libro = doc.createElement("libro");
                libro.setAttribute("isbn", l.getIsbn());

                Element titulo = doc.createElement("titulo");
                titulo.setTextContent(l.getTitulo());
                libro.appendChild(titulo);

                Element autoresXML = doc.createElement("autores");

                for (int i = 0; i < l.getAutores().size(); i++) {

                    Element autor = doc.createElement("autor");

                    autor.setAttribute("sexo", l.getSexos().get(i));

                    autor.setTextContent(l.getAutores().get(i));

                    autoresXML.appendChild(autor);
                }

                libro.appendChild(autoresXML);

                Element genero = doc.createElement("genero");
                genero.setTextContent(l.getGenero());
                libro.appendChild(genero);

                Element precio = doc.createElement("precio");
                precio.setAttribute("moneda", l.getMoneda());
                precio.setTextContent(String.format(
                        java.util.Locale.US, "%.2f", l.getPrecio()));
                libro.appendChild(precio);

                root.appendChild(libro);
            }

            Transformer transformer = TransformerFactory
                    .newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc),
                    new StreamResult(new File("biblioteca.xml")));

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TareaDOMJannik.class.getName())
                    .log(Level.SEVERE, null, ex);

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(TareaDOMJannik.class.getName())
                    .log(Level.SEVERE, null, ex);

        } catch (TransformerException ex) {
            Logger.getLogger(TareaDOMJannik.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private static void Baja(ArrayList<Libro> listaLibros,
            Scanner teclado) {

        teclado.nextLine();

        System.out.println("Dime ISBN del libro a borrar");
        String isbn = teclado.nextLine();

        int pos = Busca(isbn, listaLibros);

        if (pos == -1) {
            System.out.println("Ese libro no existe!!!");

        } else {
            System.out.println(listaLibros.get(pos));

            System.out.println("Estas seguro (s/n)?");
            char respuesta = teclado.next().toLowerCase().charAt(0);

            if (respuesta == 's') {
                listaLibros.remove(pos);
                System.out.println("Libro borrado correctamente!!!!");
            }
        }
    }

    private static int Busca(String isbn,
            ArrayList<Libro> listaLibros) {

        int pos = -1;

        for (int i = 0; i < listaLibros.size() && pos == -1; i++) {

            if (listaLibros.get(i).getIsbn().equals(isbn)) {
                pos = i;
            }
        }

        return pos;
    }


    private static void Modificar(ArrayList<Libro> listaLibros,
            Scanner teclado) {

        teclado.nextLine();

        System.out.println("Dime ISBN del libro a modificar");
        String isbn = teclado.nextLine();

        int pos = Busca(isbn, listaLibros);

        if (pos == -1) {
            System.out.println("Ese libro no existe!!!");

        } else {
            System.out.println(listaLibros.get(pos));

            System.out.println("Estas seguro (s/n)?");
            char respuesta = teclado.next().toLowerCase().charAt(0);

            if (respuesta == 's') {

                String titulo, genero, moneda;
                double precio;

                ArrayList<String> autores = new ArrayList<>();
                ArrayList<String> sexos = new ArrayList<>();

                teclado.nextLine();

                System.out.println("Dime nuevo titulo");
                titulo = teclado.nextLine();

                System.out.println("Cuantos autores tiene el libro?");
                int cantidad = teclado.nextInt();
                teclado.nextLine();

                for (int i = 0; i < cantidad; i++) {
                    System.out.println("Dime nombre del autor " + (i + 1));
                    autores.add(teclado.nextLine());

                    System.out.println("Dime sexo del autor " + (i + 1));
                    sexos.add(teclado.nextLine());
                }

                System.out.println("Dime nuevo genero");
                genero = teclado.nextLine();

                System.out.println("Dime nuevo precio");
                precio = Double.parseDouble(
                        teclado.next().replace(",", "."));
                teclado.nextLine();

                System.out.println("Dime nueva moneda");
                moneda = teclado.nextLine();

                listaLibros.get(pos).setTitulo(titulo);
                listaLibros.get(pos).setAutores(autores);
                listaLibros.get(pos).setSexos(sexos);
                listaLibros.get(pos).setGenero(genero);
                listaLibros.get(pos).setPrecio(precio);
                listaLibros.get(pos).setMoneda(moneda);

                System.out.println("Libro modificado con exito!!!");
            }
        }
    }

}
