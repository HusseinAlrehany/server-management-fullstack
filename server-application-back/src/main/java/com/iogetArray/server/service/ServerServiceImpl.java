package com.iogetArray.server.service;

import com.iogetArray.server.exceptions.NotFoundException;
import com.iogetArray.server.models.Server;
import com.iogetArray.server.serverRepository.ServerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.iogetArray.server.enumeration.Status.SERVER_DOWN;
import static com.iogetArray.server.enumeration.Status.SERVER_UP;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService{

    @Autowired
    private ServerRepository serverRepository;
    @Override
    public Server create(Server server) {
        log.info("Saving a new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }



    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server Ip: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress inetAddress= InetAddress.getByName(ipAddress);
        server.setStatus(inetAddress.isReachable(10000) ? SERVER_UP: SERVER_DOWN);

        serverRepository.save(server);

        return server ;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching All Servers");
        Collection<Server> all = serverRepository.findAll(PageRequest.of(0, limit)).toList();
        if(all.isEmpty()){
            throw new NotFoundException("OOPS! No Servers Found");
        }
        return all;
    }

    @Override
    public Server get(Long id) {
        log.info("Get Server Id ", id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());

        return serverRepository.save(server);

    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server: {}", id);

        serverRepository.deleteById(id);

        return Boolean.TRUE;
    }

    private String setServerImageUrl() {
        String [] imageNames = {"server1.png","server2.png","server3.png","server4.png"};
       return ServletUriComponentsBuilder.fromCurrentContextPath()
               .path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
