import controller.TriageManager;
import dao.DBConnection;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection(); // si tu clase tiene ctor sin args
        TriageManager app = new TriageManager(db);

        var sc = new Scanner(System.in);
        boolean run = true;

        while (run){
            System.out.println("\n=== TRIAGE CONSOLA ===");
            System.out.println("1) Cargar pendientes desde BD");
            System.out.println("2) Registrar en BD por síntoma (usa función PL/pgSQL)");
            System.out.println("3) Atender ROJO (PILA)");
            System.out.println("4) Atender NARANJA (COLA)");
            System.out.println("5) Atender AMARILLO (COLA)");
            System.out.println("6) Atender VERDE (COLA)");
            System.out.println("7) Atender AZUL (COLA)");
            System.out.println("8) Buscar por ID de REGISTRO (HASH)");
            System.out.println("9) Buscar por ID de PACIENTE (HASH)");
            System.out.println("10) Primera por DEPARTAMENTO (LISTA)");
            System.out.println("11) Eliminar primera por DEPARTAMENTO (LISTA)");
            System.out.println("12) Ver pendientes ordenados (nivel/dep/fecha)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            int op = Integer.parseInt(sc.nextLine().trim());
            try {
                switch (op){
                    case 1 -> {
                        int n = app.cargarPendientesDesdeBD();
                        System.out.println("Cargadas " + n + " tareas desde BD.");
                    }
                    case 2 -> {
                        System.out.print("ID paciente: "); int pid = Integer.parseInt(sc.nextLine());
                        System.out.print("Síntoma: "); String s = sc.nextLine();
                        app.registrarPorSintomaEnBD(pid, s);
                        System.out.println("Registro creado en BD. (Corre opción 1 para recargar a ED)");
                    }
                    case 3 -> System.out.println("Atendida: " + app.atenderRojo());
                    case 4 -> System.out.println("Atendida: " + app.atenderNaranja());
                    case 5 -> System.out.println("Atendida: " + app.atenderAmarillo());
                    case 6 -> System.out.println("Atendida: " + app.atenderVerde());
                    case 7 -> System.out.println("Atendida: " + app.atenderAzul());
                    case 8 -> {
                        System.out.print("ID REGISTRO: "); int id = Integer.parseInt(sc.nextLine());
                        var t = app.buscarPorRegistro(id);
                        System.out.println(t == null ? "No encontrada." : t.toString());
                    }
                    case 9 -> {
                        System.out.print("ID PACIENTE: "); int id = Integer.parseInt(sc.nextLine());
                        var l = app.buscarPorPaciente(id);
                        if (l.isEmpty()) System.out.println("Sin tareas para paciente " + id);
                        else l.forEach(System.out::println);
                    }
                    case 10 -> {
                        System.out.print("Departamento: "); String d = sc.nextLine();
                        var t = app.primeraPorDepartamento(d);
                        System.out.println(t == null ? "No hay tareas en " + d : t.toString());
                    }
                    case 11 -> {
                        System.out.print("Departamento: "); String d = sc.nextLine();
                        boolean ok = app.eliminarPrimeraPorDepartamento(d);
                        System.out.println(ok ? "Eliminada primera de " + d : "No había tareas en " + d);
                    }
                    case 12 -> System.out.println(app.verPendientesOrdenados());
                    case 0 -> run = false;
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception ex){
                System.out.println("Error: " + ex.getMessage());
            }
        }

        sc.close();
        System.out.println("Adiós.");
    }
}
