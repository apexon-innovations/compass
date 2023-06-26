package com.apexon.compass.sonarservice.stats;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class StatCalculatorFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final String STAT_CALCULATOR_CLASSNAME_SUFFIX = "StatCalculator";

    public static final String ALL_STATS_CALCULATOR_BEANNAME = "all" + STAT_CALCULATOR_CLASSNAME_SUFFIX;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public StatCalculator getStatCalculator(String stat) {

        if (stat == null)
            stat = "all";

        String calculatorClassname = WordUtils.capitalizeFully(stat.replaceAll("[^A-z]", ""))
                + STAT_CALCULATOR_CLASSNAME_SUFFIX;

        try {
            // Use custom calculator class
            Class<?> calculatorClass = Class
                .forName(this.getClass().getPackage().getName() + "." + calculatorClassname);
            return (StatCalculator) applicationContext.getBean(calculatorClass);
        }
        catch (ClassNotFoundException e) {
            // Use All Stat Calculator
            return (AllStatCalculator) applicationContext.getBean(ALL_STATS_CALCULATOR_BEANNAME);
        }
    }

}
