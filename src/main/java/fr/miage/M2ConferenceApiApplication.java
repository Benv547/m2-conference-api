package fr.miage;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@EnableRetry
@EnableSwagger2
public class M2ConferenceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(M2ConferenceApiApplication.class, args);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    private class RetryableDataSourceBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName)
                throws BeansException {
            if (bean instanceof DataSource dataSource) {
                bean = new RetryableDataSource(dataSource);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName)
                throws BeansException {
            return bean;
        }
    }

    @Bean
    public BeanPostProcessor dataSouceWrapper() {
        return new RetryableDataSourceBeanPostProcessor();
    }
}

class RetryableDataSource extends AbstractDataSource {

    private DataSource delegate;

    public RetryableDataSource(DataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    @Retryable(maxAttempts=10, backoff=@Backoff(multiplier=2.3, maxDelay=30000))
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    @Retryable(maxAttempts=10, backoff=@Backoff(multiplier=2.3, maxDelay=30000))
    public Connection getConnection(String username, String password)
            throws SQLException {
        return delegate.getConnection(username, password);
    }

}
