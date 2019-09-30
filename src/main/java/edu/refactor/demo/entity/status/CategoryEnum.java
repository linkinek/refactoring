package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;

public enum CategoryEnum implements EnumClass<String> {
    DEFAULT("default");

    String id;

    CategoryEnum(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return null;
    }

    @Nullable
    public static CategoryEnum fromId(String id) {
        for (CategoryEnum at : CategoryEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
