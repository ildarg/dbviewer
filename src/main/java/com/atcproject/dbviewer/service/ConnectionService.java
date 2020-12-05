package com.atcproject.dbviewer.service;

import com.atcproject.dbviewer.handlers.DBVEntityNotFound;
import com.atcproject.dbviewer.model.ConnectionDetail;
import com.atcproject.dbviewer.repository.ConnectionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectionService {

    private final ConnectionDetailRepository connectionDetailRepository;

    public List<ConnectionDetail> getAllConnections() {
        return connectionDetailRepository.findAll();
    }

    public ConnectionDetail getConnectionDetailsById(Long id) {
        Optional<ConnectionDetail> conn = connectionDetailRepository.findById(id);
        if (conn.isEmpty()) {
            throw new DBVEntityNotFound("Connection", id);
        }
        return conn.get();
    }

    public ConnectionDetail saveConnectionDetails(ConnectionDetail connectionDetail) {
        return connectionDetailRepository.save(connectionDetail);
    }

    public void deleteConnectionDetails(ConnectionDetail connectionDetail) {
        connectionDetailRepository.delete(connectionDetail);
    }

    public void deleteConnectionDetailsById(Long id) {
        connectionDetailRepository.deleteById(id);
    }
}
