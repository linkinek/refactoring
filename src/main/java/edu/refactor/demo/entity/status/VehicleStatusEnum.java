package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum VehicleStatusEnum implements EnumClass<String>, CurrentState<VehicleStatusEnum> {

    DELETE("delete", new ArrayList<>()),
    OPEN("open", Arrays.asList(VehicleStatusEnum.DELETE, VehicleStatusEnum.RESERVED)),
    SERVICE("service", Arrays.asList(VehicleStatusEnum.OPEN)),
    RESERVED("reserved", Arrays.asList(VehicleStatusEnum.LEASED)),
    LEASED("leased", Arrays.asList(VehicleStatusEnum.LOST, VehicleStatusEnum.RETURNED)),
    LOST("lost", new ArrayList<>()),
    RETURNED("returned", Arrays.asList(VehicleStatusEnum.SERVICE, VehicleStatusEnum.OPEN));

    protected String id;
    protected List<VehicleStatusEnum> currentStatuses;

    VehicleStatusEnum(String value, List<VehicleStatusEnum> currentStatuses) {
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
    public List<VehicleStatusEnum> getCurrentStatuses() {
        return currentStatuses;
    }

    @Override
    public boolean hasCurrentStatuses(VehicleStatusEnum status) {
        return currentStatuses.contains(status);
    }


}
