package model;

public enum NivelTriage {
    ROJO(1, "Rojo", "Urgencia máxima"),
    NARANJA(2, "Naranja", "Urgencia alta"),
    AMARILLO(3, "Amarillo", "Urgencia moderada"),
    VERDE(4, "Verde", "Urgencia baja"),
    AZUL(5, "Azul", "Urgencia mínima");

    private final int nivel;
    private final String color;
    private final String descripcion;

    NivelTriage(int nivel, String color, String descripcion) {
        this.nivel = nivel;
        this.color = color;
        this.descripcion = descripcion;
    }

    public int getNivel() { return nivel; }
    public String getColor() { return color; }
    public String getDescripcion() { return descripcion; }
}
