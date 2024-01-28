package id.surahman.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {
    private Long id;
    private String code;
    private Double price;
    private Integer qty;
}
