/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package top.ourlostyouth.www.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.ourlostyouth.www.enums.RequestMethodEnum;
import top.ourlostyouth.www.TokenNotValidation;
import top.ourlostyouth.www.config.bean.SecurityProperties;
import top.ourlostyouth.www.security.JwtAccessDeniedHandler;
import top.ourlostyouth.www.security.JwtAuthenticationEntryPoint;
import top.ourlostyouth.www.security.TokenProvider;
import top.ourlostyouth.www.security.service.OnlineUserService;
import top.ourlostyouth.www.security.service.UserCacheClean;

import java.util.*;

/**
 * @author Zheng Jie
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationErrorHandler;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private SecurityProperties properties;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private UserCacheClean userCacheClean;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // ?????? ROLE_ ??????
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ??????????????????
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ?????????????????? url??? @AnonymousAccess
        RequestMappingHandlerMapping
                requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // ??????????????????
        Map<String, Set<String>> anonymousUrls = getTokenNotValidationUrl(handlerMethodMap);
        return http
                // ?????? CSRF
                .csrf().disable()
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                // ????????????
                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // ??????iframe ????????????
                .and()
                .headers()
                .frameOptions()
                .disable()
                // ???????????????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // ??????????????????
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**"
                ).permitAll()
                // swagger ??????
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                // ??????
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/file/**").permitAll()
                // ???????????? druid
                .antMatchers("/druid/**").permitAll()
                // ??????OPTIONS??????
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // ???????????????????????????url???????????????????????????Token??????????????????????????? Request ??????
                // GET
                .antMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
                // POST
                .antMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
                // PUT
                .antMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
                // PATCH
                .antMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
                // DELETE
                .antMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
                // ??????????????????????????????
                .antMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
                // ???????????????????????????
                .anyRequest().authenticated()
                .and().apply(securityConfigurerAdapter()).and().build();
    }


    private TokenConfig securityConfigurerAdapter() {
        return new TokenConfig(tokenProvider, properties, onlineUserService, userCacheClean);
    }

    private Map<String, Set<String>> getTokenNotValidationUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, Set<String>> anonymousUrls = new HashMap<>(8);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            TokenNotValidation tokenNotValidation = handlerMethod.getMethodAnnotation(TokenNotValidation.class);
            if (null != tokenNotValidation) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.size() == 0 ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                Set<String> patternValues = new HashSet<>();
                if (infoEntry.getKey() != null && infoEntry.getKey().getPathPatternsCondition() != null) {
                    patternValues = infoEntry.getKey().getPathPatternsCondition().getPatternValues();
                }
                switch (Objects.requireNonNull(request)) {
                    case GET:
                        get.addAll(patternValues);
                        break;
                    case POST:
                        post.addAll(patternValues);
                        break;
                    case PUT:
                        put.addAll(patternValues);
                        break;
                    case PATCH:
                        patch.addAll(patternValues);
                        break;
                    case DELETE:
                        delete.addAll(patternValues);
                        break;
                    default:
                        all.addAll(patternValues);
                        break;
                }
            }
        }
        anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(), all);
        return anonymousUrls;
    }
}
