package com.atcproject.dbviewer.handlers;

import com.atcproject.dbviewer.Constants;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DBVEntityNotFound extends DBViewerException {
    public DBVEntityNotFound(String entityName, Long id) {
        super(Constants.ERR_ENTITY_NOT_FOUND, Constants.ENTITY_NOT_FOUND, entityName + " id: " + id);
    }
}
