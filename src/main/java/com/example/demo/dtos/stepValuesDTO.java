package com.example.demo.dtos;

import com.example.demo.entity.Champ;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class stepValuesDTO {


    Long idForm;
    String formLabel;
    List<Champ> formValues;


}
