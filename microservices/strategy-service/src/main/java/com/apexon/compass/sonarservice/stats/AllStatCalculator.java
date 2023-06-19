package com.apexon.compass.sonarservice.stats;

import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// FIXME: 17/04/23 refactor bean injection
@Component("allStatCalculator")
@Slf4j
public class AllStatCalculator implements StatCalculator, ApplicationContextAware {

    private ApplicationContext applicationContext;
    static Reflections reflections;
    static Set<Class<? extends StatCalculator>> statClasses;

    static {
        reflections = new Reflections("com.apexon.compass.sonarservice.stats", new SubTypesScanner());
        statClasses = reflections.getSubTypesOf(StatCalculator.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public SearchParametersDto calculate(Project project) {

        SearchParametersDto iscProjectDto = SearchParametersDto.builder().build();

        List<SearchParametersDto> projects = new ArrayList<>();
        for (Class clazz : statClasses) {
            if (this.getClass() != clazz) {
                log.info("iscDTO-> " + (((StatCalculator) applicationContext.getBean(clazz)).calculate(project)));
                projects.add(((StatCalculator) applicationContext.getBean(clazz)).calculate(project));
            }
        }

        for (SearchParametersDto projectData : projects) {

            try {
                iscProjectDto = mergeObjects(iscProjectDto, projectData);
            }
            catch (IllegalAccessException | InstantiationException ex) {
                throw new ServiceException("error while merging object");
            }
        }
        return iscProjectDto;
    }

    public static <T> T mergeObjects(T first, T second) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = first.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Object returnValue = clazz.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value1 = field.get(first);
            Object value2 = field.get(second);
            Object value = (value1 != null) ? value1 : value2;
            field.set(returnValue, value);
        }
        return (T) returnValue;
    }

}
