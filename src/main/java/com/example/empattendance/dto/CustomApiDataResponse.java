package com.example.empattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomApiDataResponse<T> {
    @NotNull
	private Boolean success;
    @NotNull
    private String  message;

    private T dataObject;

}
