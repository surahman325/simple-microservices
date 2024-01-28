package id.surahman.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
    private Integer status = 0;
    private String message;
    private Object data;
}