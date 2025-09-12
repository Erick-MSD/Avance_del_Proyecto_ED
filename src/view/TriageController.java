package view;

import controller.TriageManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.NivelTriage;
import model.Task;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

public class TriageController {

    private final TriageManager manager;
    private final ObservableList<Task> master = FXCollections.observableArrayList();
    private final FilteredList<Task> filtered = new FilteredList<>(master, t -> true);

    @FXML private TableView<Task> table;
    @FXML private TableColumn<Task, Integer> colRegistro;
    @FXML private TableColumn<Task, Integer> colPaciente;
    @FXML private TableColumn<Task, String> colNombre;
    @FXML private TableColumn<Task, String> colNivel;
    @FXML private TableColumn<Task, String> colDepto;
    @FXML private TableColumn<Task, String> colDesc;
    @FXML private TableColumn<Task, String> colFecha;
    @FXML private TextField txtFiltroDepto;
    @FXML private Label lblStatus;
    @FXML private HBox barraBotones;

    public TriageController(TriageManager manager){ this.manager = manager; }

    @FXML
    public void initialize(){
        configurarTabla();
        refrescar();
        lblStatus.setText("Listo");
    }

    private void configurarTabla(){
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("idRegistro"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        colDepto.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNivel.setCellValueFactory(cell -> Bindings.createStringBinding(() -> cell.getValue().getNivel().getColor()));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        colFecha.setCellValueFactory(cell -> Bindings.createStringBinding(() -> cell.getValue().getCreada().format(fmt)));

        table.setItems(filtered);
        table.setRowFactory(tv -> new TableRow<>(){
            @Override protected void updateItem(Task item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null){ setStyle(""); return; }
                String color;
                switch (item.getNivel()){
                    case ROJO -> color = "#ffcccc";
                    case NARANJA -> color = "#ffe0b3";
                    case AMARILLO -> color = "#fff9b3";
                    case VERDE -> color = "#ccffcc";
                    case AZUL -> color = "#cce0ff";
                    default -> color = "white";
                }
                setStyle("-fx-background-color: " + color + ";");
            }
        });
    }

    @FXML private void onCargarBD(){
        runAsync(() -> {
            int n = manager.cargarPendientesDesdeBD();
            return "Cargadas " + n + " tareas";
        });
    }

    @FXML private void onRegistrarSintoma(){
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setHeaderText("Nuevo registro por síntoma");
        idDialog.setContentText("ID Paciente:");
        Optional<String> r1 = idDialog.showAndWait();
        if (r1.isEmpty()) return;
        int pid;
        try { pid = Integer.parseInt(r1.get().trim()); } catch (NumberFormatException ex){ showError("ID inválido"); return; }
        TextInputDialog sDialog = new TextInputDialog();
        sDialog.setHeaderText("Síntoma");
        sDialog.setContentText("Descripción síntoma:");
        Optional<String> r2 = sDialog.showAndWait();
        if (r2.isEmpty()) return;
        String sintoma = r2.get();
        runAsync(() -> {
            manager.registrarPorSintomaEnBD(pid, sintoma);
            return "Registro creado (recarga BD para ver en memoria)";
        });
    }

    @FXML private void onAtenderRojo(){ atender(NivelTriage.ROJO); }
    @FXML private void onAtenderNaranja(){ atender(NivelTriage.NARANJA); }
    @FXML private void onAtenderAmarillo(){ atender(NivelTriage.AMARILLO); }
    @FXML private void onAtenderVerde(){ atender(NivelTriage.VERDE); }
    @FXML private void onAtenderAzul(){ atender(NivelTriage.AZUL); }

    private void atender(NivelTriage n){
        Task t = switch (n){
            case ROJO -> manager.atenderRojo();
            case NARANJA -> manager.atenderNaranja();
            case AMARILLO -> manager.atenderAmarillo();
            case VERDE -> manager.atenderVerde();
            case AZUL -> manager.atenderAzul();
        };
        if (t == null) showInfo("No hay pendientes nivel " + n.getColor());
        else showInfo("Atendido registro #" + t.getIdRegistro());
        refrescar();
    }

    @FXML private void onFiltroDepto(){
        String filtro = txtFiltroDepto.getText().trim();
        Predicate<Task> pred = filtro.isEmpty() ? t -> true : t -> t.getDepartamento().equalsIgnoreCase(filtro);
        filtered.setPredicate(pred);
        lblStatus.setText("Filtrado por: " + (filtro.isEmpty()? "(todos)" : filtro));
    }

    @FXML private void onPrimeraDepto(){
        String d = txtFiltroDepto.getText().trim();
        if (d.isEmpty()){ showError("Escribe un departamento en el campo"); return; }
        Task t = manager.primeraPorDepartamento(d);
        if (t == null) showInfo("No hay tareas en " + d);
        else {
            Alert a = new Alert(Alert.AlertType.INFORMATION, t.toString(), ButtonType.OK);
            a.setHeaderText("Primera en departamento " + d);
            a.showAndWait();
        }
    }
    @FXML private void onEliminarPrimeraDepto(){
        String d = txtFiltroDepto.getText().trim();
        if (d.isEmpty()){ showError("Escribe un departamento en el campo"); return; }
        boolean ok = manager.eliminarPrimeraPorDepartamento(d);
        if (ok) { showInfo("Eliminada primera de " + d); refrescar(); }
        else showInfo("No había tareas en " + d);
    }

    private void refrescar(){
        master.setAll(manager.getPendientesOrdenados());
    }

    private void runAsync(TaskSupplier supplier){
        lblStatus.setText("Trabajando...");
        new Thread(() -> {
            String msg;
            try { msg = supplier.work(); }
            catch (Exception ex){ msg = "Error: " + ex.getMessage(); }
            String finalMsg = msg;
            Platform.runLater(() -> { refrescar(); lblStatus.setText(finalMsg); });
        }, "worker-triage").start();
    }

    private void showError(String m){
        Alert a = new Alert(Alert.AlertType.ERROR, m, ButtonType.OK); a.setHeaderText("Error"); a.showAndWait();
    }
    private void showInfo(String m){
        Alert a = new Alert(Alert.AlertType.INFORMATION, m, ButtonType.OK); a.setHeaderText("Info"); a.showAndWait();
    }

    @FunctionalInterface private interface TaskSupplier { String work() throws Exception; }
}
