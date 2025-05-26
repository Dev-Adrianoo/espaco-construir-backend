package br.com.espacoconstruir.tutoring_backend.dto;

import br.com.espacoconstruir.tutoring_backend.model.Role;

public class TeacherDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String cnpj;

    public TeacherDTO() {
    }

    public TeacherDTO(String name, String email, String password, String phone, String cnpj) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Role getRole() {
        return Role.PROFESSORA;
    }
}
