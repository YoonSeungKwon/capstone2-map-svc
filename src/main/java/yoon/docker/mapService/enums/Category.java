package yoon.docker.mapService.enums;

import lombok.Getter;

@Getter
public enum Category {

    MEAL("식사"),

    CAFE("카페"),

    BEVERAGE("주류"),

    PICTURE("사진"),

    SHOPPING("쇼핑"),

    PRESENT("선물"),

    OTHERS("기타");

    private final String category;

    Category(String category){
        this.category = category;
    }

    public static Category fromString(String category) {
        for (Category c : Category.values()) {
            if (c.category.equalsIgnoreCase(category)) {
                return c;
            }
        }
        return OTHERS;
    }

}
