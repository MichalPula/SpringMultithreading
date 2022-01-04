package pulson.spring_multithreading.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CarServiceImpl implements CarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;
    private final CSVService csvService;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, CSVService csvService) {
        this.carRepository = carRepository;
        this.csvService = csvService;
    }

    @Async("Pulson's TaskExecutor")
    public void saveCars(MultipartFile file) {
        long start = System.currentTimeMillis();

        LOGGER.info("Parsing file: {}", file.getOriginalFilename());
        List<Car> cars = csvService.parseCSVFile(file);

        LOGGER.info("Saving a list of cars of size {} records", cars.size());
        carRepository.saveAll(cars);

        LOGGER.info("Elapsed time: {}ms", (System.currentTimeMillis() - start));
    }



    //po próbie zmiany typu zwrotnego na List<Car>
    //Method annotated with @Async should return 'void' or "Future-like" type
    @Async("Pulson's TaskExecutor")
    public CompletableFuture<List<Car>> getAllCars() { //throws IllegalArgumentException
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("Request to get a list of cars");

        List<Car> cars = carRepository.findAll();

        //throw new IllegalArgumentException();

        return CompletableFuture.completedFuture(cars);
    }
}
