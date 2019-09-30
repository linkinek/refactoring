package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public enum RentStatusEnum implements EnumClass<String>, CurrentState<RentStatusEnum> {

    CREATED("created", new ArrayList<>()),
    EXPIRED("expired", singletonList(RentStatusEnum.ACTIVE)),
    ACTIVE("active", new ArrayList<>()),
    COMPLETED("completed", singletonList(RentStatusEnum.ACTIVE));

    protected String id;
    protected List<RentStatusEnum> currentStatuses;

    RentStatusEnum(String id, List<RentStatusEnum> currentStatuses) {
        this.id = id;
        this.currentStatuses = currentStatuses;
    }

    @Override
    public List<RentStatusEnum> getCurrentStatuses() {
        return null;
    }

    @Override
    public boolean hasCurrentStatuses(RentStatusEnum status) {
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
