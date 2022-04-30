package beans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Coupon> coupons;

    /**
     * sets automatically id=0 when adding new Customer
     *
     * @param firstName String
     * @param lastName  String
     * @param email     String
     * @param password  String
     */
    public Customer(String firstName, String lastName, String email, String password) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     *constructor for getting data from DB
     * @param id Integer
     * @param firstName String
     * @param lastName String
     * @param email String
     * @param password String
     */
    public Customer(Integer id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
