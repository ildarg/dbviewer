package com.atcproject.dbviewer.rest;

import com.atcproject.dbviewer.model.ConnectionDetail;
import com.atcproject.dbviewer.model.TableView;
import com.atcproject.dbviewer.service.ConnectionService;
import com.atcproject.dbviewer.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/connections")
@RequiredArgsConstructor
public class ConnectionResource {

    private final ConnectionService connectionService;

    private final DatabaseService databaseService;

    @GetMapping
    public ResponseEntity<List<ConnectionDetail>> getConnections() {
        final List<ConnectionDetail> connections = connectionService.getAllConnections();
        return new ResponseEntity<>(connections, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConnectionDetail> getConnection(@PathVariable Long id) {
        final ConnectionDetail connection = connectionService.getConnectionById(id);
        return ResponseEntity.ok(connection);
    }

    @GetMapping(value = "/{id}/schemas")
    public ResponseEntity<List<String>> getSchemasByConnection(@PathVariable Long id) {
        final List<String> schemas = databaseService.getSchemas(id);
        return ResponseEntity.ok(schemas);
    }

    @GetMapping(value = "/{id}/tables")
    public ResponseEntity<List<String>> getTablesByConnection(@PathVariable Long id,
                                                              @RequestParam(required = false) String schema) {
        final List<String> tables = databaseService.getTables(id, schema);
        return ResponseEntity.ok(tables);
    }

    @GetMapping(value = "/{id}/columns")
    public ResponseEntity<List<String>> getColumnsByConnection(@PathVariable Long id,
                                                               @RequestParam(required = false) String schema,
                                                               @RequestParam(required = false) String table) {
        final List<String> tables = databaseService.getColumns(id, schema, table);
        return ResponseEntity.ok(tables);
    }

    @GetMapping(value = "/{id}/data")
    public ResponseEntity<TableView> getTableDataByConnection(@PathVariable Long id,
                                                                    @RequestParam String schema,
                                                                    @RequestParam String table) {
        final TableView data = databaseService.getData(id, schema, table);
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<ConnectionDetail> createConnection(@Valid @RequestBody ConnectionDetail connectionDetail) {
        log.debug("connectionDetail {}", connectionDetail);
        ConnectionDetail savedConnection = connectionService.saveConnection(connectionDetail);
        return ResponseEntity.ok(savedConnection);
    }

    @PutMapping
    public ResponseEntity<ConnectionDetail> updateConnection(@Valid @RequestBody ConnectionDetail connectionDetail) {
        ConnectionDetail updatedConnection = connectionService.saveConnection(connectionDetail);
        return ResponseEntity.ok(updatedConnection);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteConnection(@RequestBody ConnectionDetail connectionDetail) {
        connectionService.deleteConnection(connectionDetail);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        connectionService.deleteConnectionById(id);
        return ResponseEntity.noContent().build();
    }

    // better to move to ControllerAdvice class
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
