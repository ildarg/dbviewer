package com.atcproject.dbviewer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TableView {
  List<String> columns = new ArrayList<>(); // instead of String ColumnMetaView should be used to have - type, size and etc
  List<TableRowView> rows = new ArrayList<>();
}
