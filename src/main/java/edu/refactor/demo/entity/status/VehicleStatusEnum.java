package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum VehicleStatusEnum implements EnumClass<String>, CurrentState<String> {

    DELETE("delete", new ArrayList<>()),
    OPEN("open", Arrays.asList("delete", "reserved")),
    SERVICE("service", Arrays.asList("open")),
    RESERVED("reserved", Arrays.asList("leased")),
    LEASED("leased", Arrays.asList("lost", "returned")),
    LOST("lost", new ArrayList<>()),
    RETURNED("returned", Arrays.asList("service", "open"));

    protected String id;
    protected List<String> currentStatuses;

    VehicleStatusEnum(String value, List<String> currentStatuses) {
        this.id = value;
        this.currentStatuses = currentStatuses;
    }

    @Nullable
    public static VehicleStatusEnum fromId(String id) {
        for (VehicleStatusEnum at : VehicleStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> getCurrentStatuses() {
        return currentStatuses;
    }

    @Override
    public boolean hasCurrentStatuses(String status) {
        return currentStatuses.contains(status);
    }


}
