package com.atcproject.dbviewer.service;

import com.atcproject.dbviewer.model.ConnectionDetail;
import com.atcproject.dbviewer.repository.ConnectionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectionService {

    private final ConnectionDetailRepository connectionDetailRepository;

    public List<ConnectionDetail> getAllConnections(){
        return connectionDetailRepository.findAll();
    }

    public ConnectionDetail getConnectionById(Long id){
        return connectionDetailRepository.getOne(id);
    }

    public ConnectionDetail saveConnection(ConnectionDetail connectionDetail) {
        return connectionDetailRepository.save(connectionDetail);
    }

    public void deleteConnection(ConnectionDetail connectionDetail) {
        connectionDetailRepository.delete(connectionDetail);
    }

    public void deleteConnectionById(Long id) {
        connectionDetailRepository.deleteById(id);
    }
}
