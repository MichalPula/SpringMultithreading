package pulson.spring_multithreading.car;

import pulson.spring_multithreading.Configurations.AsyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfiguration.class);

    public List<Car> parseCSVFile(MultipartFile file) {
        List<Car> cars = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Car car = new Car();
                car.setManufacturer(data[0]);
                car.setModel(data[1]);
                cars.add(car);
            }
        } catch(IOException e) {
            LOGGER.error("Failed to parse CSV file {}", e.getMessage());
        }
        return cars;
    }
}
