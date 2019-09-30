package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;
import java.util.List;
import org.assertj.core.util.Lists;

public enum VehicleStatusEnum implements EnumClass<String>, CurrentState<VehicleStatusEnum> {

    DELETE("delete", Lists.newArrayList()),
    OPEN("open", Lists.newArrayList(VehicleStatusEnum.DELETE, VehicleStatusEnum.RESERVED)),
    SERVICE("service", Lists.newArrayList(VehicleStatusEnum.OPEN)),
    RESERVED("reserved", Lists.newArrayList(VehicleStatusEnum.LEASED)),
    LEASED("leased", Lists.newArrayList(VehicleStatusEnum.LOST, VehicleStatusEnum.RETURNED)),
    LOST("lost", Lists.newArrayList()),
    RETURNED("returned", Lists.newArrayList(VehicleStatusEnum.SERVICE, VehicleStatusEnum.OPEN));

    protected String id;
    protected List<VehicleStatusEnum> currentStatuses;

    VehicleStatusEnum(String value, List<VehicleStatusEnum>  currentStatuses) {
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
    public List<VehicleStatusEnum> getCurrentStatuses(){
        return currentStatuses;
    }

    @Override
    public boolean hasCurrentStatuses(VehicleStatusEnum status) {
        return currentStatuses.contains(status);
    }


}
