package yoon.docker.mapService.enums;

import lombok.Getter;

@Getter
public enum Category {

    숙소("숙소", 1),
    교통("교통", 2),
    식비("식비", 3),
    오락("오락", 4);

    private final String name;
    private final int index;

    Category(String name, int index){
        this.index = index;
        this.name = name;
    }

}
