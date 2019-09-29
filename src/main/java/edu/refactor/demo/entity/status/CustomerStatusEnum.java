package edu.refactor.demo.entity.status;

import org.springframework.lang.Nullable;

public enum  CustomerStatusEnum implements EnumClass<String>, TimeOut {

    DEFAULT("default", 86400),
    VIP("vip", 259200);

    protected String id;
    protected int timeOut;

    CustomerStatusEnum(String id, int timeOut) {
        this.id = id;

        this.timeOut = timeOut;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static CustomerStatusEnum fromId(String id) {
        for (CustomerStatusEnum at : CustomerStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    @Override
    public int getTimeOut() {
        return timeOut;
    }
}
