package pulson.spring_multithreading.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/cars")
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile[] files){
        for (MultipartFile file : files){
            carService.saveCars(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Added all cars!");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCars(){
        try{
            CompletableFuture<List<Car>> cars1 = carService.getAllCars();
            CompletableFuture<List<Car>> cars2 = carService.getAllCars();
            CompletableFuture<List<Car>> cars3 = carService.getAllCars();
            CompletableFuture.allOf(cars1, cars2, cars3).join();

           //https://stackoverflow.com/questions/35809827/java-8-completablefuture-allof-with-collection-or-list

            return ResponseEntity.ok().body(cars1.get());
        } catch(InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Getting cars failed!");
        }
    }


    //https://www.amitph.com/spring-return-specific-http-status/
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public CompletableFuture<ResponseEntity> getAll(){
//        return carService.getAllCars().<ResponseEntity>thenApply(ResponseEntity::ok).exceptionally(handleGetCarFailure);
//    }
//
//    private final Function<Throwable, ResponseEntity<?>> handleGetCarFailure = throwable -> {
//        LOGGER.error("Failed to read records: {}", throwable.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get all cars");
//    };
}
