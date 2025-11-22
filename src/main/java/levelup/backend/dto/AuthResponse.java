package levelup.backend.dto;

import java.util.List;

public class AuthResponse {

    private String token;
    private String nombre;
    private String email;
    private List<String> roles;
    private Integer puntos;
    private Integer descuentoPct;

    public AuthResponse() {
    }

    public AuthResponse(String token,
                        String nombre,
                        String email,
                        List<String> roles,
                        Integer puntos,
                        Integer descuentoPct) {
        this.token = token;
        this.nombre = nombre;
        this.email = email;
        this.roles = roles;
        this.puntos = puntos;
        this.descuentoPct = descuentoPct;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getDescuentoPct() {
        return descuentoPct;
    }

    public void setDescuentoPct(Integer descuentoPct) {
        this.descuentoPct = descuentoPct;
    }
}
