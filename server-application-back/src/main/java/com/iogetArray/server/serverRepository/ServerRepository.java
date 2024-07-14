package com.iogetArray.server.serverRepository;

import com.iogetArray.server.models.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findByIpAddress(String ipAddress);
}
