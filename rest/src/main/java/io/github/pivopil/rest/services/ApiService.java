package io.github.pivopil.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 05.07.16.
 */
@Service
public class ApiService {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private EndpointHandlerMapping endpointHandlerMapping;

    @Autowired
    private FrameworkEndpointHandlerMapping frameworkEndpointHandlerMapping;

    public Map<String, Map<String, String>> getApi() {

        return Stream.of(
                requestMappingHandlerMapping.getHandlerMethods(),
                endpointHandlerMapping.getHandlerMethods(),
                frameworkEndpointHandlerMapping.getHandlerMethods())
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(transformToKey, transformToValue));
    }

    private Function<? super Map.Entry<RequestMappingInfo, HandlerMethod>, ? extends String> transformToKey = entry -> {
        HandlerMethod handlerMethod = entry.getValue();

        String beanName = handlerMethod.getBean().toString();

        if (beanName.contains("@")) {
            String url = entry.getKey().getPatternsCondition().getPatterns().iterator().next();
            return url.replace("/", "");
        }

        return beanName + "." + handlerMethod.getMethod().getName();


    };

    private Function<? super Map.Entry<RequestMappingInfo, HandlerMethod>, ? extends Map<String, String>> transformToValue = entry -> {
        RequestMappingInfo requestMappingInfo = entry.getKey();

        Map<String, String> map = new HashMap<>();

        Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();


        if (methods.size() > 0) {
            map.put("method", methods.iterator().next().toString());
        } else {
            map.put("method", RequestMethod.GET.toString());
        }

        map.put("url", requestMappingInfo.getPatternsCondition().getPatterns().iterator().next());

        return map;
    };
}
