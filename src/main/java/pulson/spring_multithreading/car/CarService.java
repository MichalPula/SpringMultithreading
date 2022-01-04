package pulson.spring_multithreading.car;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface CarService {
    void saveCars(MultipartFile file);
    CompletableFuture<List<Car>> getAllCars();
}
