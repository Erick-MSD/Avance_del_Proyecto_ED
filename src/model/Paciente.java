package model;

public class Paciente {
    private int id;
    private String nombre;
    private String apellido;
    private int edad;
    private String departamento;
    private NivelTriage nivelTriage;

    public Paciente(int id, String nombre, String apellido, int edad, String departamento, NivelTriage nivelTriage) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.departamento = departamento;
        this.nivelTriage = nivelTriage;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getDepartamento() { return departamento; }
    public NivelTriage getNivelTriage() { return nivelTriage; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setNivelTriage(NivelTriage nivelTriage) { this.nivelTriage = nivelTriage; }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", departamento='" + departamento + '\'' +
                ", nivelTriage=" + nivelTriage +
                '}';
    }
}
