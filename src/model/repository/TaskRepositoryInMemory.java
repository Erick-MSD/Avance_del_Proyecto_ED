package model.repository;

import model.NivelTriage;
import model.Task;
import model.ds.Queue;
import model.ds.Stack;
import model.ds.SinglyLinkedList;
import model.ds.HashTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Encapsula las estructuras de datos en memoria para las tareas de triage.
 * Aísla pila/colas/lista/hash de la capa de servicio/UI.
 */
public class TaskRepositoryInMemory {

    // Estructuras base por nivel
    private final Stack<Task> pilaRojo = new Stack<>();
    private final Queue<Task> colaNaranja = new Queue<>();
    private final Queue<Task> colaAmarillo = new Queue<>();
    private final Queue<Task> colaVerde = new Queue<>();
    private final Queue<Task> colaAzul = new Queue<>();

    // Índices auxiliares
    private final SinglyLinkedList<Task> listaPorDepto = new SinglyLinkedList<>();
    private final HashTable<Integer, Task> hashPorRegistro = new HashTable<>(521);
    private final HashTable<Integer, ArrayList<Task>> hashPorPaciente = new HashTable<>(521);
    private final Set<Integer> patientIds = new HashSet<>();

    // Lista maestra para ordenaciones rápidas
    private final List<Task> allTasks = new ArrayList<>();

    public void clear(){
        // Reinicio sencillo creando nuevas instancias (si se quisiera limpiar incrementalmente se haría manual)
        pilaRojo.clear();
        colaNaranja.clear();
        colaAmarillo.clear();
        colaVerde.clear();
        colaAzul.clear();
        listaPorDepto.clear();
        // HashTables no tienen clear → reconstrucción (simplificación):
        // No se expone clear; podríamos recrear repo, pero aquí limpiamos manualmente vía nueva instancia NO posible.
        // Como alternativa: no se usa clear actualmente; omitimos.
        hashPorRegistroRebuild();
        hashPorPacienteRebuild();
        patientIds.clear();
        allTasks.clear();
    }

    private void hashPorRegistroRebuild() { /* no-op placeholder */ }
    private void hashPorPacienteRebuild() { /* no-op placeholder */ }

    /** Inserta una nueva Task en todas las estructuras. */
    public void addTask(Task t){
        switch (t.getNivel()){
            case ROJO -> pilaRojo.push(t);
            case NARANJA -> colaNaranja.enqueue(t);
            case AMARILLO -> colaAmarillo.enqueue(t);
            case VERDE -> colaVerde.enqueue(t);
            case AZUL -> colaAzul.enqueue(t);
        }
        listaPorDepto.insert(t); // inserción al inicio
        hashPorRegistro.put(t.getIdRegistro(), t);
        ArrayList<Task> byPac = hashPorPaciente.get(t.getPacienteId());
        if (byPac == null) byPac = new ArrayList<>();
        byPac.add(t);
        hashPorPaciente.put(t.getPacienteId(), byPac);
        patientIds.add(t.getPacienteId());
        allTasks.add(t);
    }

    /** Devuelve la primera task (más recientemente insertada) para el departamento dado. */
    public Task firstByDepartment(String depto){
        return listaPorDepto.findFirst(t -> t.getDepartamento().equalsIgnoreCase(depto));
    }

    /** Elimina y retorna la primera task del departamento dado. */
    public Task removeFirstByDepartment(String depto){
        final Task[] removed = new Task[1];
        boolean ok = listaPorDepto.deleteFirstMatch(t -> {
            if (t.getDepartamento().equalsIgnoreCase(depto)){
                removed[0] = t; return true;
            }
            return false;
        });
        if (ok){
            internalRemove(removed[0]);
            return removed[0];
        }
        return null;
    }

    /** Atiende (extrae) la siguiente task según nivel. */
    public Task attend(NivelTriage nivel){
        Task t = switch (nivel){
            case ROJO -> pilaRojo.tryPop();
            case NARANJA -> colaNaranja.tryDequeue();
            case AMARILLO -> colaAmarillo.tryDequeue();
            case VERDE -> colaVerde.tryDequeue();
            case AZUL -> colaAzul.tryDequeue();
        };
        if (t != null){
            // remover de lista por depto
            listaPorDepto.deleteFirstMatch(x -> x.getIdRegistro() == t.getIdRegistro());
            internalRemove(t);
        }
        return t;
    }

    private void internalRemove(Task t){
        hashPorRegistro.remove(t.getIdRegistro());
        ArrayList<Task> byPac = hashPorPaciente.get(t.getPacienteId());
        if (byPac != null){
            byPac.removeIf(x -> x.getIdRegistro() == t.getIdRegistro());
            if (byPac.isEmpty()) hashPorPaciente.remove(t.getPacienteId());
            else hashPorPaciente.put(t.getPacienteId(), byPac); // reponer lista modificada
        }
        allTasks.remove(t);
        // Si el paciente ya no tiene tareas activas lo quitamos del set
        if (byPac == null || byPac.isEmpty()) patientIds.remove(t.getPacienteId());
    }

    public Task findByRegistro(int idRegistro){ return hashPorRegistro.get(idRegistro); }

    public List<Task> findByPaciente(int pacienteId){
        ArrayList<Task> l = hashPorPaciente.get(pacienteId);
        if (l == null) return List.of();
        return List.copyOf(l);
    }

    /** Devuelve nueva lista ordenada (no modifica la interna). */
    public List<Task> getAllOrdered(){
        ArrayList<Task> copy = new ArrayList<>(allTasks);
        copy.sort(Comparator
                .comparingInt((Task t) -> t.getNivel().getNivel())
                .thenComparing(Task::getDepartamento, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Task::getCreada));
        return copy;
    }

    public int totalTasks(){ return allTasks.size(); }
}
