package beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    private Integer id;
    private Integer companyID;
    private Category category;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer amount;
    private Double price;
    private String Image;

    /**
     * sets automatically id=0 when adding new Coupon
     *
     * @param category    Category Enum
     * @param title       String
     * @param description String
     * @param startDate   LocalDate
     * @param endDate     LocalDAte
     * @param amount      Integer
     * @param price       Double
     * @param image       String
     */
    public Coupon(Category category, String title, String description, LocalDate startDate, LocalDate endDate, Integer amount, Double price, String image) {
        this.id = 0;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        Image = image;
    }

}

