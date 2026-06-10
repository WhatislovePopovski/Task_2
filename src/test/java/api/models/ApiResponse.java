package api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;
    private Order order;
    private List<Order> orders;
    private Integer total;
    private Integer totalToday;
}