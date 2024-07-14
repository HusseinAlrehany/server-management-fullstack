package com.iogetArray.server.resources;

import com.iogetArray.server.enumeration.Status;
import com.iogetArray.server.exceptions.NotFoundException;
import com.iogetArray.server.models.Response;
import com.iogetArray.server.models.Server;
import com.iogetArray.server.service.ServerService;
import com.iogetArray.server.service.ServerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {

    private final ServerServiceImpl serverServiceImpl;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers() {
        //this line of code to see another state of the app (LOADING STATE)
        //TimeUnit.SECONDS.sleep(3);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("servers", serverServiceImpl.list(30)))
                        .message(" Servers retrieved Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverServiceImpl.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", server))
                        .message(server.getStatus() == Status.SERVER_UP ? "Ping Success" : "Ping Failed")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", serverServiceImpl.create(server)))
                        .message("Server Created Success")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> pingServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", serverServiceImpl.get(id)))
                        .message("Server Retrieved Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server deleted", serverServiceImpl.delete(id)))
                        .message("Server Deleted Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    //the following is for image setting
    //we just inform that we produce an image of type PNG not a JSON
    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
       return Files.readAllBytes(Paths.get(System.getProperty("user.home") +
               "/Downloads/images/" + fileName));
    }
}
