package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.Address;
import edu.miu.cs.cs544.domain.State;
import edu.miu.cs.cs544.domain.dto.StateDTO;

public class StateAdapter {
    public static State getState(StateDTO stateDTO) {
        return new State(
                stateDTO.getId(),
                stateDTO.getCode(),
                stateDTO.getName(),
                AuditDataAdapter.getAuditData(stateDTO.getAuditDataDTO())
        );
    }

    public static StateDTO getStateDTO(State state) {
        return new StateDTO(
                state.getId(),
                state.getCode(),
                state.getName(),
                AuditDataAdapter.getAuditDataDTO(state.getAuditData())
        );
    }
}
