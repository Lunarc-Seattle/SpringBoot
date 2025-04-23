package com.pm.patientservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Patient { //这句话是说：“我要创建一个叫 Patient（病人）的模板”。 像做饺子的模具一样，它是一个模板，告诉我们每一个“病人”应该有什么。
    @Id //这是说：“下面这个字段是唯一编号”，就像你考试的学号，不能重复。
    @GeneratedValue(strategy = GenerationType.AUTO)//这句是说：“这个编号是自动生成的”，不需要我们自己来写编号，电脑会自动帮我们生成。
    private UUID id; //这句话是说：“每个病人都有一个编号（id），它是UUID类型”。UUID 是一种特别的编号，比如：123e4567-e89b-12d3-a456-426614174000，用来保证每个人的编号都不一样。
}
