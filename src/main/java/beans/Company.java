package beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private List<Coupon> coupons;


    /**
     * sets automatically id=0 when adding new Company
     *
     * @param name     String
     * @param email    String
     * @param password String
     */
    public Company(String name, String email, String password) {
        this.id = 0;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * constructor for getting data from DB
     * @param id Integer
     * @param name String
     * @param email String
     * @param password String
     */
    public Company(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
