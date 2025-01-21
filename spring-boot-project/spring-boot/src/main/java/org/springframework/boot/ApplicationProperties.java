/*
 * Copyright 2012-2024 the original author or authors.
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

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.context.properties.bind.BindableRuntimeHintsRegistrar;
import org.springframework.boot.logging.LoggingSystemProperty;
import org.springframework.core.env.Environment;

/**
 * Spring application properties.
 * Spring 应用属性
 *
 * @author Moritz Halbritter
 */
class ApplicationProperties {

	/**
	 * Whether bean definition overriding, by registering a definition with the same name
	 * as an existing definition, is allowed.
	 */
	private boolean allowBeanDefinitionOverriding;

	/**
	 * 是否允许Bean循环依赖，并尝试去解决它们，默认不允许
	 */
	private boolean allowCircularReferences;

	/**
	 * 当应用启动时，用于展示banner
	 */
	private Banner.Mode bannerMode;

	/**
	 * 即当没有非守护线程时，应用是否存活，默认不存活
	 */
	private boolean keepAlive;

	/**
	 * 是否应该支持懒加载，默认不进行懒加载
	 */
	private boolean lazyInitialization = false;

	/**
	 * 在应用启动时是否打印日志信息，默认打印
	 */
	private boolean logStartupInfo = true;

	/**
	 * 应用程序是否应该注册关闭钩子，默认为true
	 */
	private boolean registerShutdownHook = true;

	/**
	 * 要包含在应用上下文中的源（类名，包名，XML资源位置）
	 */
	private Set<String> sources = new LinkedHashSet<>();

	/**
	 * 表名以明确请求特定类型的Web应用程序，如果未设置，根据类路径自动检测
	 */
	private WebApplicationType webApplicationType;

	boolean isAllowBeanDefinitionOverriding() {
		return this.allowBeanDefinitionOverriding;
	}

	void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
		this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
	}

	boolean isAllowCircularReferences() {
		return this.allowCircularReferences;
	}

	void setAllowCircularReferences(boolean allowCircularReferences) {
		this.allowCircularReferences = allowCircularReferences;
	}

	Mode getBannerMode(Environment environment) {
		if (this.bannerMode != null) {
			return this.bannerMode;
		}
		boolean structuredLoggingEnabled = environment.containsProperty(LoggingSystemProperty.CONSOLE_STRUCTURED_FORMAT.getApplicationPropertyName());
		return (structuredLoggingEnabled) ? Mode.OFF : Banner.Mode.CONSOLE;
	}

	void setBannerMode(Mode bannerMode) {
		this.bannerMode = bannerMode;
	}

	boolean isKeepAlive() {
		return this.keepAlive;
	}

	void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	boolean isLazyInitialization() {
		return this.lazyInitialization;
	}

	void setLazyInitialization(boolean lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	boolean isLogStartupInfo() {
		return this.logStartupInfo;
	}

	void setLogStartupInfo(boolean logStartupInfo) {
		this.logStartupInfo = logStartupInfo;
	}

	boolean isRegisterShutdownHook() {
		return this.registerShutdownHook;
	}

	void setRegisterShutdownHook(boolean registerShutdownHook) {
		this.registerShutdownHook = registerShutdownHook;
	}

	Set<String> getSources() {
		return this.sources;
	}

	void setSources(Set<String> sources) {
		this.sources = new LinkedHashSet<>(sources);
	}

	WebApplicationType getWebApplicationType() {
		return this.webApplicationType;
	}

	void setWebApplicationType(WebApplicationType webApplicationType) {
		this.webApplicationType = webApplicationType;
	}

	static class ApplicationPropertiesRuntimeHints extends BindableRuntimeHintsRegistrar {

		ApplicationPropertiesRuntimeHints() {
			super(ApplicationProperties.class);
		}

	}

}
