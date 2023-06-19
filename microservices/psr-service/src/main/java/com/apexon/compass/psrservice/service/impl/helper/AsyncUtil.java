package com.apexon.compass.psrservice.service.impl.helper;

import static com.apexon.compass.constants.CompassUtilityConstants.FILE_DELETION_ERROR_MESSAGE;

import com.apexon.compass.exception.custom.ServiceException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncUtil {

    @Async("threadPoolExecutor")
    public void deleteTempFolder(File currentWorkingRepository) {
        // Code to delete temporary folder
        try (Stream<Path> walk = Files.walk(Paths.get(currentWorkingRepository.getParentFile().getAbsolutePath()));) {

            walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
        catch (IOException e) {
            throw new ServiceException(FILE_DELETION_ERROR_MESSAGE);
        }
    }

}
