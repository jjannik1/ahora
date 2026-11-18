
package pruebapokemon;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class PruebaPokemon {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        ArrayList<Pokemon> listaPokemon = new ArrayList<>();

        int opcion = -1;

        LeerDom(listaPokemon);

        do {
            System.out.println("\nMENU POKEMON");
            System.out.println("1. Listar Pokemon");
            System.out.println("2. Crear Pokemon");
            System.out.println("3. Modificar Pokemon");
            System.out.println("4. Eliminar Pokemon");
            System.out.println("5. Crear XML de un tipo");
            System.out.println("0. Salir");

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {

                case 1:
                    LeerDatos(listaPokemon);
                    break;

                case 2:
                    CrearPokemon(listaPokemon, teclado);
                    GuardarDom(listaPokemon);
                    break;

                case 3:
                    ModificarPokemon(listaPokemon, teclado);
                    GuardarDom(listaPokemon);
                    break;

                case 4:
                    EliminarPokemon(listaPokemon, teclado);
                    GuardarDom(listaPokemon);
                    break;

                case 5:
                    CrearXMLTipo(listaPokemon, teclado);
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opcion incorrecta.");
            }

        } while (opcion != 0);

        teclado.close();
    }

    // LEER XML Y CARGAR ARRAYLIST
    private static void LeerDom(ArrayList<Pokemon> listaPokemon) {

        try {

            File archivo = new File("pokemon.xml");

            if (!archivo.exists()) {
                return;
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento = builder.parse(archivo);

            NodeList lista = documento.getElementsByTagName("pokemon");

            for (int i = 0; i < lista.getLength(); i++) {

                Element elemento = (Element) lista.item(i);

                int id = Integer.parseInt(elemento.getAttribute("id"));

                String nombre = elemento.getElementsByTagName("nombre")
                        .item(0).getTextContent();

                String tipo = elemento.getElementsByTagName("tipo")
                        .item(0).getTextContent();

                Pokemon p = new Pokemon(id, nombre, tipo);

                listaPokemon.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error al leer el XML.");
        }
    }

    // GUARDAR ARRAYLIST EN XML
    private static void GuardarDom(ArrayList<Pokemon> listaPokemon) {

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento = builder.newDocument();

            Element raiz = documento.createElement("pokemons");

            documento.appendChild(raiz);

            for (Pokemon p : listaPokemon) {

                Element pokemon = documento.createElement("pokemon");

                pokemon.setAttribute("id", String.valueOf(p.getId()));

                Element nombre = documento.createElement("nombre");
                nombre.setTextContent(p.getNombre());

                Element tipo = documento.createElement("tipo");
                tipo.setTextContent(p.getTipo());

                pokemon.appendChild(nombre);
                pokemon.appendChild(tipo);

                raiz.appendChild(pokemon);
            }

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(documento);

            StreamResult result = new StreamResult(new File("pokemon.xml"));

            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Error al guardar el XML.");
        }
    }

    // LISTAR POKEMON
    private static void LeerDatos(ArrayList<Pokemon> listaPokemon) {

        for (Pokemon p : listaPokemon) {
            System.out.println(p);
        }

        if (listaPokemon.isEmpty()) {
            System.out.println("No hay Pokemon.");
        }
    }

    // CREAR POKEMON
    private static void CrearPokemon(ArrayList<Pokemon> listaPokemon,
                                     Scanner teclado) {

        System.out.println("Introduce el ID:");
        int id = teclado.nextInt();
        teclado.nextLine();

        boolean existe = false;

        for (Pokemon p : listaPokemon) {
            if (p.getId() == id) {
                existe = true;
            }
        }

        if (existe) {
            System.out.println("Ese ID ya existe.");
        } else {

            System.out.println("Introduce el nombre:");
            String nombre = teclado.nextLine();

            System.out.println("Introduce el tipo:");
            String tipo = teclado.nextLine();

            Pokemon nuevo = new Pokemon(id, nombre, tipo);

            listaPokemon.add(nuevo);

            System.out.println("Pokemon creado.");
        }
    }

    // MODIFICAR POKEMON
    private static void ModificarPokemon(ArrayList<Pokemon> listaPokemon,
                                         Scanner teclado) {

        System.out.println("Introduce el ID del Pokemon:");
        int id = teclado.nextInt();
        teclado.nextLine();

        boolean encontrado = false;

        for (Pokemon p : listaPokemon) {

            if (p.getId() == id) {

                System.out.println("Introduce el nuevo nombre:");
                String nombre = teclado.nextLine();

                System.out.println("Introduce el nuevo tipo:");
                String tipo = teclado.nextLine();

                p.setNombre(nombre);
                p.setTipo(tipo);

                encontrado = true;

                System.out.println("Pokemon modificado.");
            }
        }

        if (!encontrado) {
            System.out.println("Pokemon no encontrado.");
        }
    }

    // ELIMINAR POKEMON
    private static void EliminarPokemon(ArrayList<Pokemon> listaPokemon,
                                        Scanner teclado) {

        System.out.println("Introduce el ID del Pokemon:");
        int id = teclado.nextInt();
        teclado.nextLine();

        boolean encontrado = false;

        for (int i = 0; i < listaPokemon.size(); i++) {

            Pokemon p = listaPokemon.get(i);

            if (p.getId() == id) {

                System.out.println(p);

                System.out.println("¿Seguro que quieres eliminarlo? (s/n)");
                String respuesta = teclado.nextLine();

                if (respuesta.equalsIgnoreCase("s")) {

                    listaPokemon.remove(i);

                    System.out.println("Pokemon eliminado.");

                } else {
                    System.out.println("Eliminacion cancelada.");
                }

                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Pokemon no encontrado.");
        }
    }

    // CREAR XML DE UN TIPO
    private static void CrearXMLTipo(ArrayList<Pokemon> listaPokemon,
                                     Scanner teclado) {

        System.out.println("Introduce el tipo de Pokemon:");
        String tipoBuscado = teclado.nextLine();

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento = builder.newDocument();

            Element raiz = documento.createElement("pokemons");

            documento.appendChild(raiz);

            for (Pokemon p : listaPokemon) {

                if (p.getTipo().equalsIgnoreCase(tipoBuscado)) {

                    Element pokemon = documento.createElement("pokemon");

                    pokemon.setAttribute("id", String.valueOf(p.getId()));

                    Element nombre = documento.createElement("nombre");
                    nombre.setTextContent(p.getNombre());

                    Element tipo = documento.createElement("tipo");
                    tipo.setTextContent(p.getTipo());

                    pokemon.appendChild(nombre);
                    pokemon.appendChild(tipo);

                    raiz.appendChild(pokemon);
                }
            }

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(documento);

            StreamResult result = new StreamResult(
                    new File("pokemon_" + tipoBuscado + ".xml"));

            transformer.transform(source, result);

            System.out.println("XML creado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al crear el XML.");
        }
    }
}
