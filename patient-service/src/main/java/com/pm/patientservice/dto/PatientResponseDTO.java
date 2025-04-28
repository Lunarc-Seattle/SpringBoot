package com.pm.patientservice.dto;


//在Spring Boot（或者更广泛说，在Java后端开发）里，DTO 是 Data Transfer Object 的缩写，意思是数据传输对象。

//简单说，DTO就是一个专门用来装数据的小盒子，它的作用是：
//👉 在不同系统层（比如 Controller → Service → Repository）之间传递数据，
//而不是直接用数据库里的实体对象（Entity）
//🔵 为什么要用DTO？
//安全性：可以避免把数据库里的所有字段都暴露出去（比如用户密码）。
//灵活性：可以自由组合数据，只传需要的部分。
//解耦合：前端需要变化的时候，不用动数据库结构，只改DTO就行了。
//简化数据：有些时候，前端只要3个字段，DTO就只传这3个，减少网络传输量。
public class PatientResponseDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
