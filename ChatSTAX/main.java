
package pruebapokemon;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

import javax.xml.stream.*;

public class PruebaPokemon {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        ArrayList<Pokemon> listaPokemon = new ArrayList<>();

        int opcion = -1;

        LeerStax(listaPokemon);

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
                    GuardarStax(listaPokemon);
                    break;

                case 3:
                    ModificarPokemon(listaPokemon, teclado);
                    GuardarStax(listaPokemon);
                    break;

                case 4:
                    EliminarPokemon(listaPokemon, teclado);
                    GuardarStax(listaPokemon);
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

    // LEER XML
    private static void LeerStax(ArrayList<Pokemon> listaPokemon) {

        try {

            File archivo = new File("pokemon.xml");

            if (!archivo.exists()) {
                return;
            }

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader xml = factory.createXMLStreamReader(
                    new java.io.FileInputStream(archivo));

            int id = 0;
            String nombre = "";
            String tipo = "";

            while (xml.hasNext()) {

                int evento = xml.next();

                if (evento == XMLStreamConstants.START_ELEMENT) {

                    if (xml.getLocalName().equals("pokemon")) {
                        id = Integer.parseInt(xml.getAttributeValue(0));
                    }

                    if (xml.getLocalName().equals("nombre")) {
                        nombre = xml.getElementText();
                    }

                    if (xml.getLocalName().equals("tipo")) {
                        tipo = xml.getElementText();

                        listaPokemon.add(new Pokemon(id, nombre, tipo));
                    }
                }
            }

            xml.close();

        } catch (Exception e) {
            System.out.println("Error al leer XML.");
        }
    }

    // GUARDAR XML
    private static void GuardarStax(ArrayList<Pokemon> listaPokemon) {

        try {

            XMLOutputFactory factory = XMLOutputFactory.newInstance();

            XMLStreamWriter xml = factory.createXMLStreamWriter(
                    new FileWriter("pokemon.xml"));

            xml.writeStartDocument("UTF-8", "1.0");
            xml.writeStartElement("pokemons");

            for (Pokemon p : listaPokemon) {

                xml.writeStartElement("pokemon");
                xml.writeAttribute("id", String.valueOf(p.getId()));

                xml.writeStartElement("nombre");
                xml.writeCharacters(p.getNombre());
                xml.writeEndElement();

                xml.writeStartElement("tipo");
                xml.writeCharacters(p.getTipo());
                xml.writeEndElement();

                xml.writeEndElement();
            }

            xml.writeEndElement();
            xml.writeEndDocument();

            xml.close();

        } catch (Exception e) {
            System.out.println("Error al guardar XML.");
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
                break;
            }
        }

        if (existe) {
            System.out.println("Ese ID ya existe.");
        } else {

            System.out.println("Introduce el nombre:");
            String nombre = teclado.nextLine();

            System.out.println("Introduce el tipo:");
            String tipo = teclado.nextLine();

            listaPokemon.add(new Pokemon(id, nombre, tipo));

            System.out.println("Pokemon creado.");
        }
    }

    // MODIFICAR POKEMON
    private static void ModificarPokemon(ArrayList<Pokemon> listaPokemon,
                                         Scanner teclado) {

        System.out.println("Introduce el ID:");
        int id = teclado.nextInt();
        teclado.nextLine();

        boolean encontrado = false;

        for (Pokemon p : listaPokemon) {

            if (p.getId() == id) {

                System.out.println("Nuevo nombre:");
                p.setNombre(teclado.nextLine());

                System.out.println("Nuevo tipo:");
                p.setTipo(teclado.nextLine());

                encontrado = true;
                System.out.println("Pokemon modificado.");
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Pokemon no encontrado.");
        }
    }

    // ELIMINAR POKEMON
    private static void EliminarPokemon(ArrayList<Pokemon> listaPokemon,
                                        Scanner teclado) {

        System.out.println("Introduce el ID:");
        int id = teclado.nextInt();
        teclado.nextLine();

        boolean encontrado = false;

        for (int i = 0; i < listaPokemon.size(); i++) {

            Pokemon p = listaPokemon.get(i);

            if (p.getId() == id) {

                System.out.println(p);

                System.out.println("¿Eliminar? (s/n)");
                String respuesta = teclado.nextLine();

                if (respuesta.equalsIgnoreCase("s")) {
                    listaPokemon.remove(i);
                    System.out.println("Pokemon eliminado.");
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

        System.out.println("Introduce el tipo:");
        String tipoBuscado = teclado.nextLine();

        try {

            XMLOutputFactory factory = XMLOutputFactory.newInstance();

            XMLStreamWriter xml = factory.createXMLStreamWriter(
                    new FileWriter("pokemon_" + tipoBuscado + ".xml"));

            xml.writeStartDocument("UTF-8", "1.0");
            xml.writeStartElement("pokemons");

            for (Pokemon p : listaPokemon) {

                if (p.getTipo().equalsIgnoreCase(tipoBuscado)) {

                    xml.writeStartElement("pokemon");
                    xml.writeAttribute("id", String.valueOf(p.getId()));

                    xml.writeStartElement("nombre");
                    xml.writeCharacters(p.getNombre());
                    xml.writeEndElement();

                    xml.writeStartElement("tipo");
                    xml.writeCharacters(p.getTipo());
                    xml.writeEndElement();

                    xml.writeEndElement();
                }
            }

            xml.writeEndElement();
            xml.writeEndDocument();

            xml.close();

            System.out.println("XML creado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al crear XML.");
        }
    }
}
