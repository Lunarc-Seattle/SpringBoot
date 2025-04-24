package com.pm.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Patient { //这句话是说：“我要创建一个叫 Patient（病人）的模板”。 像做饺子的模具一样，它是一个模板，告诉我们每一个“病人”应该有什么。
    @Id //这是说：“下面这个字段是唯一编号”，就像你考试的学号，不能重复。
    @GeneratedValue(strategy = GenerationType.AUTO)//这句是说：“这个编号是自动生成的”，不需要我们自己来写编号，电脑会自动帮我们生成。
    private UUID id; //这句话是说：“每个病人都有一个编号（id），它是UUID类型”。UUID 是一种特别的编号，比如：123e4567-e89b-12d3-a456-426614174000，用来保证每个人的编号都不一样。

    @NotNull
    //意思是：
    // “这个值不能为空！必须要填！”
    // 就像你交作业时老师说：“名字一定要写！”
    private String name;// will add a column for patient database
    //这是病人的名字，是一个文本（String）类型。
    //你写 @NotNull，所以名字不能空白，必须写上！
    //📝 数据库里会加一列：name


    @NotNull
    @Email //这个注解的意思是：“这个值必须是一个正确的邮箱格式！"比如 abc@gmail.com 是对的，但 abc123 就不对。
    @Column(unique = true)
    //这个是说：
    //“这个邮箱不能重复！”
    //就像一个人只能有一个身份证号码，不能有两个一样的邮箱。
    private String email;//这是病人的邮箱地址，是字符串类型的。



    @NotNull
    private String address;//地址。也是文本类型。加了 @NotNull，所以地址也必须填。

    @NotNull
    private LocalDate dateOfBirth;//这是出生日期，是 LocalDate 类型（表示年月日，不含时间）。

    @NotNull
    private LocalDate registeredDate;//这是“注册日期”，表示病人什么时候登记的，也是年月日格式，不含时间。




// 下面是generate getter setter:
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }

}
