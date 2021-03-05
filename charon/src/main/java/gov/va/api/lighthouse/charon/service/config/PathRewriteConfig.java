package gov.va.api.lighthouse.charon.service.config;

import gov.va.api.lighthouse.talos.PathRewriteFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Slf4j
@Configuration
public class PathRewriteConfig {

  static String leadingPath() {
    return "/charon/";
  }

  @Bean
  FilterRegistrationBean<PathRewriteFilter> pathRewriteFilter() {
    var registration = new FilterRegistrationBean<PathRewriteFilter>();
    PathRewriteFilter filter = PathRewriteFilter.builder().removeLeadingPath(leadingPath()).build();
    registration.setFilter(filter);
    registration.setOrder(Ordered.LOWEST_PRECEDENCE);
    registration.addUrlPatterns(filter.removeLeadingPathsAsUrlPatterns());
    log.info("PathRewriteFilter is enabled.");
    return registration;
  }
}
