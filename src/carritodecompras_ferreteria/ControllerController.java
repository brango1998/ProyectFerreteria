/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package carritodecompras_ferreteria;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author neon
 */
public class ControllerController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TableView<nodo> tabla;
    @FXML
    private TableColumn ide;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn preci;
    @FXML
    private TableColumn unidad;
    @FXML
    private TableColumn marc;
    @FXML
    private Button bt1;
    @FXML
    private Button bt2;
    @FXML
    private Button bt3;
    @FXML
    private TextField t1;
    @FXML
    private TextField t2;
    @FXML
    private TextField t3;
    @FXML
    private TextField t4;
    @FXML
    private TextField t5;

    ObservableList<nodo> nodos;
    ObservableList<String> historial;
    @FXML
    private Button bt4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        historial = FXCollections.observableArrayList();
        nodos = FXCollections.observableArrayList();
        File file = new File("src/ferrete/producto.txt");

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                nodo nike = new nodo(line[0], line[1], line[2], Double.parseDouble(line[3]), Integer.parseInt(line[4]));
                nodos.add(nike);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se pudo encontrar");
        }
        ide.setCellValueFactory(new PropertyValueFactory<>("id"));
        ide.setStyle("-fx-alignment: CENTER-LEFT");
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nom.setStyle("-fx-alignment: CENTER-LEFT");
        marc.setCellValueFactory(new PropertyValueFactory<>("marca"));
        marc.setStyle("-fx-alignment: CENTER-LEFT");
        unidad.setCellValueFactory(new PropertyValueFactory<>("unidades"));
        unidad.setStyle("-fx-alignment: CENTER-LEFT");
        preci.setCellValueFactory(new PropertyValueFactory<>("precio"));
        preci.setStyle("-fx-alignment: CENTER-LEFT");
        int size = nodos.size();
        for (int i = 0; i < size; i++) {
            int prevIndex = i == 0 ? size - 1 : i - 1;
            int nextIndex = i == size - 1 ? 0 : i + 1;
            nodos.get(i).setAnt(nodos.get(prevIndex));
            nodos.get(i).setSig(nodos.get(nextIndex));
        }
        tabla.setItems(nodos);
    }

    @FXML
    private void añadir(ActionEvent event) {
        if (t1.getText().trim().isEmpty() || t2.getText().trim().isEmpty() || t3.getText().trim().isEmpty() || t4.getText().trim().isEmpty()
                || t5.getText().trim().isEmpty()) {
            // Mostrar mensaje de advertencia sobre campos vacíos
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Mensaje de información");
            alerta.setTitle("Diálogo de advertencia");
            alerta.setContentText("Es necesario llenar todos los campos");
            alerta.showAndWait();
            return;
        }

        // Luego crea un nuevo nodo con los datos ingresados
        nodo nuevo = new nodo(t2.getText().trim(), t3.getText().trim(), t1.getText().trim(),
                Double.parseDouble(t4.getText().trim()), Integer.parseInt(t5.getText().trim()));

        // Agregar el nuevo nodo al inicio de la lista
        if (!nodos.isEmpty()) {
            nodo ultimo = nodos.get(nodos.size() - 1);
            nuevo.setSig(nodos.get(0)); // El nuevo nodo apunta al primer nodo actual
            ultimo.setSig(nuevo); // El último nodo actual apunta al nuevo nodo
        } else {
            // Si la lista está vacía, hacer que el nuevo nodo apunte a sí mismo
            nuevo.setSig(nuevo); // El único nodo apunta a sí mismo
        }
        nodos.add(0, nuevo);

        // Actualizar la tabla y limpiar los campos de texto
        tabla.setItems(nodos);
        tabla.refresh();
        t1.clear();
        t2.clear();
        t3.clear();
        t4.clear();
        t5.clear();

        guardarNodoEnArchivoInicio(nuevo);
    }

    private void guardarNodoEnArchivoInicio(nodo nodo) {
        String archivoRuta = "src/ferrete/producto.txt";

        try {
            // Leer el contenido actual del archivo
            Scanner scanner = new Scanner(new File(archivoRuta));
            StringBuilder fileContent = new StringBuilder();

            while (scanner.hasNextLine()) {
                fileContent.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            // Agregar los datos del nuevo nodo al inicio del contenido del archivo
            String nuevoDato = nodo.getNom() + "," + nodo.getId() + "," + nodo.getMarca() + "," + nodo.getPrecio() + "," + nodo.getUnidades() + "\n";
            fileContent.insert(0, nuevoDato);

            // Escribir el contenido actualizado en el archivo
            FileWriter fileWriter = new FileWriter(archivoRuta);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(fileContent.toString());
            bufferedWriter.close();

            // Mostrar un mensaje de éxito
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Datos guardados dentro del archivo");
            alerta.setContentText("Los datos se guardaron correctamente.");
            alerta.showAndWait();
        } catch (IOException e) {
            // Mostrar un mensaje de error en caso de excepción
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("No se pudieron guardar los datos");
            alerta.showAndWait();
        }
    }

    @FXML
    private void comprar(ActionEvent event) {
        // Obtener el zapato seleccionado en la tabla
        nodo fe = tabla.getSelectionModel().getSelectedItem();

        if (fe == null) {
            // Mostrar mensaje de advertencia si no se ha seleccionado ningún zapato
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText("No se ha seleccionado ningún elemento");
            alerta.setContentText("Seleccione un elemento para comprar.");
            alerta.showAndWait();
            return;
        }

        // Crear un diálogo para obtener la cantidad de unidades a comprar
        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Cantidad a comprar");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Ingrese la cantidad a comprar, debe ser menor a " + fe.getUnidades() + "\n");
        Optional<String> cantidad = dialogo.showAndWait();

        if (!cantidad.isPresent()) {
            return; // El usuario ha cerrado el diálogo
        } else if (!cantidad.get().matches("^[1-9]\\d*$")) {
            // La entrada no es un número entero positivo
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText("Entrada inválida");
            alerta.setContentText("La cantidad de unidades debe ser un número entero positivo.");
            alerta.showAndWait();
            return;
        }

        int cantidadComprar = Integer.parseInt(cantidad.get());

        if (cantidadComprar > fe.getUnidades()) {
            // Mostrar mensaje de advertencia si la cantidad de unidades no está disponible
            Alert ale = new Alert(Alert.AlertType.INFORMATION);
            ale.setHeaderText("Información");
            ale.setContentText("La cantidad de unidades no está disponible");
            ale.showAndWait();
            return;
        }

        // Actualizar la cantidad de unidades en el objeto Nodo
        fe.setUnidades(fe.getUnidades() - cantidadComprar);

        // Abre el archivo para lectura y escritura
        String archivoRuta = "src/ferrete/producto.txt";
        File archivo = new File(archivoRuta);

        try {
            // Crear un ObservableList para almacenar las líneas del archivo
            ObservableList<String> lineas = FXCollections.observableArrayList();

            // Leer todo el contenido del archivo y almacenarlo en el ObservableList
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextLine()) {
                lineas.add(scanner.nextLine());
            }
            scanner.close();

            // Encuentra y actualiza el elemento correspondiente en el ObservableList
            for (int i = 0; i < lineas.size(); i++) {
                String[] elementos = lineas.get(i).split(",");
                if (elementos.length >= 5 && elementos[0].equals(fe.getId())) {
                    int unidades = Integer.parseInt(elementos[4]);
                    unidades -= cantidadComprar;
                    elementos[4] = Integer.toString(unidades);
                    lineas.set(i, String.join(",", elementos));
                    break;
                }
            }

            // Abre el archivo nuevamente para escritura
            FileWriter fileWriter = new FileWriter(archivoRuta);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribe todas las líneas actualizadas en el archivo
            for (String linea : lineas) {
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
            }

            // Cierra el BufferedWriter
            bufferedWriter.close();

            // Mostrar información de la compra y confirmar el pago
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setHeaderText("¿Deseas comprar el elemento " + fe.getNom() + "?");
            alerta.setContentText("El precio del zapato es: " + fe.getPrecio() + "\nEl total a pagar es: " + (fe.getPrecio() * cantidadComprar));
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Pago realizado correctamente
                Alert notify = new Alert(Alert.AlertType.INFORMATION);
                notify.setTitle("¡Proceso Exitoso!");
                notify.setHeaderText("Cargando información...");
                notify.setContentText("Pago Realizado Correctamente!");
                notify.show();
                historial.add("Compra: " + fe.getNom() + ", Cantidad: " + cantidadComprar + ", Total: $" + (fe.getPrecio() * cantidadComprar));
            }

            if (fe.getUnidades() <= 0) {
                // Si no quedan unidades disponibles, eliminar el automóvil de la lista
                nodos.remove(fe);

                // Actualizar la tabla de zapatos
                tabla.setItems(null);
                tabla.layout();
                tabla.setItems(FXCollections.observableList(nodos));

                // Mostrar mensaje de información si no quedan unidades disponibles
                Alert ale = new Alert(Alert.AlertType.INFORMATION);
                ale.setHeaderText("Información");
                ale.setContentText("Ya no quedan unidades disponibles");
                ale.showAndWait();
            }

            // Actualizar la tabla
            tabla.refresh();
        } catch (IOException e) {
            // Mostrar un mensaje de error en caso de excepción
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al actualizar el archivo");
            alerta.setContentText("Se produjo un error al actualizar el archivo.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void mostrar(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        openWindow("VentanaProductos.fxml", stage);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (!nodos.isEmpty()) {
            // Eliminar el último elemento de la pila
            nodos.remove(nodos.size() - 1);

            // Abre el archivo para lectura y escritura
            String archivoRuta = "src/ferrete/producto.txt";
            File archivo = new File(archivoRuta);

            try {
                // Crear un ObservableList para almacenar las líneas del archivo
                ObservableList<String> lineas = FXCollections.observableArrayList();

                // Leer todo el contenido del archivo y almacenarlo en el ObservableList
                Scanner scanner = new Scanner(archivo);
                while (scanner.hasNextLine()) {
                    lineas.add(scanner.nextLine());
                }
                scanner.close();

                // Abre el archivo nuevamente para escritura
                FileWriter fileWriter = new FileWriter(archivoRuta);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                // Escribir todas las líneas en el archivo, excepto la última
                for (int i = 0; i < lineas.size() - 1; i++) {
                    bufferedWriter.write(lineas.get(i));
                    bufferedWriter.newLine();
                }

                // Cerrar el BufferedWriter
                bufferedWriter.close();

                // Mostrar un mensaje de éxito
                Alert ale = new Alert(Alert.AlertType.INFORMATION);
                ale.setHeaderText("Información");
                ale.setContentText("Elemento eliminado al final de la lista y del archivo!");
                ale.showAndWait();
                tabla.refresh();
            } catch (IOException e) {
                // Mostrar un mensaje de error en caso de excepción
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("Error al eliminar");
                alerta.setContentText("Se produjo un error al eliminar el elemento del archivoo.");
                alerta.showAndWait();
            }
        }
    }

    private void openWindow(String fxmlFileName, Stage stage) throws IOException {
        // Crear un nuevo cargador de FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));

        // Cargar el archivo FXML y asignarlo como la raíz de la ventana
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Establecer la nueva escena como la escena de la ventana
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void buscar(ActionEvent event) {
        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Buscar producto");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Ingrese el nombre del producto a buscar:");

        Optional<String> nombreProducto = dialogo.showAndWait();

        if (nombreProducto.isPresent()) {
            for (nodo producto : nodos) {
                if (producto.getNom().equalsIgnoreCase(nombreProducto.get())) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Detalles del producto");
                    alerta.setHeaderText("Detalles de " + producto.getNom());
                    alerta.setContentText("Marca: " + producto.getMarca() + "\nNombre: " + producto.getNom()
                            + "\nPrecio: " + producto.getPrecio() + "\nUnidades disponibles: " + producto.getUnidades());
                    alerta.showAndWait();
                    return;
                }
            }
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Producto no encontrado");
            alerta.setHeaderText(null);
            alerta.setContentText("El producto no se encuentra en la lista.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void cambiar(ActionEvent event) {
        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Cambiar unidades");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Ingrese el nombre del producto para cambiar unidades:");

        Optional<String> nombreProducto = dialogo.showAndWait();

        if (nombreProducto.isPresent()) {
            for (nodo producto : nodos) {
                if (producto.getNom().equalsIgnoreCase(nombreProducto.get())) {
                    TextInputDialog unidadesDialog = new TextInputDialog("");
                    unidadesDialog.setTitle("Modificar unidades");
                    unidadesDialog.setHeaderText(null);
                    unidadesDialog.setContentText("Unidades actuales: " + producto.getUnidades() + ". Ingrese la nueva cantidad:");

                    Optional<String> unidadesNuevas = unidadesDialog.showAndWait();

                    if (unidadesNuevas.isPresent()) {
                        int nuevasUnidades = Integer.parseInt(unidadesNuevas.get());
                        producto.setUnidades(nuevasUnidades);

                        // Actualizar el archivo con las nuevas unidades
                        guardarActualizacionArchivo(producto);
                        return;
                    }
                }
            }
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Producto no encontrado");
            alerta.setHeaderText(null);
            alerta.setContentText("El producto no se encuentra en la lista.");
            alerta.showAndWait();
        }
    }

}
