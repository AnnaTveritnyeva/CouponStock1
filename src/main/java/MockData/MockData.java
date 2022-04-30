package MockData;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import utils.DateUtils;


public class MockData {
    //Companies:
    public static Company mcDonaldS = new Company("McDonald's", "bigMac@gmail.com", "burger2022");
    public static Company apple = new Company("Apple", "apple@gmail.com", "iphone20");
    public static Company mojo = new Company("Mojo", "MojoRestaurant@gmail.com", "sushiNumber1");
    public static Company elAl = new Company("El-Al", "elal@gmail.com", "elAl123");
    public static Company johnBryce = new Company("John Bryce Academy", "johnBrice@gmail.com", "johny123");

    //Customers:
    public static Customer anna = new Customer("Anna", "Tveritnyeva", "atveretniv@gmail.com", "anna123");
    public static Customer zeev = new Customer("Zeev", "Mindali", "zeev@gmail.com", "zeev123");
    public static Customer tal = new Customer("Tal", "Rozner", "tal@gmail.com", "tal123");
    public static Customer yair = new Customer("Yair", "Nadav", "yair@gmail.com", "yair123");
    public static Customer tomer = new Customer("Tomer", "Yoel", "tomer@gmail.com", "tomer123");

    //Coupons:
    public static Coupon mcDonaldSCoupon1 = new Coupon(Category.Food, "BigMac", "10% discount!!", DateUtils.getStartDate(), DateUtils.getEndDate(), 10, 25.5, "https://www.mcdonalds.com/is/image/");
    public static Coupon mcDonaldSCoupon2 = new Coupon(Category.Electricity, "Ice Cream machine", "5000 shekels discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 25_000.9, "https://www.mcdonalds.com/is/image/");
    public static Coupon mcDonaldSCoupon3 = new Coupon(Category.Restaurant, "Meal for 2", "1+1", DateUtils.getStartDate(), DateUtils.getEndDate(), 15, 60.5, "https://www.mcdonalds.com/is/image/");
    public static Coupon mcDonaldSCoupon4 = new Coupon(Category.Vacation, "McDonald's Hotel", "15% discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 2000.0, "https://www.mcdonalds.com/is/image/");
    public static Coupon mcDonaldSCoupon5 = new Coupon(Category.Education, "Burger making lessons", "1000 shekels off!", DateUtils.getStartDate(), DateUtils.getEndDate(), 35, 3_000.0, "https://www.mcdonalds.com/is/image/");

    public static Coupon appleCoupon1 = new Coupon(Category.Food, "Meal at apple", "10% discount!!", DateUtils.getStartDate(), DateUtils.getEndDate(), 18, 64.5, "https://res.cloudinary");
    public static Coupon appleCoupon2 = new Coupon(Category.Electricity, "AirPods Pro", "100 shekels discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 800.90, "https://res.cloudinary");
    public static Coupon appleCoupon3 = new Coupon(Category.Restaurant, "Meal for 2", "1+1", DateUtils.getStartDate(), DateUtils.getEndDate(), 15, 60.5, "https://res.cloudinary");
    public static Coupon appleCoupon4 = new Coupon(Category.Vacation, "Vacation on Apple", "buy for 10000$ and get free", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 0.0, "https://res.cloudinary");
    public static Coupon appleCoupon5 = new Coupon(Category.Education, "App developer for Apple", "1000 shekels off!", DateUtils.getStartDate(), DateUtils.getEndDate(), 35, 13_000.0, "https://res.cloudinary");

    public static Coupon mojoCoupon1 = new Coupon(Category.Food, "Sushi Combination", "10% discount!!", DateUtils.getStartDate(), DateUtils.getEndDate(), 18, 60.5, "https://res.cloudinary");
    public static Coupon mojoCoupon2 = new Coupon(Category.Electricity, "Rice baking machine", "100 shekels discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 786.90, "https://res.cloudinary");
    public static Coupon mojoCoupon3 = new Coupon(Category.Restaurant, "Meal for 2", "1+1", DateUtils.getStartDate(), DateUtils.getEndDate(), 15, 60.5, "https://res.cloudinary");
    public static Coupon mojoCoupon4 = new Coupon(Category.Vacation, "Vacation in Japan", "25% off", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 20_000.0, "https://res.cloudinary");
    public static Coupon mojoCoupon5 = new Coupon(Category.Education, "Sushi making courses", "1000 shekels off!", DateUtils.getStartDate(), DateUtils.getEndDate(), 35, 15_000.0, "https://res.cloudinary");

    public static Coupon elAlCoupon1 = new Coupon(Category.Food, "Free meal in the Plane", "buy 6 tickets and get free meal", DateUtils.getStartDate(), DateUtils.getEndDate(), 10, 12_000.9, "https://res.cloudinary");
    public static Coupon elAlCoupon2 = new Coupon(Category.Electricity, "Mini plane", "100 shekels discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 800.90, "https://res.cloudinary");
    public static Coupon elAlCoupon3 = new Coupon(Category.Restaurant, "Meal at the Airport for 2", "1+1", DateUtils.getStartDate(), DateUtils.getEndDate(), 15, 60.5, "https://res.cloudinary");
    public static Coupon elAlCoupon4 = new Coupon(Category.Vacation, "Tickets to USA", "25% off", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 1_500.0, "https://res.cloudinary");
    public static Coupon elAlCoupon5 = new Coupon(Category.Education, "flying courses", "1000 shekels off!", DateUtils.getStartDate(), DateUtils.getEndDate(), 35, 53_000.0, "https://res.cloudinary");


    public static Coupon johnBryceCoupon1 = new Coupon(Category.Food, "Free meal", "buy 10 meals and get one for free", DateUtils.getStartDate(), DateUtils.getEndDate(), 13, 1_000.9, "https://res.cloudinary");
    public static Coupon johnBryceCoupon2 = new Coupon(Category.Electricity, "Mac for developing", "100 shekels discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 13_000.8, "https://res.cloudinary");
    public static Coupon johnBryceCoupon3 = new Coupon(Category.Restaurant, "Meal at the College for 2", "1+1", DateUtils.getStartDate(), DateUtils.getEndDate(), 15, 60.5, "https://res.cloudinary");
    public static Coupon johnBryceCoupon4 = new Coupon(Category.Vacation, "Tickets to USA", "25% off", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 1_500.0, "https://res.cloudinary");
    public static Coupon johnBryceCoupon5 = new Coupon(Category.Education, "Java FullStack", "1000 shekels off!", DateUtils.getStartDate(), DateUtils.getEndDate(), 35, 20_000.0, "https://res.cloudinary");

    public static Coupon mcDonaldSCoupon = new Coupon(Category.Food, "BigMac", "10% discount!!", DateUtils.getStartDate(), DateUtils.getEndDate(), 10, 25.5, "https://www.mcdonalds.com/is/image/");
    public static Coupon appleCoupon = new Coupon(Category.Electricity, "AirPods Pro", "100 shekels discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 700.0, "https://res.cloudinary");
    public static Coupon mojoCoupon = new Coupon(Category.Food, "Big Salmon Combination", "1+1", DateUtils.getStartDate(), DateUtils.getEndDate(), 15, 60.5, "https://cdn77-s3.lazycatkitchen.com");
    public static Coupon elAlCoupon = new Coupon(Category.Vacation, "Tickets to New York", "15% discount", DateUtils.getStartDate(), DateUtils.getEndDate(), 5, 1_000.0, "https://upload.wikimedia.org");
    public static Coupon johnBryceCoupon = new Coupon(Category.Education, "Java Full Stack", "1000 shekels off!", DateUtils.getStartDate(), DateUtils.getEndDate(), 50, 21_000.0, "https://upload.wikimedia.org");


}
