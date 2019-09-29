package edu.refactor.demo.entity.status;

import org.assertj.core.util.Lists;
import org.springframework.lang.Nullable;

import java.util.List;

public enum RentStatusEnum implements EnumClass<String>, CurrentState<RentStatusEnum> {

    CREATED("created", Lists.newArrayList()),
    EXPIRED("expired", Lists.newArrayList(RentStatusEnum.ACTIVE)),
    ACTIVE("active", Lists.newArrayList());

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
