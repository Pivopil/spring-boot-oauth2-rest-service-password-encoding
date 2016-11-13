/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pivopil.rest.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.annotation.*;

/**
 * Annotate Spring MVC method arguments with this annotation to indicate you wish to
 * specify the argument with the value of the current
 * {@link Authentication#getPrincipal()} found on the {@link SecurityContextHolder}.
 *
 * <p>
 * Creating your own annotation that uses {@link AuthenticationPrincipal} as a meta
 * annotation creates a layer of indirection between your code and Spring Security's. For
 * simplicity, you could instead use the {@link AuthenticationPrincipal} directly.
 * </p>
 *
 * @author Rob Winch
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
