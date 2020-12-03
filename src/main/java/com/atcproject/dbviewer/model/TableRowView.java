package com.atcproject.dbviewer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TableRowView {
    List<String> values = new ArrayList<>();
}
