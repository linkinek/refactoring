package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public enum RentStatusEnum implements EnumClass<String>, CurrentState<String> {

    CREATED("created", new ArrayList<>()),
    EXPIRED("expired", singletonList("active")),
    ACTIVE("active", new ArrayList<>()),
    COMPLETED("completed", singletonList("active"));

    protected String id;
    protected List<String> currentStatuses;

    RentStatusEnum(String id, List<String> currentStatuses) {
        this.id = id;
        this.currentStatuses = currentStatuses;
    }

    @Override
    public List<String> getCurrentStatuses() {
        return currentStatuses;
    }

    @Override
    public boolean hasCurrentStatuses(String status) {
        return currentStatuses.contains(status);
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static RentStatusEnum fromId(String id) {
        for (RentStatusEnum at : RentStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
