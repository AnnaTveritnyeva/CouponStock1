package beans;


public enum Category {
    Food,
    Electricity,
    Restaurant,
    Vacation,
    Education;

    /**
     * sets final value to each Category
     */
    private final int value = 1 + ordinal();

    /**
     * converts value of Category to Category
     *
     * @param value value of Category (int)
     * @return Category
     */
    public static Category valueOf(int value) {
        for (Category category : values()) {
            if (category.value == (value)) {
                return category;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
