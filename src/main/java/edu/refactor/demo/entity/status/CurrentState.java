package edu.refactor.demo.entity.status;

import java.util.List;

public interface CurrentState<T> {

    List<T> getCurrentStatuses();

    boolean hasCurrentStatuses(T status);

}
