package controller;

import dao.TriageDAO;
import dao.DBConnection;
import model.*;
import model.ds.*;

// importa SOLO lo que necesitas de util:
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TriageManager {

    private final TriageDAO triageDAO;   // <-- FALTA ESTA LÍNEA

    // ...
    private final Stack<Task> pilaRojo = new Stack<>();    // model.ds.Stack
    private final Queue<Task> colaNaranja = new Queue<>(); // model.ds.Queue     // Nivel 2
    private final Queue<Task> colaAmarillo = new Queue<>();     // Nivel 3
    private final Queue<Task> colaVerde = new Queue<>();        // Nivel 4
    private final Queue<Task> colaAzul = new Queue<>();         // Nivel 5

    // Lista y Hashes auxiliares
    private final SinglyLinkedList<Task> listaPorDepto = new SinglyLinkedList<>();
    private final HashTable<Integer, Task> hashPorRegistro = new HashTable<>(521);
    private final HashTable<Integer, List<Task>> hashPorPaciente = new HashTable<>(521);

    public TriageManager(DBConnection db){ this.triageDAO = new TriageDAO(db); }

    // ===== Carga desde BD a ED =====
    public int cargarPendientesDesdeBD() throws Exception {
        var lista = triageDAO.listarPendientes();
        int count = 0;
        for (var p : lista){
            Task t = new Task(
                    p.idRegistro(), p.pacienteId(), p.nivel(),
                    p.departamento(), p.descripcion(), p.creada(),
                    p.pacienteNombre()
            );
            encolarSegunNivel(t);
            indexar(t);
            count++;
        }
        return count;
    }

    // ===== Altas nuevas usando tu función PL/pgSQL =====
    public void registrarPorSintomaEnBD(int idPaciente, String sintoma) throws Exception {
        triageDAO.asignarPorSintoma(idPaciente, sintoma);
    }

    // ===== Operaciones ED =====
    private void encolarSegunNivel(Task t){
        switch (t.getNivel()){
            case ROJO     -> pilaRojo.push(t);
            case NARANJA  -> colaNaranja.enqueue(t);
            case AMARILLO -> colaAmarillo.enqueue(t);
            case VERDE    -> colaVerde.enqueue(t);
            case AZUL     -> colaAzul.enqueue(t);
        }
        listaPorDepto.insert(t);
    }

    private void indexar(Task t){
        hashPorRegistro.put(t.getIdRegistro(), t);
        List<Task> l = hashPorPaciente.get(t.getPacienteId());
        if (l == null) l = new ArrayList<>();
        l.add(t);
        hashPorPaciente.put(t.getPacienteId(), l);
    }
    private void desindexar(Task t){
        hashPorRegistro.remove(t.getIdRegistro());
        List<Task> l = hashPorPaciente.get(t.getPacienteId());
        if (l != null){ l.removeIf(x -> x.getIdRegistro() == t.getIdRegistro()); hashPorPaciente.put(t.getPacienteId(), l); }
    }

    public Task atenderRojo(){ var t = pilaRojo.pop(); desindexar(t); return t; }
    public Task atenderNaranja(){ var t = colaNaranja.dequeue(); desindexar(t); return t; }
    public Task atenderAmarillo(){ var t = colaAmarillo.dequeue(); desindexar(t); return t; }
    public Task atenderVerde(){ var t = colaVerde.dequeue(); desindexar(t); return t; }
    public Task atenderAzul(){ var t = colaAzul.dequeue(); desindexar(t); return t; }

    // Búsquedas/listado
    public Task buscarPorRegistro(int idRegistro){ return hashPorRegistro.get(idRegistro); }
    public List<Task> buscarPorPaciente(int idPaciente){
        List<Task> l = hashPorPaciente.get(idPaciente);
        return l == null ? List.of() : new ArrayList<>(l);
    }
    public Task primeraPorDepartamento(String dpto){
        return listaPorDepto.findFirst(t -> t.getDepartamento().equalsIgnoreCase(dpto));
    }
    public boolean eliminarPrimeraPorDepartamento(String dpto){
        return listaPorDepto.deleteFirstMatch(t -> {
            boolean ok = t.getDepartamento().equalsIgnoreCase(dpto);
            if (ok) desindexar(t);
            return ok;
        });
    }

    /** Vista ordenada por urgencia (nivel asc) y luego por depto y fecha. */
    public String verPendientesOrdenados(){
        List<Task> all = new ArrayList<>();
        // No tenemos iteradores sobre ED; tomamos del hash (solo pendientes indexados)
        // Si marcas “atendidos” aparte, filtra aquí.
        // En este demo, todo lo indexado es “pendiente”.
        // Supone ids de registro no son densos → simplemente recorre hash sería ideal;
        // como HashTable es simple, recopilamos desde listas por paciente:
        // (más simple: construimos una lista auxiliar desde colas/pila; aquí usamos hashPorPaciente)
        // Para no complejizar, hacemos un set para no duplicar.
        var set = new java.util.HashSet<Integer>();
        for (int pid = 1; pid <= 100000; pid++){ // rango grande; en real mantén un catálogo de pids cargados
            var l = hashPorPaciente.get(pid);
            if (l != null) for (var t : l) if (set.add(t.getIdRegistro())) all.add(t);
        }

        all.sort(Comparator
            .comparingInt((Task t) -> t.getNivel().getNivel())  // 1..5
            .thenComparing(Task::getDepartamento, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(Task::getCreada));

        var sb = new StringBuilder();
        for (Task t : all) sb.append(t).append("\n");
        return sb.toString();
    }
}
