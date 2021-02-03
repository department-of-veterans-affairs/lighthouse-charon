package gov.va.api.lighthouse.vistalink.service.config;

import gov.va.api.lighthouse.talos.PathRewriteFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PathRewriteConfig {

  @Bean
  FilterRegistrationBean<PathRewriteFilter> pathRewriteFilter() {
    var registration = new FilterRegistrationBean<PathRewriteFilter>();
    PathRewriteFilter filter = PathRewriteFilter.builder().removeLeadingPath("/vistalink/").build();
    registration.setFilter(filter);
    registration.setOrder(2);
    registration.addUrlPatterns(filter.removeLeadingPathsAsUrlPatterns());
    log.info("PathRewriteFilter is enabled.");
    return registration;
  }
}
