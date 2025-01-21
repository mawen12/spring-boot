/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContextFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContextFactory;
import org.springframework.util.ClassUtils;

/**
 * An enumeration of possible types of web application.
 *
 * @author Andy Wilkinson
 * @author Brian Clozel
 * @since 2.0.0
 */
public enum WebApplicationType {

	/**
	 * 不会以Web应用启动，并且不会启动内置Web服务
	 *
	 * @see DefaultApplicationContextFactory
	 */
	NONE,

	/**
	 * 运行基于Servlet的Web应用，并启动内置的Servlet Web服务器，其负责创建{@link ServletWebServerApplicationContext}
	 *
	 * @see ServletWebServerApplicationContext
	 * @see ServletWebServerApplicationContextFactory
	 */
	SERVLET,

	/**
	 * 运行Reactive的Web应用，并启动内置的Reactive Web服务器
	 *
	 * @see ReactiveWebServerApplicationContext
	 * @see AnnotationConfigReactiveWebServerApplicationContext
	 * @see ReactiveWebServerApplicationContextFactory
	 */
	REACTIVE;

	private static final String[] SERVLET_INDICATOR_CLASSES = { "jakarta.servlet.Servlet", "org.springframework.web.context.ConfigurableWebApplicationContext" };

	private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

	private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

	private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

	/**
	 * 从类路径检测Web应用类型
	 * <ul>
	 *     <li>{@link org.springframework.web.reactive.DispatcherHandler} -> {@link WebApplicationType#REACTIVE}</li>
	 *     <li>{@link WebApplicationType#NONE}</li>
	 *     <li>
	 *         <ol>
	 *             {@link org.springframework.web.servlet.DispatcherServlet},
	 *             {@link jakarta.servlet.Servlet},
	 *             {@link org.springframework.web.context.ConfigurableWebApplicationContext}  -> {@link WebApplicationType#SERVLET}
	 *         </ol>
	 *     </li>
	 * </ul>
	 *
	 * @return
	 */
	static WebApplicationType deduceFromClasspath() {
		if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
				&& !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
			// 仅提供了DispatcherHandler，则为REACTIVE
			return WebApplicationType.REACTIVE;
		}
		for (String className : SERVLET_INDICATOR_CLASSES) {
			if (!ClassUtils.isPresent(className, null)) {
				// Server和ConfigurableWebApplicationContext均为提供，则为NONE
				return WebApplicationType.NONE;
			}
		}
		// 默认为SERVLET
		return WebApplicationType.SERVLET;
	}

	static class WebApplicationTypeRuntimeHints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			for (String servletIndicatorClass : SERVLET_INDICATOR_CLASSES) {
				registerTypeIfPresent(servletIndicatorClass, classLoader, hints);
			}
			registerTypeIfPresent(JERSEY_INDICATOR_CLASS, classLoader, hints);
			registerTypeIfPresent(WEBFLUX_INDICATOR_CLASS, classLoader, hints);
			registerTypeIfPresent(WEBMVC_INDICATOR_CLASS, classLoader, hints);
		}

		private void registerTypeIfPresent(String typeName, ClassLoader classLoader, RuntimeHints hints) {
			if (ClassUtils.isPresent(typeName, classLoader)) {
				hints.reflection().registerType(TypeReference.of(typeName));
			}
		}

	}

}
