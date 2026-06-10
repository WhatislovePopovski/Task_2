package api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredients {
    private List<Ingredient> data;
    private boolean success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingredient {
        private String _id;
        private String name;
        private String type;
        private Integer proteins;
        private Integer fat;
        private Integer carbohydrates;
        private Integer calories;
        private Integer price;
        private String image;
        private String image_mobile;
        private String image_large;
        private Integer __v;
    }
}